package com.yn.reader.mvp.views;

import com.yn.reader.model.booklist.BookListGroup;
import com.yn.reader.model.booklist.NavigationCategory;

/**
 * 书籍列表
 * Created by sunxy on 2018/3/16.
 */

public interface BookListView extends BaseView {
    void onBookListLoaded(BookListGroup bookListGroup);
    void onCategoryLoaded(NavigationCategory navigationCategory);
}
