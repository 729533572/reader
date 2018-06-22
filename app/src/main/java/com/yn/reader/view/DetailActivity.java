package com.yn.reader.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.DateUtils;
import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.hysoso.www.viewlibrary.MFSStaticRecyclerView;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.BookDetailPresent;
import com.yn.reader.mvp.views.BookDetailView;
import com.yn.reader.model.chapter.ChapterGroup;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.comment.CommentGroup;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.detail.BookDetail;
import com.yn.reader.model.detail.BookDetailBookInfo;
import com.yn.reader.model.detail.BookDetailGroup;
import com.yn.reader.model.detail.GussYouLikeGroup;
import com.yn.reader.util.BitIntentDataManager;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.ShareUtils;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.view.adapter.BookListAdapter;
import com.yn.reader.view.adapter.CommentAdapter;
import com.yn.reader.widget.FullyLinearLayoutManager;
import com.yn.reader.widget.tag.Tag;
import com.yn.reader.widget.tag.TagView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 书籍详情页
 */
public class DetailActivity extends BaseMvpActivity implements BookDetailView {
    private BookDetailPresent detailPresent;
//    private Book bookShelfBean;

    @BindView(R.id.ivBookCover)
    ImageView bookCover;

    @BindView(R.id.tvBookListTitle)
    TextView bookTitle;

    @BindView(R.id.tvBookListAuthor)
    TextView bookAuthor;

    @BindView(R.id.tvWordCount)
    TextView bookWords;

    @BindView(R.id.tvLatelyUpdate)
    TextView latestUpdateTime;

    @BindView(R.id.book_tag)
    TagView bookTag;

    @BindView(R.id.tvlongIntro)
    TextView bookDes;
    @BindView(R.id.tv_expand)
    TextView tv_expand;

    @BindView(R.id.latest_chapter)
    TextView latest_chapter;
    @BindView(R.id.total_chapter)
    TextView total_chapter;

    @BindView(R.id.rvRecommendBoookList)
    MFSStaticRecyclerView mRecommendRecy;

    @BindView(R.id.rvHotReview)
    RecyclerView rvHotReview;

    @BindView(R.id.comment_layout)
    View comment_layout;

    @BindView(R.id.tvMoreReview)
    View tvMoreReview;

    @BindView(R.id.tv_start_read)
    TextView tv_start_read;

    private long mBookId;

    @BindView(R.id.loadingView)
    View loadingView;

    @BindView(R.id.btnJoinCollection)
    TextView btnJoinCollection;

