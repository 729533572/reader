package com.yn.reader.mvp.presenters;

import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.ShelfView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.favorite.FavoriteDelete;
import com.yn.reader.model.favorite.FavoriteGroup;

import java.util.List;

/**
 * 书架相关
 * Created by sunxy on 2018/3/12.
 */

public class ShelfPresenter extends BasePresenter {
    private ShelfView mShelfView;
    private int currentPage = 1;
    public ShelfPresenter(ShelfView shelfView) {
        this.mShelfView = shelfView;
    }

    @Override
    public BaseView getBaseView() {
        return mShelfView;
    }

    @Override
    public void success(Object o) {
        if(o instanceof FavoriteGroup){
            mShelfView.onCollectionLoaded((FavoriteGroup)o);
        }else if (o instanceof FavoriteDelete){
            mShelfView.onDeleteCollectionSelection();
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public void getFavorite() {
        addSubscription(MiniRest.getInstance().getFavorite(currentPage));
//        ++currentPage;
    }


    public void deleteFavoriteSelections(List<Book> remainBooks) {
        String bookIds = "";
        for (Book bean:remainBooks) {
            bookIds += bean.getBookid() +",";
        }
        if (bookIds.endsWith(","))bookIds = bookIds.substring(0,bookIds.length()-1);
        addSubscription(MiniRest.getInstance().deleteFavorites(bookIds));
    }
}
