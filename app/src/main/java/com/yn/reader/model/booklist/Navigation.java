package com.yn.reader.model.booklist;

/**
 * 分类导航
 * Created by sunxy on 2018/3/16.
 */

public class Navigation {
    private Integer id;
    private String name;
    private boolean isSelected = false;

    public Navigation() {
    }

    public Navigation(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
