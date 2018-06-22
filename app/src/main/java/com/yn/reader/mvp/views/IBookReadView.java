//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.mvp.views;

import android.graphics.Paint;


public interface IBookReadView extends BaseView{

    /**
     * 获取当前阅读界面UI画笔
     * @return
     */
    Paint getPaint();

    /**
     * 获取当前小说内容可绘制宽度
     * @return
     */
    int getContentWidth();

    /**
     * 小说数据初始化成功
     */
    void initContentSuccess();




    void showLoadBook();

    void dimissLoadBook();

    void loadLocationBookError();

    void showDownloadMenu();
}