    private BookDetail mBookDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        loadData();
        mSave.setText(R.string.share);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:待完善
                ShareUtils.showShare(
                        DetailActivity.this,
                        mBookDetail.getBook().getBookname(),
                        "www.baidu.com",
                        "");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeAddedStatus();
    }

    private void loadData() {
        mBookId = getIntent().getLongExtra(Constant.KEY_BOOK_ID, 0);
        detailPresent = new BookDetailPresent(this);

        detailPresent.loadBookDetailData(mBookId);
        detailPresent.gussYouLike(mBookId);
        detailPresent.getComments(mBookId, Constant.SORT_TYPE_HOT, 2);
    }

    @OnClick(R.id.fl_last_chapter)
    public void lastChapter() {
        if (loadingView.getVisibility() == View.VISIBLE) return;

        boolean isFreeLastChapter = false;
        for (ChapterListBean bean : mBookDetail.getBook().getChapterlist()) {
            if (bean.getChapterid() == mBookDetail.getBook().getBooklatestchapter().getChapterid()) {
                if (bean.getChaptershoptype() == 1) {
                    isFreeLastChapter = true;
                    break;
                }
            }
        }
        if (!UserInfoManager.getInstance().isLanded() && !isFreeLastChapter) {
            IntentUtils.startActivity(this, LoginTipActivity.class);
            return;
        }
        mBookDetail.getBook().setChapterid(mBookDetail.getBook().getBooklatestchapter().getChapterid());
        BookDBManager.getInstance().addToHistory(mBookDetail.getBook());
        IntentUtils.startActivity(DetailActivity.this, ReaderActivity.class, mBookId);
    }

    @OnClick(R.id.tv_expand)
    public void expand() {
        if (loadingView.getVisibility() == View.VISIBLE) return;

        tv_expand.setSelected(!tv_expand.isSelected());

        if (tv_expand.isSelected()) tv_expand.setText(R.string.pick_up);
        else tv_expand.setText(R.string.expand);

        bookDes.setMaxLines(tv_expand.isSelected() ? Integer.MAX_VALUE : 3);
    }

    @OnClick(R.id.tv_comment)
    public void comment() {
        if (loadingView.getVisibility() == View.VISIBLE) return;
        IntentUtils.startActivity(this, CommentActivity.class, mBookId);
    }

    @Override
    public void onBookDetailLoaded(BookDetailGroup bookDetailGroup) {
        mBookDetail = bookDetailGroup.getData();

        if (mBookDetail != null) {
            BookDetailBookInfo bookInfo = mBookDetail.getBook();
            if (bookInfo != null) {
                setToolbarTitle(bookInfo.getBookname());
                GlideUtils.load(this, bookInfo.getBookimage(), bookCover, R.mipmap.ic_hold_place_book_detail_cover);
                bookTitle.setText(bookInfo.getBookname());
                bookAuthor.setText(bookInfo.getBookauthor());

                if (!StringUtil.isEmpty(bookInfo.getBooklastupdate()))
                    latestUpdateTime.setText(DateUtils.toToday(TimeUtil.toDate(bookInfo.getBooklastupdate())));

                float wordCount = bookInfo.getBookwordcount() * 1.00f / 10000 * 1.00f;
                DecimalFormat fnum = new DecimalFormat("##0.00");
                bookWords.setText(fnum.format(wordCount) + "万字" + (bookInfo.getBookstatus() == 1 ? "(已完结)" : "(连载中)"));

                bookDes.setText(bookInfo.getBookdesc());

                bookDes.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.i(getClass().getSimpleName(), "行数：" + bookDes.getLineCount() + "");
                        if (bookDes.getLineCount() < 3) tv_expand.setVisibility(View.GONE);
                        else tv_expand.setVisibility(View.VISIBLE);
                    }
                });


                for (String tagString : bookInfo.getBooktags()) {

                    Tag tag = new Tag(tagString);
                    String tagColor = Constant.BOOK_TAG_COLOR[bookInfo.getBooktags().indexOf(tagString) % Constant.BOOK_TAG_COLOR.length];

                    tag.tagTextColor = Color.parseColor(tagColor);

                    tag.layoutColor = Color.parseColor("#ffffff");
                    tag.layoutColorPress = Color.parseColor("#ffffff");

                    tag.radius = 24f;
                    tag.tagTextSize = 9f;
                    tag.layoutBorderSize = 1f;
                    tag.layoutBorderColor = Color.parseColor(tagColor);
                    tag.layoutBorderColorPress = Color.parseColor(tagColor);

                    bookTag.addTag(tag);
                }
                if (bookInfo.getBooklatestchapter() != null && !TextUtils.isEmpty(bookInfo.getBooklatestchapter().getChaptername())) {
                    latest_chapter.setText(bookInfo.getBooklatestchapter().getChaptername());
                }
                total_chapter.setText(String.format(getString(R.string.total_chapter), bookInfo.getBooktotoalchapter()));

                updateBookShelf(mBookDetail);
                detailPresent.getChapters(mBookId);
            }
        } else {
            hideLoadingView();
        }

    }

    private void judgeAddedStatus() {
        if (mBookDetail == null) return;

        try {

            if (mBookDetail.getBook().getChapterlist().indexOf(mBookDetail.getBook().getCurrentChapter()) > 0
                    || mBookDetail.getBook().getDurChapterPage() > 0) {
                tv_start_read.setText("继续阅读");
            } else tv_start_read.setText("开始阅读");


            boolean isAdded = UserInfoManager.getInstance().isLanded() ? (mBookDetail.getBook().isCollected() || BookDBManager.getInstance().isCollection(mBookId)) : BookDBManager.getInstance().isCollection(mBookId);
            addedToShelf(isAdded);

        } catch (Exception ex) {
            LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }
    }

    private void updateBookShelf(BookDetail bookDetail) {
        Book book = BookDBManager.getInstance().getBook(bookDetail.getBook().getBookid());
        if (book == null) return;

        book.setBookname(bookDetail.getBook().getBookname());
        book.setBookauthor(bookDetail.getBook().getBookauthor());
        book.setBookimage(bookDetail.getBook().getBookimage());
        book.setBookdesc(bookDetail.getBook().getBookdesc());

        book.setTag(String.valueOf(bookDetail.getBook().getBookid()));
        book.setNoteUrl(String.valueOf(bookDetail.getBook().getBookid()));
        book.setFinalDate(bookDetail.getBook().getFinalDate());

        if (UserInfoManager.getInstance().isLanded()) {
            synchronized (this) {
                Boolean isCollected = book.isCollected() || bookDetail.getBook().isCollected();
                book.setCollected(isCollected);
                mBookDetail.getBook().setCollected(isCollected);

                if (isCollected && !book.isCollected())
                    book.setSynchronized(false);
                else book.setSynchronized(true);

                if (isCollected && bookDetail.getCurrentprogress() != null) {
                    book.setChapterprogress(bookDetail.getCurrentprogress().getProgress());
                    book.setChapterid(bookDetail.getCurrentprogress().getChapterid());
                }
            }
        }
        BookDBManager.getInstance().getBookDao().update(book);
    }


    @Override
    public void onRecommendLoaded(GussYouLikeGroup gussYouLikeGroup) {
        if (gussYouLikeGroup.getData() != null && gussYouLikeGroup.getData().getBooks() != null) {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));
            mRecommendRecy.addItemDecoration(itemDecoration);
            mRecommendRecy.setHasFixedSize(true);
            mRecommendRecy.setNestedScrollingEnabled(false);
            mRecommendRecy.setLayoutManager(layoutManager);
            mRecommendRecy.setAdapter(new BookListAdapter(this, gussYouLikeGroup.getData().getBooks()).setShowAdvertisement(false));
        }
    }

    @Override
    public void onChaptersLoaded(ChapterGroup chapterGroup) {
        if (chapterGroup.getChapters() != null) {
            mBookDetail.getBook().setChapterlist(chapterGroup.getChapters());
            judgeAddedStatus();
            BookDBManager.getInstance().saveChapters(chapterGroup.getChapters(), mBookDetail.getBook().getBookid());
        }
        hideLoadingView();
    }

    @Override
    protected void onDestroy() {
        if (!BookDBManager.getInstance().isAddedToShelf(mBookId))
            BookDBManager.getInstance().deleteChapters(mBookId);
        super.onDestroy();
    }

    @Override
    public void onCommentsLoaded(CommentGroup commentGroup) {
        if (commentGroup.getComments() != null && !commentGroup.getComments().isEmpty()) {
            comment_layout.setVisibility(View.VISIBLE);
            tvMoreReview.setVisibility(View.VISIBLE);
            final FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            rvHotReview.setHasFixedSize(true);
            rvHotReview.setNestedScrollingEnabled(false);
            rvHotReview.setLayoutManager(layoutManager);
            rvHotReview.setAdapter(new CommentAdapter(this, commentGroup.getComments()).setShowLikeAndReport(false));
        }
    }

    @Override
    public void onCachedBookLoaded(Book book) {

    }

    private void addedToShelf(boolean isAdded) {
        btnJoinCollection.setEnabled(!isAdded);
        btnJoinCollection.setText(isAdded ? R.string.added_to_shelf : R.string.add_to_shelf);
        if (isAdded) {
            btnJoinCollection.setTextColor(Color.parseColor("#fefefe"));
            btnJoinCollection.setBackgroundColor(Color.parseColor("#cccccc"));
        } else {
            btnJoinCollection.setTextColor(getResources().getColor(R.color.colorAccent));
            btnJoinCollection.setBackgroundColor(getResources().getColor(R.color.clear));
        }
    }


    private void hideLoadingView() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onAddBookShelfSuccess() {
        ToastUtil.showLong(this, R.string.add_shelf_success);
        addedToShelf(true);
    }


    @OnClick(R.id.tvMoreReview)
    public void moreComment() {
        if (loadingView.getVisibility() == View.VISIBLE) return;
        IntentUtils.startActivity(this, CommentActivity.class, mBookId);
    }

    @OnClick(R.id.tv_start_read)
    public void startRead() {
        if (loadingView.getVisibility() == View.VISIBLE) return;

        long currentChapterId = BookDBManager.getInstance().isAddedToShelf(mBookId)
                ? BookDBManager.getInstance().getBook(mBookId).getChapterid()
                : mBookDetail.getBook().getChapterid();

        boolean isFreeLastChapter = false;
        for (ChapterListBean bean : mBookDetail.getBook().getChapterlist()) {
            if (bean.getChapterid() == currentChapterId) {
                if (bean.getChaptershoptype() == 1) {
                    isFreeLastChapter = true;
                    break;
                }
            }
        }
        if (!UserInfoManager.getInstance().isLanded() && !isFreeLastChapter) {
            IntentUtils.startActivity(this, LoginTipActivity.class);
            return;
        }

        mBookDetail.getBook().setChapterid(currentChapterId);
        BookDBManager.getInstance().addToHistory(mBookDetail.getBook());
        IntentUtils.startActivity(DetailActivity.this, ReaderActivity.class, mBookId);

    }

    @OnClick(R.id.all_chapters)
    public void allChapters() {
        if (loadingView.getVisibility() == View.VISIBLE) return;
        //进入阅读
        Intent intent = new Intent(this, ChapterListActivity.class);
        String key = String.valueOf(System.currentTimeMillis());
        intent.putExtra("data_key", key);

        BitIntentDataManager.getInstance().putData(key, mBookDetail.getBook());

        startActivity(intent);
    }

    @OnClick(R.id.btnJoinCollection)
    public void addBookShelf() {
        if (loadingView.getVisibility() == View.VISIBLE) return;
        detailPresent.addToShelf(mBookDetail.getBook());
        onAddBookShelfSuccess();
    }

    @Override
    public BasePresenter getPresenter() {
        return detailPresent;
    }

    @Override
    public BaseActivity getContext() {
        return this;
    }


}
