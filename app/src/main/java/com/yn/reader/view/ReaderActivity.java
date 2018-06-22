package com.yn.reader.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwangjr.rxbus.RxBus;
import com.hysoso.www.utillibrary.LogUtil;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.yn.reader.MiniApp;
import com.yn.reader.R;
import com.yn.reader.base.BaseMvpActivity;
import com.yn.reader.mvp.presenters.BasePresenter;
import com.yn.reader.mvp.presenters.ReaderPresenter;
import com.yn.reader.mvp.views.IBookReadView;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.model.dao.DownloadChapterBean;
import com.yn.reader.model.dao.DownloadChapterListBean;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.Constant;
import com.yn.reader.util.RxBusTag;
import com.yn.reader.view.controller.reader.BottomMenuController;
import com.yn.reader.widget.BookContentView;
import com.yn.reader.widget.ChapterListView;
import com.yn.reader.widget.ContentSwitchView;
import com.yn.reader.widget.modialog.MoProgressHUD;
import com.yn.reader.widget.popupwindow.CheckAddShelfPop;
import com.yn.reader.widget.popupwindow.FontPop;
import com.yn.reader.widget.popupwindow.ReadBookMenuMorePop;
import com.yn.reader.widget.popupwindow.WindowLightPop;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

public class ReaderActivity extends BaseMvpActivity implements IBookReadView {

    private FrameLayout flContent;
    private ContentSwitchView csvBook;
    //主菜单
    private FrameLayout flMenu;
    private View vMenuBg;
    private LinearLayout llMenuTop;
    private ImageButton ivReturn;
    private ImageView ivMenuMore;

    private AutofitTextView atvTitle;
    private BottomMenuController mBottomMenuController;//底部书导航
    //主菜单动画
    private Animation menuTopIn;
    private Animation menuTopOut;

    private CheckAddShelfPop checkAddShelfPop;
    private ChapterListView chapterListView;
    private WindowLightPop windowLightPop;
    private ReadBookMenuMorePop readBookMenuMorePop;
    private FontPop fontPop;

    private MoProgressHUD moProgressHUD;
    private ReaderPresenter mPresenter;
    private IntentFilter intentFilter;
    private Receiver receiver;
    private int battery = 40;

    private long mBookId;
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        bindView();

        mBookId = getIntent().getLongExtra(Constant.KEY_ID, -1);
        mBook = BookDBManager.getInstance().getBook(mBookId);

