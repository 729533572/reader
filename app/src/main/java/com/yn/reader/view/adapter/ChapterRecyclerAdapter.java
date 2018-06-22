//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.yn.reader.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.yn.reader.R;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.common.Book;
import com.yn.reader.widget.ChapterListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChapterRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChapterListBean> mChapterListBeans;
    private ChapterListBean currentChapterListBean;
    private boolean isSortUp = false;
    private boolean isNight = true;

    private Book mBook;
    private ChapterListView.OnItemClickListener itemClickListener;

    public boolean isSortUp() {
        return isSortUp;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public ChapterRecyclerAdapter(Book book, List<ChapterListBean> chapterListBeans, @NonNull ChapterListView.OnItemClickListener itemClickListener) {
        this.mBook = book;
        mChapterListBeans = chapterListBeans;
        this.itemClickListener = itemClickListener;
        sortUp(false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.view_adapter_chapterlist, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int postion) {
        try {
            if (holder instanceof ViewHolder) {
                ChapterListBean bean = mChapterListBeans.get(postion);
                ((ViewHolder) holder).assign(bean);
            }
        } catch (Exception ex) {
            LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
        }

    }
    @Override
    public int getItemCount() {
        if (mChapterListBeans == null)
            return 0;
        else
            return mChapterListBeans.size() ;
    }

    public void sortUp(final boolean sortUp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ChapterListBean> tempChapterListBeans = new ArrayList<>();
                isSortUp = sortUp;
                if (isSortUp) {
                    for (int i = mChapterListBeans.size() - 1; i > -1; i--) {
                        tempChapterListBeans.add(mChapterListBeans.get(i));
                    }
                } else tempChapterListBeans.addAll(mChapterListBeans);
                mChapterListBeans.clear();
                mChapterListBeans.addAll(tempChapterListBeans);
                notifyDataSetChanged();
            }
        });
    }

    public void sortUp() {
        sortUp(!isSortUp);
    }

    public class MoveViewHolder extends RecyclerView.ViewHolder {
        public MoveViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_lock)
        ImageView iv_lock;


        @BindView(R.id.v_line)
        View vLine;

        @BindView(R.id.mask_view)
        View mask_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void assign(ChapterListBean bean) {
            currentChapterListBean = mBook.getCurrentChapter();

            itemView.setBackgroundColor(itemView.getContext().getResources().getColor(isNight ? R.color.read_bottom_bar__night_bg_color : R.color.white));

            tvName.setText(bean.getChaptername());
            tvName.setSelected(false);
            if (!isSortUp) {
                if (mChapterListBeans.indexOf(bean) < mChapterListBeans.indexOf(currentChapterListBean)) {
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_read_night : R.color.font_size_color));
//                    mask_view.setVisibility(View.VISIBLE);
                } else {
//                    mask_view.setVisibility(View.GONE);
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_to_read_night : R.color.common_black));
                }

            } else {
                if (mChapterListBeans.indexOf(bean) > mChapterListBeans.indexOf(currentChapterListBean)) {
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_read_night : R.color.font_size_color));
                } else
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_to_read_night : R.color.common_black));
            }

            if (mChapterListBeans.indexOf(bean) == mChapterListBeans.indexOf(currentChapterListBean)) {
                tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.colorAccentNight : R.color.colorAccent));
            }

            itemView.setTag(bean);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChapterListBean chapterListBean = (ChapterListBean) v.getTag();
                    if (chapterListBean == null) return;
                    if (itemClickListener != null)
                        itemClickListener.itemClick(chapterListBean, mChapterListBeans.indexOf(chapterListBean));
                }
            });

            if (bean.getChaptershoptype() == 1)
                iv_lock.setImageResource(!isNight ? R.mipmap.ic_tag_lock_open_light : R.mipmap.ic_tag_lock_open_dark);
            else
                iv_lock.setImageResource(!isNight ? R.mipmap.ic_tag_lock_close_light : R.mipmap.ic_tag_lock_close_dark);
        }
    }
}
