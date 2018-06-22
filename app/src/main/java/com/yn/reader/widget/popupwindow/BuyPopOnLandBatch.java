//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.widget.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.mvp.presenters.BuyPopOnLandBatchPresenter;
import com.yn.reader.mvp.views.BuyPopOnLandBatchView;
import com.yn.reader.model.chapter.BookContentGroup;
import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.Constant;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.BuyActivity;
import com.yn.reader.view.BuyOnLandMoreActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BuyPopOnLandBatch extends PopupWindow implements BuyPopOnLandBatchView {
    private BuyPopOnLandBatchPresenter mBuyPopOnLandBatchPresenter;
    private Activity mContext;
    private View view;
    private static BuyPopOnLandBatch mInstance;
    private Book mBook;
    private ChaptersPrice mChaptersPrice;

    private static List<TextView> options = new ArrayList<>();
    private static int[] option_ids = new int[]{
            R.id.tv_nest_ten,
            R.id.tv_nest_fifty,
            R.id.tv_nest_hundred
    };

    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.iv_tip)
    ImageView iv_tip;

    @BindView(R.id.tv_pay_fact)
    TextView tv_pay_fact;

    @BindView(R.id.tv_pay_original)
    TextView tv_pay_original;
    private BookContentGroup mBookContentGroup;
    private long mChapterIdBought;
    private BuyPopOnLand.OnPaySuccess mOnPaySuccess;

    public BuyPopOnLandBatch(Activity context) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContext = context;

        view = LayoutInflater.from(mContext).inflate(AppPreference.getInstance().isNightModel()?R.layout.view_pop_buy_on_land_batch_night:R.layout.view_pop_buy_on_land_batch, null);
        this.setContentView(view);
        ButterKnife.bind(this, getContentView());
        for (int i = 0; i < option_ids.length; i++) {
            int id = option_ids[i];
            TextView tv = getContentView().findViewById(id);
            if (i == 0) tv.setSelected(true);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) return;
                    for (TextView tv : options) {
                        tv.setSelected(false);
                    }
                    v.setSelected(true);
                    mBuyPopOnLandBatchPresenter.getPrice(mBook, mChapterIdBought, getChapterCount());
                }
            });
            options.add(tv);
        }

        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setAnimationStyle(R.style.anim_pop_windowlight);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                for (int i = 0; i < options.size(); i++) {
                    TextView tv = options.get(i);
                    if (i == 0) tv.setSelected(true);
                    else
                        tv.setSelected(false);
                }
                tv_balance.setText("");
                iv_tip.setSelected(false);
            }
        });
        mBuyPopOnLandBatchPresenter = new BuyPopOnLandBatchPresenter(this);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        mBuyPopOnLandBatchPresenter.getPrice(mBook, mChapterIdBought, getChapterCount());
    }

    @OnClick(R.id.tv_more)
    public void buyMore() {
        dismiss();
        IntentUtils.startActivity(mContext, BuyOnLandMoreActivity.class, mBook.getBookid(),mBookContentGroup.getBalance());
    }

    @OnClick(R.id.btn_recharge_buy)
    public void rechargeAndBuy() {
        /**
         * step1:获取购买章节的价格
         * step2:根据价格判断余额是否充足
         * step3:充值
         */
        if (mChaptersPrice == null
                || StringUtil.isEmpty(tv_pay_fact.getText().toString())
                || StringUtil.isEmpty(tv_pay_original.getText().toString())) {
            mBuyPopOnLandBatchPresenter.getPrice(mBook, mChapterIdBought, getChapterCount());
        } else if (mChaptersPrice.getRealchapterprice() > mBookContentGroup.getBalance()) {
            //TODO:？回来继续支付还是消失
            dismiss();
            IntentUtils.startActivity(mContext, BuyActivity.class, Constant.BUY_TYPE_READ_POINT);
        } else
            mBuyPopOnLandBatchPresenter.payForChapters(mBook, mChapterIdBought, getChapterCount());
    }

    private int getChapterCount() {
        int i = 0;
        for (TextView tv : options) {
            if (tv.isSelected()) {
                i = Integer.parseInt(tv.getText().toString().substring(1, tv.getText().toString().length() - 1));
                break;
            }
        }
        return i;
    }

    public void setData(BookContentGroup bookContentGroup) {
        mBookContentGroup = bookContentGroup;
        tv_balance.setText(bookContentGroup.getBalance() + "点");
        iv_tip.setSelected(bookContentGroup.getChapterautobuy() == 1);
    }

    @Override
    public Activity getContext() {
        return mContext;
    }

    @Override
    public void onLoadChaptersPrice(ChaptersPrice chaptersPrice) {
        if (chaptersPrice == null) return;
        mChaptersPrice = chaptersPrice;
        tv_pay_fact.setText(chaptersPrice.getRealchapterprice() + "");
        tv_pay_original.setText(chaptersPrice.getAllchapterprice() + "");
    }

    @Override
    public void paySuccess(PayResult payResult) {
        //购买成功
        dismiss();
        ToastUtil.showShort(mContext, "购买成功");
        if (mOnPaySuccess != null) mOnPaySuccess.success();
    }

    public void setBook(Book book) {
        mBook = book;
    }

    public void setChapterIdBought(long chapterIdBought) {
        mChapterIdBought = chapterIdBought;
    }

    public void setOnPaySuccess(BuyPopOnLand.OnPaySuccess onPaySuccess) {
        mOnPaySuccess = onPaySuccess;
    }
}
