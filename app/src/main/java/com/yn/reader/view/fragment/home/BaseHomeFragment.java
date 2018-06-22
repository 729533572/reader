package com.yn.reader.view.fragment.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.HomePresenter;
import com.yn.reader.mvp.views.HomeView;
import com.yn.reader.model.home.ChangeBatch;
import com.yn.reader.model.home.ChangeBatchGroup;
import com.yn.reader.model.home.Channel;
import com.yn.reader.model.home.Divider;
import com.yn.reader.model.home.HomeGroup;
import com.yn.reader.model.home.HotCategoryGroup;
import com.yn.reader.model.home.TopBanner;
import com.yn.reader.util.Constant;
import com.yn.reader.util.SwipeRefreshLayoutUtil;
import com.yn.reader.view.adapter.ImagePagerAdapter;
import com.yn.reader.view.fragment.BaseFragment;
import com.yn.reader.view.holder.ChannelItemViewBinder;
import com.yn.reader.view.holder.DividerItemViewBinder;
import com.yn.reader.view.holder.HomeHotCategoryViewBinder;
import com.yn.reader.view.holder.TopBannerItemViewBinder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.yn.reader.model.home.Channel.PRE_START_INDEX;


/**
 * 统一处理精选男生女生频道
 * Created by sunxy on 2018/2/7.
 */

public abstract class BaseHomeFragment extends BaseFragment implements HomeView, SwipeRefreshLayout.OnRefreshListener {
    private final String KEY_JSON_DATA = "key_json_data";

    private ViewGroup containerView;
    AutoScrollViewPager viewPager;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private View topBanner;


    //    public static final int SPAN_COUNT = 4;
    @VisibleForTesting
    List<Object> items;
    @VisibleForTesting
    MultiTypeAdapter adapter;

    private int screen_width;

    protected HomePresenter homePresenter;

    @Override
    public BasePresenter getPresenter() {
        return homePresenter;
    }

    protected abstract int getHomeType();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        containerView = (ViewGroup) inflater.inflate(R.layout.fragment_base_home, container, false);
        ButterKnife.bind(this, containerView);
        SwipeRefreshLayoutUtil.styleSwipeRefreshLayout(swipeRefreshLayout);
        return containerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        homePresenter = new HomePresenter(this);
        screen_width = getResources().getDisplayMetrics().widthPixels;
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.setOnRefreshListener(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                homePresenter.getHomePageInfo(getHomeType());
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }

    @Override
    public void onDataLoaded(HomeGroup homeGroup) {
        swipeRefreshLayout.setRefreshing(false);
        initAutoScrollViewPager(homeGroup);
        initAdapter();
        bindData(homeGroup);
    }

    @Override
    public void onChangeBatchLoaded(ChangeBatchGroup changeBatchGroup) {
        ChangeBatch changeBatch = changeBatchGroup.getData();
        if (changeBatch != null) {
            for (Object object : items) {
                if (object instanceof Channel) {
                    Channel channel = (Channel) object;
                    if (channel.getHome_channel_id() == changeBatch.getHomechannelid()) {
                        if (changeBatch.getBooks().size() == 0) {
                            channel.setCurrentIndex(PRE_START_INDEX);
                        }
                        channel.getBooks().clear();
                        channel.getBooks().addAll(changeBatch.getBooks());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void initAdapter() {
        adapter = new MultiTypeAdapter();

        adapter.register(TopBanner.class, new TopBannerItemViewBinder(topBanner));
        adapter.register(Divider.class, new DividerItemViewBinder(screen_width));
        adapter.register(Channel.class, new ChannelItemViewBinder(getActivity(), homePresenter));
        adapter.register(HotCategoryGroup.class, new HomeHotCategoryViewBinder(getActivity(), getHomeType()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void initAutoScrollViewPager(HomeGroup homeGroup) {
        if (homeGroup.getData() != null && homeGroup.getData().getBanners() != null && !homeGroup.getData().getBanners().isEmpty()) {
            topBanner = LayoutInflater.from(getActivity()).inflate(R.layout.layout_auto_scroll_viewpager, containerView, false);
            viewPager = topBanner.findViewById(R.id.view_pager);
            viewPager.setAdapter(new ImagePagerAdapter(getActivity(), homeGroup.getData().getBanners()).setInfiniteLoop(true));
            viewPager.setInterval(2000);
            viewPager.startAutoScroll();
            viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % homeGroup.getData().getBanners().size());
        }
    }

    /**
     * 绑定数据
     */
    private void bindData(HomeGroup homeGroup) {
        if (homeGroup.getData() != null && homeGroup.getData().getChannels() != null && !homeGroup.getData().getChannels().isEmpty()) {
            if (items == null) items = new ArrayList<>();
            items.clear();

            items.add(new TopBanner());
            for (Channel channel : homeGroup.getData().getChannels()) {
                channel.setType(getHomeType());
                items.add(channel);
                if (!(channel.getAd() != null && channel.getAd().isIsshow()))
                    items.add(new Divider());
            }

            if (homeGroup.getData().getHotchannels() != null && homeGroup.getData().getHotchannels().size() > 0) {
                items.add(new HotCategoryGroup(homeGroup.getData().getHotchannels()));
                items.add(new Divider());
            }
            adapter.setItems(items);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            }, 400);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager != null) {
            viewPager.startAutoScroll();
        }
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                homePresenter.getHomePageInfo(getHomeType());
                handler.removeCallbacks(this);
            }
        }, Constant.TIME_DELAY);
    }
}