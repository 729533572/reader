package com.yn.reader.model.chaptersPrice;

import com.yn.reader.model.BaseData;

/**
 * Created by luhe on 2018/3/26.
 */

public class ChaptersPrice extends BaseData{
    /*
    "realchapterprice":28.8,
    "countchapter":"11",
    "allchapterprice":"32"
    * */
    private Float realchapterprice;
    private int countchapter;
    private Float allchapterprice;

    public void setRealchapterprice(Float realchapterprice) {
        this.realchapterprice = realchapterprice;
    }

    public Float getRealchapterprice() {
        return realchapterprice;
    }

    public void setCountchapter(int countchapter) {
        this.countchapter = countchapter;
    }

    public int getCountchapter() {
        return countchapter;
    }

    public void setAllchapterprice(Float allchapterprice) {
        this.allchapterprice = allchapterprice;
    }

    public Float getAllchapterprice() {
        return allchapterprice;
    }
}
