package com.yn.reader.mvp.views;

import com.yn.reader.model.favorite.FavoriteGroup;

/**
 * Created by sunxy on 2018/3/12.
 */

public interface ShelfView extends BaseView {
    void onCollectionLoaded(FavoriteGroup favoriteGroup);
    void onDeleteCollectionSelection();
}
