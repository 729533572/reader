package com.yn.reader.view.controller.reader;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.yn.reader.R;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.controller.base.BaseController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.grantland.widget.AutofitTextView;

/**
 * Created by luhe on 2018/5/26.
 */

public class TopMenuController extends BaseController {
    @BindView(R.id.ib_return)
    ImageButton ib_return;

    @BindView(R.id.atv_title)
    AutofitTextView atv_title;

    @BindView(R.id.iv_more)
    ImageView iv_more;

    private Animation menuTopIn;
    private Animation menuTopOut;

    private Book mBook;

    public TopMenuController(View view, Book book) {
        super(view);
        mBook = book;
    }

    @Override
    protected void initElement() {
        ButterKnife.bind(this, mContentView);
    }

    @Override
    protected void initData() {
        atv_title.setText(mBook.getCurrentChapter() == null ? "无章节" : mBook.getCurrentChapter().getChaptername());
    }

    @OnClick(R.id.ib_return)
    public void returnBack() {
        IntentUtils.popPreviousActivity((Activity) mContext);
    }
}
