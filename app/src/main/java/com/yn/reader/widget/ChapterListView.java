package com.yn.reader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.DividerItemDecoration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.view.adapter.ChapterListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChapterListView extends FrameLayout {
    private TextView tv_list_count;
    //    private RecyclerView rvList;
    private ListView lv_list;

    private LinearLayout llContent;
    private View view_right;
    private View back_layout;
    private TextView title;
    private TextView tv_sort;

    private ChapterListAdapter mChapterListAdapter;
    private List<ChapterListBean> mChapterListBeans;

    private Animation animIn;
    private Animation animOut;

    public ChapterListView(@NonNull Context context) {
        this(context, null);
    }

    public ChapterListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChapterListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChapterListView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.view_chapterlist, this, true);
        initData();
        initView();
    }

    private void initData() {
        animIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_pop_chapterlist_in);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_pop_chapterlist_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llContent.setVisibility(GONE);
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void show() {
        try {
            mChapterListAdapter.notifyDataSetChanged();
            if (getVisibility() != VISIBLE) {
                setVisibility(VISIBLE);
                llContent.setVisibility(VISIBLE);
                llContent.startAnimation(animIn);
            }
        } catch (Exception ex) {
            LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }
    }

    public void show(boolean isNight) {
        mChapterListAdapter.setNight(isNight);
        lv_list.setDivider(getResources().getDrawable(isNight ? R.drawable.divider_line_night : R.drawable.divider_line));

        Drawable drawable_list_count = getResources().getDrawable(isNight ? R.mipmap.ic_line_vertical : R.mipmap.ic_line_vertical);
        /**这一步必须要做,否则不会显示.*/
        drawable_list_count.setBounds(0, 0, drawable_list_count.getMinimumWidth(), drawable_list_count.getMinimumHeight());//对图片进行压缩
        /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
        tv_list_count.setCompoundDrawables(drawable_list_count, null, null, null);
        findViewById(R.id.ll_title_content).setBackgroundColor(getResources().getColor(isNight ? R.color.read_bottom_bar__night_bg_color : R.color.white));

        Drawable drawable_sort = getResources().getDrawable(isNight ? R.drawable.selector_tv_sort_night : R.drawable.selector_tv_sort);
        /**这一步必须要做,否则不会显示.*/
        drawable_sort.setBounds(0, 0, drawable_sort.getMinimumWidth(), drawable_sort.getMinimumHeight());//对图片进行压缩
        /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
        tv_sort.setCompoundDrawables(drawable_sort, null, null, null);

        tv_sort.setTextColor(getContext().getResources().getColor(isNight ? R.color.chapter_name_color_read_night : R.color.sub_title_color));

        findViewById(R.id.view_title_divider).setBackgroundColor(getContext().getResources().getColor(isNight ? R.color.divider_color_night : R.color.divider_color));

        title.setTextColor(getContext().getResources().getColor(isNight ? R.color.font_size_color : R.color.white));

        findViewById(R.id.base_toolbar).setBackgroundColor(getContext().getResources().getColor(isNight ? R.color.colorToolbarNight : R.color.colorPrimary));

        ((ImageView) findViewById(R.id.toolbar_back)).setImageResource(isNight ? R.drawable.ic_back_night : R.drawable.ic_back);

        show();
    }

    public void setFullScreen() {
        view_right.setVisibility(GONE);
    }

    public interface OnItemClickListener {
        void itemClick(Object item, int index);
    }

    private OnItemClickListener itemClickListener;
    private Book mBook;

    private void initView() {
        back_layout = findViewById(R.id.back_layout);
        title = findViewById(R.id.tv_title);
//        llBg = findViewById(R.id.ll_bg);
        llContent = findViewById(R.id.ll_content);
        view_right = findViewById(R.id.view_right);
        tv_list_count = findViewById(R.id.tv_list_count);
        lv_list = findViewById(R.id.lv_list);
        tv_sort = findViewById(R.id.tv_sort);

        setBackgroundColor(getResources().getColor(R.color.clear));
        view_right.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dimissChapterList();
                return true;
            }
        });
        tv_sort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChapterListAdapter.sortUp();
                tv_sort.setSelected(mChapterListAdapter.isSortUp());
                tv_sort.setText(tv_sort.isSelected() ? "升序" : "降序");
            }
        });
    }

    public void setData(Book bookShelfBean, OnItemClickListener clickListener, final OnClickListener onClickListener) {
        try {
            this.itemClickListener = clickListener;
            this.mBook = bookShelfBean;
            title.setText(bookShelfBean.getBookname());
            tv_list_count.setText("共" + BookDBManager.getInstance().getChapterSize(bookShelfBean.getBookid()) + "章");

            mChapterListBeans = new ArrayList<>();
            mChapterListAdapter = new ChapterListAdapter(getContext(), mChapterListBeans, mBook);
            mChapterListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void itemClick(Object item, int index) {
                    if (itemClickListener != null) {
                        itemClickListener.itemClick(item, index);
                    }
                }
            });
            lv_list.setAdapter(mChapterListAdapter);
            tv_sort.setText(tv_sort.isSelected() ? "升序" : "降序");

            back_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    } else {
                        dimissChapterList();
                    }
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<ChapterListBean> chapterListBeans = BookDBManager.getInstance().getChapterList(mBook.getBookid());
                    mChapterListBeans.clear();
                    mChapterListBeans.addAll(chapterListBeans);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChapterListAdapter.notifyDataSetChanged();
                        }
                    }, 400);
                }
            }).start();
        } catch (Exception ex) {
            LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }
    }

    public Boolean dimissChapterList() {
        if (getVisibility() != VISIBLE) {
            return false;
        } else {
            animOut.cancel();
            animIn.cancel();
            llContent.startAnimation(animOut);
            return true;
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        llContent.setBackgroundResource(resid);
    }
}