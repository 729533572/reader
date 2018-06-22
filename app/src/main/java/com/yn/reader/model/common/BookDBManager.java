package com.yn.reader.model.common;

import android.os.Handler;
import android.os.Looper;

import com.hysoso.www.utillibrary.TimeUtil;
import com.yn.reader.db.BookDao;
import com.yn.reader.db.ChapterListBeanDao;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.dao.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhe on 2018/5/18.
 */

public class BookDBManager {
    private static BookDBManager mSingleInstance = null;
    private BookDao mBookDao;
    private ChapterListBeanDao mChapterListBeanDao;

    public void deleteChapters(long bookId) {
        mChapterListBeanDao.deleteInTx(getChapterList(bookId));
    }

    public interface OnCallBack {
        void callBack();
    }

    private BookDBManager() {
        this.mBookDao = DbHelper.getInstance().getDaoSession().getBookDao();
        this.mChapterListBeanDao = DbHelper.getInstance().getDaoSession().getChapterListBeanDao();
    }

    public static BookDBManager getInstance() {
        if (mSingleInstance == null) {
            synchronized (BookDBManager.class) {
                if (mSingleInstance == null) mSingleInstance = new BookDBManager();
            }
        }
        return mSingleInstance;
    }

    public Book getBook(long bookId) {
        return mBookDao.queryBuilder().where(BookDao.Properties.Bookid.eq(bookId)).build().unique();
    }

    public boolean isAddedToShelf(long bookId) {
        return getBook(bookId) != null;
    }

    public void deleteHistory(long bookId) {
        if (getBook(bookId) == null) return;
        Book book = getBook(bookId);
        if (isCollection(bookId)) {
            book.setIsHistory(false);
            mBookDao.update(book);
        } else mBookDao.delete(book);
    }

    /**
     * 从数据库中删除收藏
     *
     * @param bookId
     */
    public void deleteCollection(long bookId) {
        if (getBook(bookId) == null) return;
        Book book = getBook(bookId);

        if (isHistory(bookId)) {
            book.setCollected(false);
            mBookDao.update(book);
        } else mBookDao.delete(book);
    }

    /**
     * 是否是历史
     *
     * @param bookId
     * @return
     */
    public boolean isHistory(long bookId) {
        return getBook(bookId) != null && getBook(bookId).getIsHistory();
    }


    /**
     * 判断是否是收藏
     *
     * @param bookId
     * @return
     */
    public boolean isCollection(long bookId) {
        return getBook(bookId) != null && getBook(bookId).isCollected();
    }

    public BookDao getBookDao() {
        return mBookDao;
    }

    public void addToHistory(final Book book) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (book == null) return;

                for (ChapterListBean bean : book.getChapterlist()) {
                    bean.setBookId(book.getBookid());
                    mChapterListBeanDao.insertOrReplace(bean);
                }
            }
        }).start();
        Book bean = getBook(book.getBookid());

        if (bean == null) {
            book.setIsHistory(true);
            book.setFinalDate(TimeUtil.getCurrentTime());
            mBookDao.insert(book);
        } else {
            bean.setIsHistory(true);
            bean.setChapterid(book.getChapterid());
            bean.setFinalDate(TimeUtil.getCurrentTime());
            mBookDao.update(bean);
        }
    }

    public void setBookCurrentChapter(long bookId, long chapterId) {
        Book book = getBook(bookId);
        if (book == null) return;

        Book bean = getBook(book.getBookid());
        bean.setChapterid(chapterId);
        mBookDao.update(bean);
    }

    public void collect(final Book book, final boolean isSynchronized) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ChapterListBean bean : book.getChapterlist()) {
                    bean.setBookId(book.getBookid());
                    mChapterListBeanDao.insertOrReplace(bean);
                }
            }
        }).start();

        Book bean = getBook(book.getBookid());

        if (bean == null) {
            book.setCollected(true);
            book.setSynchronized(isSynchronized);
            book.setFinalDate(TimeUtil.getCurrentTime());
            mBookDao.insert(book);
        } else {
            bean.setCollected(true);
            bean.setSynchronized(isSynchronized);
            bean.setChapterid(book.getChapterid());
            bean.setFinalDate(TimeUtil.getCurrentTime());
            mBookDao.update(bean);
        }
    }

    public ChapterListBean getBookCurrentChapter(long chapterId) {
        return mChapterListBeanDao.queryBuilder().where(ChapterListBeanDao.Properties.Chapterid.eq(chapterId)).build().unique();
    }

    public int getChapterSize(long bookId) {
        return mChapterListBeanDao.queryBuilder().where(ChapterListBeanDao.Properties.BookId.eq(bookId)).build().list().size();
    }

    public List<ChapterListBean> getChapterList(long bookId) {
        return mChapterListBeanDao.queryBuilder().where(ChapterListBeanDao.Properties.BookId.eq(bookId)).build().list();
    }

    public void clear() {
        mChapterListBeanDao.deleteAll();
        mBookDao.deleteAll();
    }

    public void saveChapters(final List<ChapterListBean> chapters, final long bookId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ChapterListBean bean : chapters) {
                    bean.setBookId(bookId);
                    mChapterListBeanDao.insertOrReplace(bean);
                }
            }
        }).start();
    }

    public List<Book> getCollections() {
        return mBookDao.queryBuilder().where(BookDao.Properties.Alreadyjoin.eq(1)).orderDesc(BookDao.Properties.FinalDate).build().list();
    }

    public List<Book> getHistories() {
        return mBookDao.queryBuilder().where(BookDao.Properties.IsHistory.eq(true)).orderDesc(BookDao.Properties.FinalDate).build().list();
    }

    public List<Book> getUnUploadedCollections() {
        return mBookDao.queryBuilder().where(BookDao.Properties.Alreadyjoin.eq(1), BookDao.Properties.IsSynchronized.eq(false)).build().list();
    }

    public void setBookUploaded(long bookId, boolean isUploaded) {
        Book book = getBook(bookId);
        if (book == null) return;
        book.setSynchronized(isUploaded);
        mBookDao.update(book);
    }
}
