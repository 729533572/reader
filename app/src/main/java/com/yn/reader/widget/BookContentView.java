package com.yn.reader.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.model.chapter.BookContentGroup;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.LogUtils;
import com.yn.reader.util.ReadBookControl;
import com.yn.reader.widget.popupwindow.BuyPopOnLand;
import com.yn.reader.widget.popupwindow.BuyPopOutLand;

import java.text.NumberFormat;
import java.util.List;

public class BookContentView extends FrameLayout {
    public long qTag = System.currentTimeMillis();

    public static final int DURPAGEINDEXBEGIN = -1;
    public static final int DURPAGEINDEXEND = -2;

    private View view;
    private ImageView ivBg;
    private TextView tvTitle;
    private LinearLayout llContent;
    private MTextView tvContent;
    private TextView tvPage;

    private TextView tvLoading;
    private LinearLayout llError;
    private TextView tvErrorInfo;
    private TextView tvLoadAgain;
    private TextView buy_tips;
    private ProgressBar progreebar_battery;

    private String title;
    private String content;
    private int durChapterIndex;  //如果durPageIndex = -1 则是从头开始  -2则是从尾开始
    private int chapterAll;
    private int durPageIndex;
    private int pageAll;

    private ContentSwitchView.LoadDataListener loadDataListener;

    private SetDataListener setDataListener;

    private static int currentBattery = 40;
    private BookContentGroup mBusinessInfo;
    private Book mBook;
    private PaySuccessBroadCastReceiver mPaySuccessBroadCastReceiver;

    public static void setBattery(int battery) {
        currentBattery = battery;
    }

    public void setBusinessInfo(BookContentGroup businessInfo) {
        mBusinessInfo = businessInfo;
    }

    public void setBook(Book book) {
        mBook = book;
    }

    public interface SetDataListener {
        public void setDataFinish(BookContentView bookContentView, int durChapterIndex, int chapterAll, int durPageIndex, int pageAll, int fromPageIndex);
    }

    public BookContentView(Context context) {
        this(context, null);
    }

    public BookContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BookContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_content_switch_item, this, false);
        addView(view);
        progreebar_battery = (ProgressBar) view.findViewById(R.id.progreebar_battery);
        ivBg = (ImageView) view.findViewById(R.id.iv_bg);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        tvContent = (MTextView) view.findViewById(R.id.tv_content);