        mPresenter = new ReaderPresenter(this, mBook);
        initData();
        bindEvent();
        setStatusBarColor2Black();
        batteryReceiver();
    }

    private void batteryReceiver() {
        receiver = new Receiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, intentFilter);
    }

    protected void initData() {
        mPresenter.saveProgress();
        menuTopIn = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_top_in);
        menuTopIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vMenuBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llMenuTop.startAnimation(menuTopOut);
                        mBottomMenuController.hide();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        menuTopOut = AnimationUtils.loadAnimation(this, R.anim.anim_readbook_top_out);
        menuTopOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                vMenuBg.setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flMenu.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected void bindView() {

        moProgressHUD = new MoProgressHUD(this);
        flContent = findViewById(R.id.fl_content);
        csvBook = findViewById(R.id.csv_book);
        initCsvBook();

        flMenu = findViewById(R.id.fl_menu);
        vMenuBg = findViewById(R.id.v_menu_bg);
        llMenuTop = findViewById(R.id.ll_menu_top);
        ivReturn = findViewById(R.id.ib_return);
        ivMenuMore = findViewById(R.id.iv_more);

        atvTitle = findViewById(R.id.atv_title);

        chapterListView = findViewById(R.id.clp_chapterlist);
        toggleNightModel();
    }

    private void toggleNightModel() {
        if (AppPreference.getInstance().isNightModel()) {
            llMenuTop.setBackgroundResource(R.color.read_bottom_bar__night_bg_color);
            chapterListView.setBackgroundResource(R.color.read_bottom_bar__night_bg_color);
        } else {
            llMenuTop.setBackgroundResource(R.color.read_bottom_bar_bg_color);
            chapterListView.setBackgroundResource(R.color.read_bottom_bar_bg_color);
        }
    }

    private void initCsvBook() {
        csvBook.bookReadInit(new ContentSwitchView.OnBookReadInitListener() {
            @Override
            public void success() {
                initPop();
                csvBook.startLoading();
            }
        });
    }

    public void initPop() {
        chapterListView.setData(mBook, new ChapterListView.OnItemClickListener() {
            @Override
            public void itemClick(Object item, int index) {
                chapterListView.dimissChapterList();
                csvBook.setInitData(index, BookDBManager.getInstance().getChapterSize(mBookId), BookContentView.DURPAGEINDEXBEGIN);
            }
        }, null);

        windowLightPop = new WindowLightPop(this);
        windowLightPop.initLight();

        fontPop = new FontPop(this, new FontPop.OnChangeProListener() {
            @Override
            public void textChange(int index) {
                csvBook.changeTextSize();
            }

        });

        readBookMenuMorePop = new ReadBookMenuMorePop(this);
        readBookMenuMorePop.setOnClickDownload(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readBookMenuMorePop.dismiss();
                if (flMenu.getVisibility() == View.VISIBLE) {
                    llMenuTop.startAnimation(menuTopOut);
                    mBottomMenuController.hide();
                }
                //弹出离线下载界面
                int endIndex = mBook.getChapterlist().indexOf(mBook.getCurrentChapter()) + 50;
                if (endIndex >= mBook.getChapterlist().size()) {
                    endIndex = mBook.getChapterlist().size() - 1;
                }
                moProgressHUD.showDownloadList(
                        mBook.getChapterlist().indexOf(
                                mBook.getCurrentChapter()),
                        endIndex,
                        mBook.getChapterlist().size(), new MoProgressHUD.OnClickDownload() {
                            @Override
                            public void download(final int start, final int end) {
                                moProgressHUD.dismiss();
                                mPresenter.addToShelf(new ReaderPresenter.OnAddListner() {
                                    @Override
                                    public void addSuccess() {
                                        List<DownloadChapterBean> result = new ArrayList<DownloadChapterBean>();
                                        for (int i = start; i <= end; i++) {
                                            DownloadChapterBean item = new DownloadChapterBean();
                                            item.setNoteUrl(mBook.getNoteUrl());
                                            item.setDurChapterIndex(mBook.getChapterlist().get(i).getChapterrank());
                                            item.setDurChapterName(mBook.getChapterlist().get(i).getChaptername());
                                            item.setDurChapterUrl(String.valueOf(mBook.getChapterlist().get(i).getChapterid()));
                                            item.setTag(mBook.getTag());
                                            item.setBookName(mBook.getBookname());
                                            item.setCoverUrl(mBook.getBookimage());
                                            result.add(item);
                                        }
                                        RxBus.get().post(RxBusTag.ADD_DOWNLOAD_TASK, new DownloadChapterListBean(result));
                                    }
                                });

                            }
                        });
            }
        });
    }

    protected void bindEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivMenuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readBookMenuMorePop.showAsDropDown(ivMenuMore, 0, DensityUtil.dp2px(ReaderActivity.this, -3.5f));
            }
        });

        csvBook.setLoadDataListener(new ContentSwitchView.LoadDataListener() {
            @Override
            public void loadData(BookContentView bookContentView, long qtag, int chapterIndex, int pageIndex) {
                mPresenter.loadContentData(bookContentView, qtag, chapterIndex, pageIndex);
            }

            @Override
            public void updateProgress(int chapterIndex, int pageIndex, int totalPage) {

                try {
                    mBook.setChapterid(mBook.getChapterlist().get(chapterIndex).getChapterid());
                    mBook.setDurChapterPage(pageIndex, totalPage);

                    if (mBook.getChapterlist().size() > 0) {
                        atvTitle.setText(mBook.getChapterlist().get(
                                chapterIndex < 0 ? 0 :
                                        (chapterIndex > mBook.getChapterlist().size() ? mBook.getChapterlist().size() - 1 :
                                                chapterIndex)).getChaptername());

                    } else {
                        atvTitle.setText(MiniApp.getInstance().getResources().getString(R.string.no_chapter));
                    }

                    mBottomMenuController.updateChapterProgress(chapterIndex);
                } catch (Exception ex) {
                    LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
                }
            }

            @Override
            public String getChapterTitle(int chapterIndex) {

                if (mBook.getChapterlist().size() == 0) {
                    return MiniApp.getInstance().getString(R.string.no_chapter);
                } else if (chapterIndex >= 0 || chapterIndex < mBook.getChapterlist().size()) {
                    return mBook.getChapterName(chapterIndex);
                } else return "";
            }

            @Override
            public void initData(int lineCount) {
                mPresenter.setPageLineCount(lineCount);
                mPresenter.initContent();
            }

            @Override
            public void showMenu() {
                flMenu.setVisibility(View.VISIBLE);
                llMenuTop.startAnimation(menuTopIn);
                int durChapterIndex = csvBook.getDurContentView().getDurChapterIndex();
                LogUtil.e(getClass().getSimpleName(), "durChapterIndex:" + durChapterIndex);
                mBottomMenuController.show(csvBook.getDurContentView().getDurChapterIndex());
            }
        });
        mBottomMenuController = new BottomMenuController(findViewById(R.id.ll_menu_bottom), mBook);
        mBottomMenuController.setOnBottomMenuCallBack(new BottomMenuController.OnBottomMenuCallBack() {
            @Override
            public void selectShownChapter(int chapterIndex) {
                csvBook.setInitData(chapterIndex, mBook.getChapterlist().size(), BookContentView.DURPAGEINDEXBEGIN);
            }

            @Override
            public void showCatalog() {
                LogUtil.e(getClass().getSimpleName(), "弹出目录列表之前");
                llMenuTop.startAnimation(menuTopOut);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e(getClass().getSimpleName(), "弹出目录列表");
                        chapterListView.show(AppPreference.getInstance().isNightModel());
                    }
                }, menuTopOut.getDuration());
            }

            @Override
            public void lightOrDark() {
                llMenuTop.startAnimation(menuTopOut);
                csvBook.changeBg();
                toggleNightModel();
            }

            @Override
            public void setFont() {
                llMenuTop.startAnimation(menuTopOut);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fontPop.showAtLocation(flContent, Gravity.BOTTOM, 0, 0);
                    }
                }, menuTopOut.getDuration());
            }
        });
        mBottomMenuController.hide();
    }

    @Override
    public Paint getPaint() {
        return csvBook.getTextPaint();
    }

    @Override
    public int getContentWidth() {
        return csvBook.getContentWidth();
    }

    @Override
    public void initContentSuccess() {
        int chapterIndex = mBook.getDurChapterIndex();
        LogUtil.i(getClass().getSimpleName(), "initContentSuccess chapterIndex:" + chapterIndex);
        csvBook.setInitData(chapterIndex, mBook.getChapterlist().size(), mBook.getDurChapterPage());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.saveProgress();
    }

    @Override
    public void onBackPressed() {
        if (!BookDBManager.getInstance().isCollection(mBook.getBookid())) {
            if (checkAddShelfPop == null)
                checkAddShelfPop = new CheckAddShelfPop(this, mBook.getBookname(), new CheckAddShelfPop.OnItemClickListener() {
                    @Override
                    public void clickExit() {
                        finish();
                    }

                    @Override
                    public void clickAddShelf() {
                        BookDBManager.getInstance().collect(mBook, false);
                        checkAddShelfPop.dismiss();
                        finish();
                    }
                });
            checkAddShelfPop.showAtLocation(flContent, Gravity.CENTER, 0, 0);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void showLoadBook() {
        moProgressHUD.showLoading(getString(R.string.loading));
    }

    @Override
    public void dimissLoadBook() {
        moProgressHUD.dismiss();
    }

    @Override
    public void loadLocationBookError() {
        csvBook.loadError();
    }

    @Override
    public void showDownloadMenu() {
        ivMenuMore.setVisibility(View.VISIBLE);
    }


    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public Activity getContext() {
        return this;
    }


    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int level = intent.getIntExtra("level", 0);
                setBattery(100 - level);
            }
        }
    }


    public void setBattery(int battery) {
        this.battery = battery;
        BookContentView.setBattery(this.battery);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        csvBook.releaseBookContentBroadCastReceivers();
    }
}
