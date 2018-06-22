package com.yn.reader.view;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hysoso.www.utillibrary.LogUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.yn.reader.MiniApp;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.BuyPresenter;
import com.yn.reader.mvp.views.BuyView;
import com.yn.reader.model.buy.BuyAdvertisement;
import com.yn.reader.model.buy.BuyChoice;
import com.yn.reader.model.buy.BuyGroup;
import com.yn.reader.model.home.Banner;
import com.yn.reader.model.pay.PayRequire;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.view.adapter.BuyChoiceAdapter;
import com.yn.reader.view.adapter.ImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * 包月购买界面
 */
public class BuyActivity extends BaseMvpActivity implements BuyView {
    private BuyPresenter mBuyPresenter;
    private static int choice_span_count = 2;

    @BindView(R.id.view_pager)
    AutoScrollViewPager viewPager;

    @BindView(R.id.buy_recycler)
    RecyclerView mBuyRecycler;

    @BindView(R.id.iv_bottom_advertisement)
    ImageView iv_bottom_advertisement;

    @BindView(R.id.fl_bottom_advertisement)
    FrameLayout fl_bottom_advertisement;

    @BindView(R.id.tv_explain)
    TextView tv_explain;

    private BuyChoiceAdapter buyChoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);

        if (getIntent().getIntExtra(Constant.KEY_TYPE, Constant.BUY_TYPE_MONTHLY) == Constant.BUY_TYPE_MONTHLY)
            setToolbarTitle(R.string.buy);
        else if (getIntent().getIntExtra(Constant.KEY_TYPE, Constant.BUY_TYPE_MONTHLY) == Constant.BUY_TYPE_READ_POINT)
            setToolbarTitle(R.string.buy_read_point);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, choice_span_count);
        mBuyRecycler.setLayoutManager(layoutManager);
        mBuyRecycler.setHasFixedSize(true);
        mBuyRecycler.setNestedScrollingEnabled(false);
        PostItemDecoration postItemDecoration = new PostItemDecoration(layoutManager.getSpanSizeLookup());
        postItemDecoration.setSpace_vertical(ComUtils.dip2px(16));
        postItemDecoration.setSpace_horizontal(ComUtils.dip2px(18));
        mBuyRecycler.addItemDecoration(postItemDecoration);

        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0)
            viewPager.startAutoScroll();
        loadData();
    }

    private void loadData() {
        mBuyPresenter = new BuyPresenter(this);
        mBuyPresenter.getBuyChoices();
    }

    private void initAutoScrollViewPager(List<BuyAdvertisement> buyAdvertisements) {
        List<Banner> banners = new ArrayList<>();
        for (BuyAdvertisement bean : buyAdvertisements) {
            Banner banner = new Banner(bean);
            banners.add(banner);
        }
        viewPager.setAdapter(new ImagePagerAdapter(this, banners).setInfiniteLoop(true));
        viewPager.setInterval(2000);
        viewPager.startAutoScroll();
//        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % banners.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0)
            viewPager.stopAutoScroll();
    }


    @Override
    public BasePresenter getPresenter() {
        return mBuyPresenter;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onLoadBuyChoices(BuyGroup buyGroup) {
        try {
            if (buyGroup.getAd_top() == null || buyGroup.getAd_top().size() == 0) {
                viewPager.setVisibility(View.GONE);
            } else {
                viewPager.setVisibility(View.VISIBLE);
                initAutoScrollViewPager(buyGroup.getAd_top());
            }
            if (buyGroup.getVip_list() != null && buyGroup.getVip_list().size() > 0) {
                tv_explain.setVisibility(View.VISIBLE);
                buyChoiceAdapter = new BuyChoiceAdapter(
                        this, buyGroup.getVip_list(),
                        getIntent().getIntExtra(Constant.KEY_TYPE, Constant.BUY_TYPE_MONTHLY)
                );
                buyChoiceAdapter.setOnSelectChoiceChanged(new BuyChoiceAdapter.OnSelectChoiceChanged() {
                    @Override
                    public void select(BuyChoice choice) {
                        //TODO:微信支付
                        mBuyPresenter.pay();
                    }
                });
                mBuyRecycler.setAdapter(buyChoiceAdapter);
            }
            if (buyGroup.getAd_bottom() == null) fl_bottom_advertisement.setVisibility(View.GONE);
            else {
                fl_bottom_advertisement.setVisibility(View.VISIBLE);
                GlideUtils.load(getContext(), buyGroup.getAd_bottom().getImage(), iv_bottom_advertisement);
            }
        } catch (Exception ex) {
            LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }
    }

    private class PostItemDecoration extends RecyclerView.ItemDecoration {
        private int space_horizontal;
        private int space_vertical;
        private GridLayoutManager.SpanSizeLookup spanSizeLookup;

        public PostItemDecoration(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
            this.spanSizeLookup = spanSizeLookup;
        }

        public void setSpace_vertical(int space_vertical) {
            this.space_vertical = space_vertical;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildLayoutPosition(view);

            if (spanSizeLookup.getSpanSize(position) != 0) {
                outRect.top = space_vertical;
            }

            if (position % choice_span_count != 0) {
                outRect.left = space_horizontal / 2;
            } else outRect.right = space_horizontal / 2;
        }

        public void setSpace_horizontal(int space_horizontal) {
            this.space_horizontal = space_horizontal;
        }
    }

    @Override
    public void onLoadPayRequire(PayRequire payRequire) {
        PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId = payRequire.getAppid();
        req.partnerId = payRequire.getPartnerid();
        req.prepayId = payRequire.getPrepayid();
        req.nonceStr = payRequire.getNoncestr();
        req.timeStamp = payRequire.getTimestamp();
        req.packageValue = payRequire.getPackageValue();
        req.sign = payRequire.getSign();
        req.extData = payRequire.getExtData(); // optional
        Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        MiniApp.getInstance().getIWXAPI().sendReq(req);
    }
}
