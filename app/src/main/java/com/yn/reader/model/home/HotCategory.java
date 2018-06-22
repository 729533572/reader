package com.yn.reader.model.home;

/**
 * Created by luhe on 2018/5/16.
 */

public class HotCategory {
    /**
     * "categoryid":"2",
     * "categoryname":"玄幻魔幻",
     * "categoryicon":"http://114.55.232.1/product/web/image/novel/books/cover/cover.png"
     **/
    private int categoryid;
    private String categoryname;
    private String categoryicon;
    private int sex;

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }
}
