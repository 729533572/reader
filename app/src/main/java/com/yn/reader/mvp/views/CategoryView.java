package com.yn.reader.mvp.views;

import com.yn.reader.model.category.CategoryGroup;

/**
 * 分类
 * Created by sunxy on 2018/3/12.
 */

public interface CategoryView extends BaseView {
    void onCategoryLoaded(CategoryGroup categoryGroup);

}
