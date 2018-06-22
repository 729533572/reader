package com.yn.reader.mvp.views;

import com.yn.reader.model.chapter.ChapterGroup;
import com.yn.reader.model.comment.CommentGroup;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.detail.BookDetailGroup;
import com.yn.reader.model.detail.GussYouLikeGroup;

/**
 * 小说详情
 * Created by sunxy on 2018/3/12.
 */

public interface BookDetailView extends BaseView {
    void onBookDetailLoaded(BookDetailGroup bookDetailGroup);
    void onRecommendLoaded(GussYouLikeGroup gussYouLikeGroup);
    void onChaptersLoaded(ChapterGroup chapterGroup);
    void onCommentsLoaded(CommentGroup commentGroup);

    /**
     * @param book
     * @deprecated
     */
    void onCachedBookLoaded(Book book);
    void onAddBookShelfSuccess();
}
