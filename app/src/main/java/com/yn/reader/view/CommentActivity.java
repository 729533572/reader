package com.yn.reader.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hysoso.www.utillibrary.KeyboardUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.login.utils.LoadingUtils;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.CommentPresenter;
import com.yn.reader.mvp.views.CommentView;
import com.yn.reader.model.comment.Comment;
import com.yn.reader.model.comment.CommentGroup;
import com.yn.reader.util.Constant;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.view.adapter.CommentAdapter;
import com.yn.reader.widget.FullyLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论列表
 */
public class CommentActivity extends BaseMvpActivity implements CommentView, CommentAdapter.OnCommentItemClick {
    private CommentPresenter mCommentPresenter;
    @BindView(R.id.rv_comment_hot)
    RecyclerView rv_comment_hot;//热评列表

    @BindView(R.id.tv_more_hot_comment)
    TextView tv_more_hot_comment;//加载更多热评

    @BindView(R.id.rv_comment_last)
    RecyclerView rv_comment_last;//最新评论

    @BindView(R.id.et_comment_content)
    EditText et_comment_content;

    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;

    private long mBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setToolbarTitle(R.string.comment);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));

        rv_comment_hot.addItemDecoration(itemDecoration);
        rv_comment_last.addItemDecoration(itemDecoration);

        loadData();
    }

    private void loadData() {
        mBookId = getIntent().getLongExtra(Constant.KEY_ID, 0);

        mCommentPresenter = new CommentPresenter(this);
        mCommentPresenter.getHotComments(mBookId, 2);
        mCommentPresenter.getLastComments(mBookId, 2);
    }

    @Override
    public BasePresenter getPresenter() {
        return mCommentPresenter;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @OnClick(R.id.tv_more_hot_comment)
    public void moreHotComment() {
        IntentUtils.startActivity(this, CommentHotMoreActivity.class, mBookId);
    }

    @Override
    public void onCommentsLoaded(CommentGroup commentGroup) {
        if (commentGroup.getComments() != null && !commentGroup.getComments().isEmpty()) {
            final FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            switch (commentGroup.getType()) {
                case Constant.SORT_TYPE_HOT:
                    tv_more_hot_comment.setVisibility(View.VISIBLE);
                    rv_comment_hot.setHasFixedSize(true);
                    rv_comment_hot.setNestedScrollingEnabled(false);
                    rv_comment_hot.setLayoutManager(layoutManager);
                    rv_comment_hot.setAdapter(new CommentAdapter(this, commentGroup.getComments())
                            .setShowLikeAndReport(true).setOnCommentItemClick(this));
                    break;
                case Constant.SORT_TYPE_TIME:
                    rv_comment_last.setHasFixedSize(true);
                    rv_comment_last.setNestedScrollingEnabled(false);
                    rv_comment_last.setLayoutManager(layoutManager);
                    rv_comment_last.setAdapter(new CommentAdapter(this, commentGroup.getComments())
                            .setShowLikeAndReport(true).setOnCommentItemClick(this));
                    break;
            }
        }
    }

    @Override
    public void updateLastComment() {
        mCommentPresenter.getLastComments(mBookId, 2);
    }

    @Override
    public void hideKeyBoardAndLoading() {
        LoadingUtils.hideLoading();
        KeyboardUtil.closeKeyboard(et_comment_content);
    }

    @Override
    public void cleanInputContent() {
        et_comment_content.setText("");
    }

    @Override
    public void enableSendBookComment() {
        tv_send_comment.setEnabled(true);
    }

    @OnClick(R.id.tv_send_comment)
    public void sendBookComment() {
        if (!UserInfoManager.getInstance().isLanded()) {
            IntentUtils.startActivity(this, LoginTipActivity.class);
            return;
        }
        String content = et_comment_content.getText().toString();
        if (StringUtil.isEmpty(content)) return;
        LoadingUtils.showLoading();
        tv_send_comment.setEnabled(false);
        mCommentPresenter.sendBookComment(mBookId, content);
    }

    @Override
    public void like(Comment comment) {

        mCommentPresenter.like(comment.getCommentid());
    }

    @Override
    public void report(Comment comment) {
        mCommentPresenter.report(comment.getCommentid());
    }
}
