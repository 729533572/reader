package com.yn.reader.mvp.presenters;


import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.alibaba.fastjson.JSON;
import com.hwangjr.rxbus.RxBus;
import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.base.observer.SimpleObserver;
import com.yn.reader.db.BookContentBeanDao;
import com.yn.reader.mvp.views.BaseView;
import com.yn.reader.mvp.views.IBookReadView;
import com.yn.reader.network.api.MiniRest;
import com.yn.reader.model.chapter.BookContentGroup;
import com.yn.reader.model.chapter.ChapterGroup;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.dao.BookContentBean;
import com.yn.reader.model.dao.DbHelper;
import com.yn.reader.model.dao.ReadBookContentBean;
import com.yn.reader.model.favorite.FavoritePost;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.RxBusTag;
import com.yn.reader.widget.BookContentView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 广告信息接口
 * Created by sunxy on 2018/1/10.
 */

public class ReaderPresenter extends BasePresenter {
    private IBookReadView mView;
    private Book mBook;

    public ReaderPresenter(IBookReadView bookReadView, Book book) {
        this.mView = bookReadView;
        mBook = book;
    }


    private int pageLineCount = 5;   //假设5行一页

    public void initContent() {
        mView.initContentSuccess();
    }

    public void loadContentData(final BookContentView bookContentView, final long bookTag, final int chapterIndex, int pageIndex) {
        if (chapterIndex > -1 && mBook.getChapterlist().size() > 0 && chapterIndex < mBook.getChapterlist().size()) {
            bookContentView.setBook(mBook);

            ChapterListBean chapterListBean = mBook.getChapterlist().get(chapterIndex);

            if (chapterListBean.getChaptershoptype() != 1) {
                loadBookChapterContentFromNet(bookContentView, bookTag, chapterIndex, pageIndex);
            } else {
                if (chapterListBean.getBookContentBean() != null
                        && chapterListBean.getBookContentBean().getChapterdetails() != null) {
                    loadBookChapterContentFromMemory(bookContentView, bookTag, chapterIndex, pageIndex);

                } else {
                    loadBookChapterContentFromDB(bookContentView, bookTag, chapterIndex, pageIndex);
                }
            }
        } else {
            if (bookContentView != null && bookTag == bookContentView.getqTag())
                bookContentView.loadError();
        }
    }

