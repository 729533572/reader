package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.CategoryView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.category.CategoryGroup;

/**
 * 主界面小说分类
 * Created by sunxy on 2018/3/12.
 */

public class CategoryPresenter extends BasePresenter {
    private CategoryView categoryView;

    public CategoryPresenter(CategoryView categoryView) {
        this.categoryView = categoryView;
    }

    @Override
    public BaseView getBaseView() {
        return categoryView;
    }

    @Override
    public void success(Object o) {
        if(o instanceof CategoryGroup){
            categoryView.onCategoryLoaded((CategoryGroup)o);
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void load(int sex) {
        addSubscription(MiniRest.getInstance().getHotCategory(sex));
    }
}
