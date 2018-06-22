package com.yn.reader.model.notice;

import com.yn.reader.model.BaseData;

import java.util.List;

/**
 * 消息中心
 * Created by sunxy on 2018/3/14.
 */

public class NoticeGroup extends BaseData {
    private List<Notice>data;

    public List<Notice> getData() {
        return data;
    }

    public void setData(List<Notice> data) {
        this.data = data;
    }
}
