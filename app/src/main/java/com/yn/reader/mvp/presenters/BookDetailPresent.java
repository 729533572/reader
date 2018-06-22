package com.yn.reader.mvp.presenters;

import com.yn.reader.base.observer.SimpleObserver;
import com.yn.reader.db.BookDao;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.BookDetailView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.chapter.ChapterGroup;
import com.yn.reader.model.comment.CommentResponse;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.detail.BookDetailGroup;
import com.yn.reader.model.detail.GussYouLikeGroup;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 小说详情
 * Created by sunxy on 2018/3/12.
 */

public class BookDetailPresent extends BasePresenter {
    private BookDetailView bookDetailView;
    private int currentPage = 1;

    public BookDetailPresent(BookDetailView bookDetailView) {
        this.bookDetailView = bookDetailView;
    }

    public void loadBookDetailData(long bookId) {
        addSubscription(MiniRest.getInstance().getBookDetailInfo(bookId));
    }

    public void gussYouLike(long bookId) {
        addSubscription(MiniRest.getInstance().gussYouLike(bookId));
    }

    public void getChapters(long bookId) {
        addSubscription(MiniRest.getInstance().getChapters(bookId));
    }

    public void getComments(long bookId, int sortType, int pageSize) {
        addSubscription(MiniRest.getInstance().getComments(bookId, currentPage, sortType, pageSize));
        ++currentPage;
    }

    @Override
    public BaseView getBaseView() {
        return bookDetailView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof BookDetailGroup) {
            bookDetailView.onBookDetailLoaded((BookDetailGroup) o);
        } else if (o instanceof GussYouLikeGroup) {
            bookDetailView.onRecommendLoaded((GussYouLikeGroup) o);
        } else if (o instanceof ChapterGroup) {
            bookDetailView.onChaptersLoaded((ChapterGroup) o);
        } else if (o instanceof CommentResponse) {
            bookDetailView.onCommentsLoaded(((CommentResponse) o).getData());
        }
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    /**
     * @param bookId
     * @deprecated
     */
    public void getBookShelfInfo(final String bookId) {
        Observable.create(new ObservableOnSubscribe<Book>() {
            @Override
            public void subscribe(ObservableEmitter<Book> e) throws Exception {
                Book book = DbHelper.getInstance().getDaoSession().getBookDao().queryBuilder().where(BookDao.Properties.Bookid.eq(bookId)).build().unique();
                e.onNext(book);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Book>() {
                    @Override
                    public void onNext(Book value) {
                        bookDetailView.onCachedBookLoaded(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        bookDetailView.onCachedBookLoaded(null);
                    }
                });
    }


    public void addToShelf(Book book) {
        BookDBManager.getInstance().collect(book, false);
    }
}