    private synchronized void loadBookChapterContentFromDB(final BookContentView bookContentView, final long bookTag, final int chapterIndex, int pageIndex) {
        final int finalPageIndex1 = pageIndex;
        Observable.create(new ObservableOnSubscribe<ReadBookContentBean>() {
            @Override
            public void subscribe(ObservableEmitter<ReadBookContentBean> e) throws Exception {
                List<BookContentBean> tempList = DbHelper.getInstance().getDaoSession().getBookContentBeanDao().queryBuilder().where(BookContentBeanDao.Properties.DurChapterUrl.eq(mBook.getChapterlist().get(chapterIndex).getChapterid())).build().list();
                e.onNext(new ReadBookContentBean(tempList == null ? new ArrayList<BookContentBean>() : tempList, finalPageIndex1));
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .compose(((BaseActivity) mView.getContext()).<ReadBookContentBean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SimpleObserver<ReadBookContentBean>() {
                    @Override
                    public void onNext(ReadBookContentBean tempList) {
                        if (tempList.getBookContentList() != null
                                && tempList.getBookContentList().size() > 0
                                && tempList.getBookContentList().get(0).getChapterdetails() != null) {

                            mBook.getChapterlist().get(chapterIndex).setBookContentBean(tempList.getBookContentList().get(0));
                            loadBookChapterContentFromMemory(bookContentView, bookTag, chapterIndex, tempList.getPageIndex());

                        } else {
                            final int finalPageIndex1 = tempList.getPageIndex();
                            loadBookChapterContentFromNet(bookContentView, bookTag, chapterIndex, finalPageIndex1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private synchronized void loadBookChapterContentFromMemory(final BookContentView bookContentView, final long bookTag, final int chapterIndex, int pageIndex) {
        //是否对章节内容进行分行处理
        ChapterListBean chapterListBean = mBook.getChapterlist().get(chapterIndex);

        if (chapterListBean.getBookContentBean().getLineSize() == mView.getPaint().getTextSize()
                && chapterListBean.getBookContentBean().getLineContent().size() > 0) {

            showChapterContent(bookContentView, bookTag, chapterIndex, pageIndex);

        } else
            branchDisplayChapterContent(bookContentView, bookTag, chapterIndex, pageIndex);
    }

    /**
     * 显示章节内容
     *
     * @param bookContentView
     * @param bookTag
     * @param chapterIndex
     * @param pageIndex
     */
    private void showChapterContent(final BookContentView bookContentView, final long bookTag, final int chapterIndex, int pageIndex) {
        //已有数据
        ChapterListBean chapterListBean = mBook.getChapterlist().get(chapterIndex);

        int tempCount = (int) Math.ceil(chapterListBean.getBookContentBean().getLineContent().size() * 1.0 / pageLineCount) - 1;

        if (pageIndex == BookContentView.DURPAGEINDEXBEGIN) {
            pageIndex = 0;
        } else if (pageIndex == BookContentView.DURPAGEINDEXEND) {
            pageIndex = tempCount;
        } else {
            if (pageIndex >= tempCount) {
                pageIndex = tempCount;
            }
        }

        int start = pageIndex * pageLineCount;
        int end = pageIndex == tempCount ? chapterListBean.getBookContentBean().getLineContent().size() : start + pageLineCount;
        if (bookContentView != null && bookTag == bookContentView.getqTag()) {
            bookContentView.updateData(bookTag, chapterListBean.getChaptername()
                    , chapterListBean.getBookContentBean().getLineContent().subList(start, end)
                    , chapterIndex
                    , mBook.getChapterlist().size()
                    , pageIndex
                    , tempCount + 1);
        }
        mView.dimissLoadBook();
    }

    /**
     * 对章节内容进行分行
     *
     * @param bookTag
     * @param chapterIndex
     * @param pageIndex
     */
    private void branchDisplayChapterContent(final BookContentView bookContentView, final long bookTag, final int chapterIndex, int pageIndex) {
        ChapterListBean chapterListBean = mBook.getChapterlist().get(chapterIndex);

        chapterListBean.getBookContentBean().setLineSize(mView.getPaint().getTextSize());

        final int finalPageIndex = pageIndex;

        branchDisplay(chapterListBean.getBookContentBean().getChapterdetails(), new SimpleObserver<List<String>>() {
            @Override
            public void onNext(List<String> value) {
                mBook.getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().clear();
                mBook.getChapterlist().get(chapterIndex).getBookContentBean().getLineContent().addAll(value);
                showChapterContent(bookContentView, bookTag, chapterIndex, finalPageIndex);
            }

            @Override
            public void onError(Throwable e) {
                if (bookContentView != null && bookTag == bookContentView.getqTag())
                    bookContentView.loadError();
            }
        });
    }

    /**
     * 对显示内容进行分行
     *
     * @param shownContent
     * @param simpleObserver
     */
    private void branchDisplay(String shownContent, SimpleObserver simpleObserver) {
        SeparateParagraphtoLines(shownContent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(((BaseActivity) mView.getContext()).<List<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(simpleObserver);
    }

    private void loadBookChapterContentFromNet(final BookContentView bookContentView, final long bookTag, final int chapterIndex, final int finalPageIndex1) {
        final ChapterListBean chapterListBean = mBook.getChapterlist().get(chapterIndex);
        getBookContent(chapterListBean.getChapterid())
                .map(new Function<BookContentGroup, BookContentGroup>() {
                    @Override
                    public BookContentGroup apply(final BookContentGroup bookContentGroup) throws Exception {
                        LogUtil.e(getClass().getSimpleName(), "apply");
                        try {
                            BookContentBean bookContentBean = bookContentGroup.getData();
                            if (bookContentGroup.getStatus() != 1) {
                                bookContentView.setBusinessInfo(bookContentGroup);
                                bookContentBean.setChapterdetails(bookContentGroup.getChapterdetails());
                                bookContentBean.setChaptertitle(chapterListBean.getChaptername());
                                if (chapterListBean.getChaptershoptype() == 1)
                                    chapterListBean.setChaptershoptype(0);

                            } else if (chapterListBean.getChaptershoptype() == 0)
                                chapterListBean.setChaptershoptype(1);

                            bookContentBean.setDurChapterUrl(String.valueOf(chapterListBean.getChapterid()));
                            if (bookContentBean.getRight()) {
                                DbHelper.getInstance().getDaoSession().getBookContentBeanDao().insertOrReplace(bookContentBean);
                                chapterListBean.setHasCache(true);
                                DbHelper.getInstance().getDaoSession().getChapterListBeanDao().update(chapterListBean);
                            }
                        } catch (Exception ignored) {
                        }
                        return bookContentGroup;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .compose(((BaseActivity) mView.getContext()).<BookContentGroup>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SimpleObserver<BookContentGroup>() {
                    @Override
                    public void onNext(BookContentGroup value) {
                        BookContentBean bookContentBean = value.getData();
                        if (bookContentBean != null) {
                            if (!StringUtil.isEmpty(bookContentBean.getDurChapterUrl())) {
                                chapterListBean.setBookContentBean(bookContentBean);
                                if (bookTag == bookContentView.getqTag())
                                    loadBookChapterContentFromMemory(bookContentView, bookTag, chapterIndex, finalPageIndex1);
                            } else {
                                if (bookContentView != null && bookTag == bookContentView.getqTag())
                                    bookContentView.loadError();
                            }
                        }
                        if (value.getStatus() != 1) {
                            if (bookContentView != null && bookTag == bookContentView.getqTag()) {
                                bookContentView.shouldBuy();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        if (bookContentView != null && bookTag == bookContentView.getqTag())
                            bookContentView.loadError();
                    }
                });
    }


    public void saveProgress() {
        if (mBook != null) {
            Observable.create(new ObservableOnSubscribe<Book>() {
                @Override
                public void subscribe(ObservableEmitter<Book> e) throws Exception {
                    mBook.setFinalDate(System.currentTimeMillis());
                    DbHelper.getInstance().getDaoSession().getBookDao().insertOrReplace(mBook);
                    e.onNext(mBook);
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.newThread())
                    .subscribe(new SimpleObserver<Book>() {
                        @Override
                        public void onNext(Book value) {
                            RxBus.get().post(RxBusTag.UPDATE_BOOK_PROGRESS, mBook);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
            if (mBook.isCollected())
                updateFavorite();
        }
    }

    public void updateFavorite() {
        FavoritePost favoritePost = new FavoritePost();
        favoritePost.setUserid(AppPreference.getInstance().getUid());

        favoritePost.setBookid(mBook.getBookid());
        favoritePost.setChapterid(mBook.getChapterid());
        //TODO:下一行代码是有问题需要改正的
        favoritePost.setProgress(StringUtil.isEmpty(mBook.getChapterprogress()) ? "0" : mBook.getChapterprogress());

        favoritePost.setUpdatedate(TimeUtil.getCurrentTime() + "");

        List<FavoritePost> favoritePosts = new ArrayList<>(1);
        favoritePosts.add(favoritePost);
        addSubscription(MiniRest.getInstance().addFavorite(JSON.toJSONString(favoritePosts)));
    }

    public Observable<List<String>> SeparateParagraphtoLines(final String paragraphstr) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                TextPaint mPaint = (TextPaint) mView.getPaint();
                mPaint.setSubpixelText(true);
                /**
                 * 获取小说内容绘制参数的关键代码
                 * **/
                Layout tempLayout = new StaticLayout(paragraphstr, mPaint, mView.getContentWidth(), Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
                LogUtil.i(getClass().getSimpleName(), "内容高度:" + tempLayout.getHeight() + "\n" + "内容行数：" + tempLayout.getLineCount());

                List<String> linesdata = new ArrayList<String>();
                for (int i = 0; i < tempLayout.getLineCount(); i++) {
                    linesdata.add(paragraphstr.substring(tempLayout.getLineStart(i), tempLayout.getLineEnd(i)));
                }
                e.onNext(linesdata);
                e.onComplete();
            }
        });
    }

    public void setPageLineCount(int pageLineCount) {
        this.pageLineCount = pageLineCount;
    }

    public interface OnAddListner {
        void addSuccess();
    }

    public void addToShelf(final OnAddListner addListner) {
        if (mBook != null) {
            updateFavorite();
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                    BookDBManager.getInstance().collect(mBook, false);
                    RxBus.get().post(RxBusTag.HAD_ADD_BOOK, mBook);
                    e.onNext(true);
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleObserver<Object>() {
                        @Override
                        public void onNext(Object value) {
                            if (addListner != null)
                                addListner.addSuccess();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    @Override
    public BaseView getBaseView() {
        return mView;
    }

    @Override
    public void success(Object o) {
        if (o instanceof ChapterGroup) {

        }
    }

    @Override
    public void onFailure(int code, String msg) {
        super.onFailure(code, msg);
//        splashView.error(code,msg);
    }

    @Override
    public void detachView() {
        unSubscribe();
    }

    public Observable<BookContentGroup> getBookContent(long chapterId) {
        return MiniRest.getInstance().getBookContent(chapterId);
    }
}
