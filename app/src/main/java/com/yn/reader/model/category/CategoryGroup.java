package com.yn.reader.model.category;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * 分类
 * Created by sunxy on 2018/3/12.
 */

public class CategoryGroup extends BaseData {
    private List<CategoryInner>data;

    public List<CategoryInner> getData() {
        return data;
    }

    public void setData(List<CategoryInner> data) {
        this.data = data;
    }
}
