package com.yn.reader.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.BroadCastUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.BuyOnLandMorePresenter;
import com.yn.reader.mvp.views.BuyOnLandMoreView;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.util.Constant;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.adapter.ChapterSelectableAdapter;
import com.yn.reader.widget.BookContentView;
import com.yn.reader.widget.FullyLinearLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyOnLandMoreActivity extends BaseMvpActivity implements BuyOnLandMoreView {
    private BuyOnLandMorePresenter mBuyOnLandMorePresenter;
    private Book mBook;
    private int mBalance;
    private ChapterSelectableAdapter mChapterSelectableAdapter;

    @BindView(R.id.save)
    TextView mToolbarSave;

    @BindView(R.id.rv_chapter)
    RecyclerView rv_chapter;

    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.iv_tip)
    ImageView iv_tip;

    @BindView(R.id.tv_pay_fact)
    TextView tv_pay_fact;

    @BindView(R.id.tv_pay_original)
    TextView tv_pay_original;
    private ChaptersPrice mChaptersPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_on_land_more);
        ButterKnife.bind(this);
        setToolbarTitle("更多");
        mToolbarSave.setText("全选");
        mBook = BookDBManager.getInstance().getBook(getIntent().getLongExtra(Constant.KEY_ID, -1));
        mBalance = getIntent().getIntExtra(Constant.KEY_INT, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_balance.setText(mBalance + "");
        final FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        rv_chapter.setHasFixedSize(false);
        rv_chapter.setNestedScrollingEnabled(false);
        rv_chapter.setLayoutManager(layoutManager);
        loadData();
    }

    private void loadData() {
        mBuyOnLandMorePresenter = new BuyOnLandMorePresenter(this);
        mBuyOnLandMorePresenter.getChapters(mBook.getBookid());
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
            mBuyOnLandMorePresenter.getPrice(mBook, getChapterIds());
        } else if (mChaptersPrice.getRealchapterprice() > mBalance) {
            IntentUtils.startActivity(
                    this,
                    BuyActivity.class,
                    Constant.BUY_TYPE_READ_POINT);
        } else mBuyOnLandMorePresenter.payForChapters(mBook, getChapterIds());
    }

    private String getChapterIds() {
        String ids = "";
        for (ChapterListBean bean : mChapterSelectableAdapter.getSelections()) {
            ids += bean.getChapterid() + ",";
        }
        if (ids.endsWith(",")) ids = ids.substring(0, ids.length() - 1);
        return ids;
    }

    @Override
    public BasePresenter getPresenter() {
        return mBuyOnLandMorePresenter;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onLoadChapters(List<ChapterListBean> chapterListBeans) {
        if (chapterListBeans == null || chapterListBeans.size() == 0) return;
        mChapterSelectableAdapter = new ChapterSelectableAdapter(this, mBook.getChapterlist());
        mChapterSelectableAdapter.setOnSelectionsChanged(new ChapterSelectableAdapter.OnSelectionsChanged() {
            @Override
            public void selectionsChange() {
                mBuyOnLandMorePresenter.getPrice(mBook, getChapterIds());
            }
        });
        rv_chapter.setAdapter(mChapterSelectableAdapter);
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
        ToastUtil.showShort(this, "充值成功");
        IntentUtils.popPreviousActivity(this);
        BroadCastUtil.sendLocalBroadcast(this, BookContentView.PaySuccessBroadCastReceiver.class, true);
    }
}
