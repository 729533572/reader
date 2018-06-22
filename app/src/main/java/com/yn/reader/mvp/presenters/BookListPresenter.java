package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.BookListView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.booklist.BookListResponse;
import com.yn.reader.model.booklist.NavigationCategoryGroup;

/**
 * 小说列表
 * Created by sunxy on 2018/3/16.
 */

public class BookListPresenter extends BasePresenter {
    private BookListView bookListView;

    public BookListPresenter(BookListView bookListView) {
        this.bookListView = bookListView;
    }
    public void getBooksByCategory(
            int categoryid,
            int sex,
            int pagesize,
            int pageno,
            Integer status,
            Integer chargetype,
            Integer lastnevigate,
            Integer word,
            String tags) {
        addSubscription(
                MiniRest.getInstance().getBooksByCategory(
                        categoryid,
                        sex,
                        pagesize,
                        pageno,
                        status,
                        chargetype,
                        lastnevigate,
                        word,
                        tags
                ));
    }

    public void getBooksByCategory(int categoryid,int sex, int pagesize,int pageno) {
        addSubscription(MiniRest.getInstance().getBooksByCategory(categoryid,sex, pagesize, pageno));
    }

    public void getNavigationCategory(int categoryid) {
        addSubscription(MiniRest.getInstance().getNavigationCategory(categoryid));
    }


    @Override
    public BaseView getBaseView() {
        return bookListView;
    }

    @Override
    public void success(Object o) {
        if(o instanceof BookListResponse){
            bookListView.onBookListLoaded(((BookListResponse)o).getData());
        }else if(o instanceof NavigationCategoryGroup){
            NavigationCategoryGroup navigationCategoryGroup = (NavigationCategoryGroup)o;
            bookListView.onCategoryLoaded(navigationCategoryGroup.getCategorytopnevigate());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }
}