//        vBottom = view.findViewById(R.id.v_bottom);
        tvPage = (TextView) view.findViewById(R.id.tv_page);

        tvLoading = (TextView) view.findViewById(R.id.tv_loading);
        llError = (LinearLayout) view.findViewById(R.id.ll_error);
        tvErrorInfo = (TextView) view.findViewById(R.id.tv_error_info);
        tvLoadAgain = (TextView) view.findViewById(R.id.tv_load_again);
        buy_tips = view.findViewById(R.id.buy_tips);

        buy_tips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBuyByStatusBack();
            }
        });
        tvLoadAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadDataListener != null)
                    loading();
            }
        });
        mPaySuccessBroadCastReceiver = new PaySuccessBroadCastReceiver();
        IntentFilter filter = new IntentFilter(PaySuccessBroadCastReceiver.class.getSimpleName());
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mPaySuccessBroadCastReceiver, filter);
    }

    private void gotoBuyByStatusBack() {
        Handler handler = new Handler(getContext().getMainLooper());
        Runnable buyPopRunnableOnLand = new Runnable() {
            @Override
            public void run() {
                BuyPopOnLand buyPop = new BuyPopOnLand((Activity) getContext());
                if (buyPop.isShowing()) return;
                buyPop.setData(mBusinessInfo);
                buyPop.setBook(mBook);
                buyPop.setChapterIdBought(mBook.getChapterlist().get(durChapterIndex).getChapterid());
                buyPop.setOnPaySuccess(new BuyPopOnLand.OnPaySuccess() {
                    @Override
                    public void success() {
                        buySuccess();
                    }
                });
                buyPop.showAtLocation(((Activity) getContext()).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            }
        };
        Runnable buyPopRunnableOutLand = new Runnable() {
            @Override
            public void run() {
                BuyPopOutLand buyPop = new BuyPopOutLand((Activity) getContext());
                if (buyPop.isShowing()) return;
                buyPop.setData(mBusinessInfo);
                buyPop.showAtLocation(((Activity) getContext()).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            }
        };
        if ((mBusinessInfo != null && mBusinessInfo.getStatus() == 3)) {
            handler.post(buyPopRunnableOnLand);
        }
        if ((mBusinessInfo != null && mBusinessInfo.getStatus() == 2)) {
            handler.post(buyPopRunnableOutLand);
        }
    }

    public void loading() {
        buy_tips.setVisibility(GONE);
        llError.setVisibility(GONE);
        tvLoading.setVisibility(VISIBLE);
        llContent.setVisibility(INVISIBLE);
        qTag = System.currentTimeMillis();
        //执行请求操作
        if (loadDataListener != null) {
            loadDataListener.loadData(this, qTag, durChapterIndex, durPageIndex);
            LogUtil.e(getClass().getSimpleName() + "  loading", durChapterIndex + "");
        }
    }

    public void finishLoading() {
        llError.setVisibility(GONE);
        llContent.setVisibility(VISIBLE);
        tvLoading.setVisibility(GONE);
        if (mBook != null && mBook.getChapterlist().get(durChapterIndex).getChaptershoptype() == 0)
            buy_tips.setVisibility(VISIBLE);
        else buy_tips.setVisibility(GONE);
    }

    private void buySuccess() {
        llError.setVisibility(GONE);
        buy_tips.setVisibility(GONE);
        llContent.setVisibility(VISIBLE);
        tvLoading.setVisibility(GONE);
        loading();
    }

    public void updateData(long tag, String title, List<String> contentLines, int durChapterIndex, int chapterAll, int durPageIndex, int durPageAll) {
        if (tag == qTag) {
            if (setDataListener != null) {
                setDataListener.setDataFinish(this, durChapterIndex, chapterAll, durPageIndex, durPageAll, this.durPageIndex);
            }
            if (contentLines == null) {
                this.content = "";
            } else {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < contentLines.size(); i++) {
                    s.append(contentLines.get(i));
                }
                this.content = s.toString();
            }
            this.title = title;
            this.durChapterIndex = durChapterIndex;
            this.chapterAll = chapterAll;
            this.durPageIndex = durPageIndex;
            this.pageAll = durPageAll;

            tvTitle.setText(this.title);
            tvContent.setText(this.content);
            LogUtils.log("durPageIndex=" + durPageIndex + " pageAll=" + pageAll);

            updateProgress(durPageIndex);
            updateBattery();

            finishLoading();
        }
    }

    private void updateProgress(float durPageIndex) {
        double currentPage = (((double) durPageIndex + 1)) / pageAll;
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        tvPage.setText(String.format(getResources().getString(R.string.current_chapter_progress), nt.format(currentPage)));

    }

    private void updateBattery() {
        progreebar_battery.setProgress(currentBattery);
    }

    public void loadData(String title, int durChapterIndex, int chapterAll, int durPageIndex) {
        LogUtil.e(getClass().getSimpleName() + " loadBookDetailData", durChapterIndex + "");
        this.title = title;
        this.durChapterIndex = durChapterIndex;
        this.chapterAll = chapterAll;
        this.durPageIndex = durPageIndex;
        tvTitle.setText(title);
        tvPage.setText("");
        loading();
    }

    public ContentSwitchView.LoadDataListener getLoadDataListener() {
        return loadDataListener;
    }

    public void setLoadDataListener(ContentSwitchView.LoadDataListener loadDataListener, SetDataListener setDataListener) {
        this.loadDataListener = loadDataListener;
        this.setDataListener = setDataListener;
    }

    public void setLoadDataListener(ContentSwitchView.LoadDataListener loadDataListener) {
        this.loadDataListener = loadDataListener;
    }

    public void loadError() {
        llError.setVisibility(VISIBLE);
        tvLoading.setVisibility(GONE);
        llContent.setVisibility(INVISIBLE);
        buy_tips.setVisibility(GONE);
    }

    public void shouldBuy() {
        buy_tips.setVisibility(VISIBLE);
        tvLoading.setVisibility(GONE);
        llContent.setVisibility(INVISIBLE);
    }

    public int getPageAll() {
        return pageAll;
    }

    public void setPageAll(int pageAll) {
        this.pageAll = pageAll;
    }

    public int getDurPageIndex() {
        return durPageIndex;
    }

    public void setDurPageIndex(int durPageIndex) {
        this.durPageIndex = durPageIndex;
    }

    public int getDurChapterIndex() {
        return durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public int getChapterAll() {
        return chapterAll;
    }

    public void setChapterAll(int chapterAll) {
        this.chapterAll = chapterAll;
    }

    public SetDataListener getSetDataListener() {
        return setDataListener;
    }

    public void setSetDataListener(SetDataListener setDataListener) {
        this.setDataListener = setDataListener;
    }

    public long getqTag() {
        return qTag;
    }

    public void setqTag(long qTag) {
        this.qTag = qTag;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public int getLineCount(int height) {
        float ascent = tvContent.getPaint().ascent();
        float descent = tvContent.getPaint().descent();
        float textHeight = descent - ascent;
        return (int) ((height * 1.0f - tvContent.getLineSpacingExtra()) / (textHeight + tvContent.getLineSpacingExtra()));
    }

    public void setReadBookControl(ReadBookControl readBookControl) {
        setTextKind(readBookControl);
        setBg(readBookControl);
    }

    public void setBg(ReadBookControl readBookControl) {
        ivBg.setImageResource(readBookControl.getTextBackground());

        tvTitle.setTextColor(readBookControl.getTextColor());
        tvContent.setTextColor(readBookControl.getTextColor());
        tvPage.setTextColor(readBookControl.getTextColor());
//        vBottom.setBackgroundColor(readBookControl.getTextColor());
        tvLoading.setTextColor(readBookControl.getTextColor());
        tvErrorInfo.setTextColor(readBookControl.getTextColor());
    }

    public void setTextKind(ReadBookControl readBookControl) {
        tvContent.setTextSize(readBookControl.getTextSize());
        tvContent.setLineSpacing(readBookControl.getTextExtra(), 1);
    }

    public void releaseBroadCastReceiver() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPaySuccessBroadCastReceiver);
    }

    public class PaySuccessBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle.getBoolean(getClass().getSimpleName())) buySuccess();
        }
    }
}
