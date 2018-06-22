package com.yn.reader.view.controller.reader;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.util.AppPreference;
import com.yn.reader.view.controller.base.BaseController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luhe on 2018/5/26.
 */

public class BottomMenuController extends BaseController {

    public interface OnBottomMenuCallBack {
        void selectShownChapter(int chapterIndex);

        void showCatalog();

        void lightOrDark();

        void setFont();
    }

    @BindView(R.id.ll_menu_bottom)
    ViewGroup ll_menu_bottom;

    @BindView(R.id.tv_pre)
    TextView tv_pre;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.iv_light_or_dark)
    ImageView iv_light_or_dark;

    @BindView(R.id.tv_current_chapter)
    TextView tv_current_chapter;

    @BindView(R.id.ll_bottom_bar)
    ViewGroup ll_bottom_bar;

    @BindView(R.id.bottom_bar_divider)
    View bottom_bar_divider;

    private Animation mAnimationComeIn;
    private Animation mAnimationGoOut;

    private boolean isShown = false;

    private Book mBook;
    private OnBottomMenuCallBack mOnBottomMenuCallBack;
    private int mCurrentChapterIndex = 0;

    public void setOnBottomMenuCallBack(OnBottomMenuCallBack onBottomMenuCallBack) {
        mOnBottomMenuCallBack = onBottomMenuCallBack;
    }

    public BottomMenuController(View view, Book book) {
        super(view);
        mBook = book;
    }

    @Override
    protected void initElement() {
        ButterKnife.bind(this, mContentView);
    }

    @Override
    protected void initData() {
        mAnimationComeIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_readbook_bottom_in);
        mAnimationGoOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_readbook_bottom_out);
    }

    public void show(int currentChapterIndex) {
        if (isShown) return;
        mContentView.startAnimation(mAnimationComeIn);
        isShown = true;
        appearance();
        mCurrentChapterIndex = currentChapterIndex;
        LogUtil.e(getClass().getSimpleName(), "currentIndex:" + mCurrentChapterIndex);
        updateChapterProgress(mCurrentChapterIndex);
    }

    public void hide() {
        mContentView.startAnimation(mAnimationGoOut);
        isShown = false;
    }

    @OnClick(R.id.tv_pre)
    public void preChapter() {
        if (!isShown) return;
        if (mCurrentChapterIndex <= 0) {
            ToastUtil.showShort(mContext, "没有上一篇");
            return;
        }

        mCurrentChapterIndex--;

        if (mOnBottomMenuCallBack != null)
            mOnBottomMenuCallBack.selectShownChapter(mCurrentChapterIndex);

        updateChapterProgress(mCurrentChapterIndex);
    }

    @OnClick(R.id.tv_next)
    public void nextChapter() {
        if (!isShown) return;
        if (mCurrentChapterIndex >= mBook.getChapterlist().size() - 1) {
            ToastUtil.showShort(mContext, "没有下一篇");
            return;
        }

        mCurrentChapterIndex++;
        if (mOnBottomMenuCallBack != null)
            mOnBottomMenuCallBack.selectShownChapter(mCurrentChapterIndex);
        updateChapterProgress(mCurrentChapterIndex);
    }

    @OnClick(R.id.iv_catalog)
    public void showCatalog() {
        if (!isShown) return;
        if (mOnBottomMenuCallBack != null) mOnBottomMenuCallBack.showCatalog();
        hide();
    }

    @OnClick(R.id.iv_light_or_dark)
    public void lightOrDark() {
        if (!isShown) return;
        AppPreference.getInstance().toggleNightModel();
        appearance();
        if (mOnBottomMenuCallBack != null) mOnBottomMenuCallBack.lightOrDark();
        hide();
    }

    private void appearance() {
        if (AppPreference.getInstance().isNightModel()) {
            bottom_bar_divider.setBackgroundResource(R.color.night_divider_color);
            iv_light_or_dark.setImageResource(R.drawable.icon_light_night);
            ll_bottom_bar.setBackgroundResource(R.color.read_bottom_bar__night_bg_color);
        } else {
            bottom_bar_divider.setBackgroundResource(R.color.divider_color);
            iv_light_or_dark.setImageResource(R.drawable.icon_light_nor);
            ll_bottom_bar.setBackgroundResource(R.color.read_bottom_bar_bg_color);
        }
    }

    @OnClick(R.id.iv_font)
    public void setFont() {
        if (!isShown) return;
        if (mOnBottomMenuCallBack != null) mOnBottomMenuCallBack.setFont();
        hide();
    }

    public void updateChapterProgress(int chapterIndex) {
        if (!isShown) return;
        if (mBook.getChapterlist().size() <= 1) {
            tv_pre.setEnabled(false);
            tv_next.setEnabled(false);
        } else {
            if (chapterIndex == 0) {
                tv_pre.setEnabled(false);
                tv_next.setEnabled(true);
            } else if (chapterIndex >= BookDBManager.getInstance().getChapterSize(mBook.getBookid()) - 1) {
                tv_pre.setEnabled(true);
                tv_next.setEnabled(false);
            } else {
                tv_pre.setEnabled(true);
                tv_next.setEnabled(true);
            }
        }
        try {
            tv_current_chapter.setText(mBook.getChapterName(chapterIndex));
        } catch (Exception ex) {
            LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }

    }

    public boolean isShown() {
        return isShown;
    }
}
