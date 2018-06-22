package com.yn.reader.view.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hysoso.www.viewlibrary.MFSStaticRecyclerView;
import com.yn.reader.R;
import com.yn.reader.model.home.HotCategory;
import com.yn.reader.model.home.HotCategoryGroup;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.MoreHotCategoryActivity;
import com.yn.reader.view.adapter.HomeHotCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by luhe on 2018/5/16.
 */

public class HomeHotCategoryViewBinder extends ItemViewBinder<HotCategoryGroup, HomeHotCategoryViewBinder.ViewHolder> {
    private Context mContext;
    private int mType;

    public HomeHotCategoryViewBinder(Context context, int type) {
        mContext = context;
        mType = type;
    }

    @NonNull
    @Override
    protected HomeHotCategoryViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_home_hot_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeHotCategoryViewBinder.ViewHolder holder, @NonNull HotCategoryGroup item) {
        holder.assign(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_hot_category)
        MFSStaticRecyclerView rv_hot_category;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void assign(HotCategoryGroup item) {
            List<HotCategory> hotCategories = new ArrayList<>();
            for (HotCategory bean : item.getData()) {
                bean.setSex(mType);
                hotCategories.add(bean);
            }
            HomeHotCategoryAdapter adapter = new HomeHotCategoryAdapter(mContext, hotCategories);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_hot_category.setLayoutManager(linearLayoutManager);
            rv_hot_category.setAdapter(adapter);
        }

        @OnClick(R.id.tv_more)
        public void moreHotCategory() {
            IntentUtils.startActivity(mContext, MoreHotCategoryActivity.class, mType);
        }
    }
}
