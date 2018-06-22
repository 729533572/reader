package com.yn.reader.model.booklist;

import com.yn.reader.model.BaseData;

/**
 * Created by sunxy on 2018/3/16.
 */

public class NavigationCategoryGroup extends BaseData{
    private NavigationCategory categorytopnevigate;

    public NavigationCategory getCategorytopnevigate() {
        return categorytopnevigate;
    }

    public void setCategorytopnevigate(NavigationCategory categorytopnevigate) {
        this.categorytopnevigate = categorytopnevigate;
    }
}
