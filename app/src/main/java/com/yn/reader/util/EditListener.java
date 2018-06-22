package com.yn.reader.util;

/**
 * 编辑操作
 * Created by sunxy on 2018/3/19.
 */

public interface EditListener {
    void onEditClicked();//编辑状态
    void onCompleteClicked();//完成状态
    void onSelectAllChecked();//全选状态
    void deleteSelect();//删除选择
    void mark2Read();//标记为已读
    void refresh();//标记为已读
}
