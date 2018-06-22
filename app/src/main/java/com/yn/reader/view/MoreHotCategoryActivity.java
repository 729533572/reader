package com.yn.reader.view;

import android.os.Bundle;

import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.util.Constant;
import com.yn.reader.view.fragment.category.CategoryInnerFragment;

import butterknife.ButterKnife;

public class MoreHotCategoryActivity extends BaseActivity {
    private CategoryInnerFragment mCategoryInnerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_hot_category);
        ButterKnife.bind(this);
        setToolbarTitle("更多热门分类");

        int type = getIntent().getIntExtra(Constant.KEY_TYPE, Constant.HOME_TYPE_BOY);
        mCategoryInnerFragment = CategoryInnerFragment.getInstance(type);

        if (mCategoryInnerFragment.isAdded()) {
            getFragmentManager().beginTransaction().show(mCategoryInnerFragment).commit();
        } else {
            getFragmentManager().beginTransaction().add(R.id.fl_content, mCategoryInnerFragment).commit();
        }
    }
}
