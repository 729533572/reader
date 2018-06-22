package com.yn.reader.model.chapter;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * 章节
 * Created by sunxy on 2018/3/13.
 */

public class ChapterGroup extends BaseData{
    private List<ChapterListBean>chapters;

    public List<ChapterListBean> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterListBean> chapters) {
        this.chapters = chapters;
    }
}
