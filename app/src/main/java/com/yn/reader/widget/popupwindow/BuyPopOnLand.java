//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.widget.popupwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BuyPopOnLandPresenter;
import com.yn.reader.mvp.views.BuyPopOnLandView;
import com.yn.reader.model.chapter.BookContentGroup;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.Constant;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.BuyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BuyPopOnLand extends PopupWindow implements BuyPopOnLandView {

    public interface OnPaySuccess {
        void success();
    }

    private Activity mContext;
    private View view;
    private BookContentGroup mBookContentGroup;
    private static BuyPopOnLand mInstance;
    private Book mBook;
    private long mChapterIdBought;
    private BuyPopOnLandPresenter mBuyPopOnLandPresenter;
    private OnPaySuccess mOnPaySuccess;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.iv_tip)
    ImageView iv_tip;

    @BindView(R.id.btn_recharge_buy)
    Button btn_recharge_buy;
    @BindView(R.id.btn_buy_batch)
    Button btn_buy_batch;

    @BindView(R.id.tv_open_monthly)
    TextView tv_open_monthly;

    public BuyPopOnLand(Activity context) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContext = context;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        view = LayoutInflater.from(mContext).inflate(AppPreference.getInstance().isNightModel() ? R.layout.view_pop_buy_on_land_night : R.layout.view_pop_buy_on_land, null);
        this.setContentView(view);
        ButterKnife.bind(this, getContentView());

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_windowlight);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_price.setText("");
                tv_balance.setText("");
                iv_tip.setSelected(false);
            }
        });
        mBuyPopOnLandPresenter = new BuyPopOnLandPresenter(this);
    }

    /**
     * 同意自动购买
     */
    @OnClick(R.id.iv_tip)
    public void agreeAutoPurchase() {
        iv_tip.setSelected(!iv_tip.isSelected());
    }

    /**
     * 充值并购买
     */
    @OnClick(R.id.btn_recharge_buy)
    public void rechargeAndBuy() {
        if (mBookContentGroup.getBalance() < mBookContentGroup.getPrice()) {
            /******充值******/
            //TODO:？回来继续支付还是消失
            dismiss();
            IntentUtils.startActivity(mContext, BuyActivity.class, Constant.BUY_TYPE_READ_POINT);
        } else if (mBook != null) {
            /******购买******/
            mBuyPopOnLandPresenter.payForChapter(mBook, mChapterIdBought);
        }
    }

    /**
     * 批量购买
     */
    @OnClick(R.id.btn_buy_batch)
    public void buyBatch() {
        BuyPopOnLandBatch buyPopOnLandBatch = new BuyPopOnLandBatch(getContext());
        buyPopOnLandBatch.setData(mBookContentGroup);
        buyPopOnLandBatch.setBook(mBook);
        buyPopOnLandBatch.setChapterIdBought(mChapterIdBought);
        buyPopOnLandBatch.setOnPaySuccess(mOnPaySuccess);
        if (buyPopOnLandBatch.isShowing()) return;
        dismiss();
        buyPopOnLandBatch.showAtLocation(mContext.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public void setData(BookContentGroup bookContentGroup) {
        mBookContentGroup = bookContentGroup;
        tv_price.setText(bookContentGroup.getPrice() + "点");
        tv_balance.setText(bookContentGroup.getBalance() + "点");
        iv_tip.setSelected(bookContentGroup.getChapterautobuy() == 1);
    }

    public void setBook(Book bookShelf) {
        mBook = bookShelf;
    }

    public void setChapterIdBought(long chapterIdBought) {
        mChapterIdBought = chapterIdBought;
    }

    @Override
    public Activity getContext() {
        return mContext;
    }

    @Override
    public void paySuccess(PayResult result) {
        //购买成功
        dismiss();
        ToastUtil.showShort(mContext, "购买成功");
        if (mOnPaySuccess != null) mOnPaySuccess.success();
    }

    public void setOnPaySuccess(OnPaySuccess onPaySuccess) {
        mOnPaySuccess = onPaySuccess;
    }
}
