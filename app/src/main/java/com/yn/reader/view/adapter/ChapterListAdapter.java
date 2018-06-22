package com.yn.reader.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.base.BaseCustomAdapter1;
import com.yn.reader.base.BaseHolder;
import com.yn.reader.model.chapter.ChapterListBean;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookDBManager;
import com.yn.reader.widget.ChapterListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/5/28.
 */

public class ChapterListAdapter extends BaseCustomAdapter1 {
    private ChapterListBean currentChapterListBean;
    private Book mBook;
    private boolean isSortUp = false;
    private boolean isNight = true;

    private ChapterListView.OnItemClickListener mOnItemClickListener;

    public boolean isSortUp() {
        return isSortUp;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public ChapterListAdapter(Context context, List objects, Book book) {
        super(context, objects);
        mBook = book;
    }

    @Override
    protected BaseHolder setHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    protected Integer setLayoutId() {
        return R.layout.view_adapter_chapterlist;
    }

    public void sortUp() {
        sortUp(!isSortUp);
    }

    public void sortUp(final boolean sortUp) {
        List tempChapterListBeans = new ArrayList<>();
        isSortUp = sortUp;
        if (isSortUp) {
            for (int i = mObjects.size() - 1; i > -1; i--) {
                tempChapterListBeans.add(mObjects.get(i));
            }
        } else tempChapterListBeans.addAll(mObjects);

        mObjects.clear();
        mObjects.addAll(tempChapterListBeans);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ChapterListView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ItemViewHolder extends BaseHolder {
        private View itemView;
        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_lock)
        ImageView iv_lock;


        @BindView(R.id.v_line)
        View vLine;

        @BindView(R.id.mask_view)
        View mask_view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void assign(Object item) {
            final ChapterListBean bean = (ChapterListBean) item;

            currentChapterListBean = mBook.getCurrentChapter();

            itemView.setBackgroundColor(itemView.getContext().getResources().getColor(isNight ? R.color.read_bottom_bar__night_bg_color : R.color.white));

            tvName.setText(bean.getChaptername());
            tvName.setSelected(false);
            if (!isSortUp) {
                if (mObjects.indexOf(bean) < mObjects.indexOf(currentChapterListBean)) {
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_read_night : R.color.font_size_color));
                } else {
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_to_read_night : R.color.common_black));
                }

            } else {
                if (mObjects.indexOf(bean) > mObjects.indexOf(currentChapterListBean)) {
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_read_night : R.color.font_size_color));
                } else
                    tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.chapter_name_color_to_read_night : R.color.common_black));
            }

            if (mObjects.indexOf(bean) == mObjects.indexOf(currentChapterListBean)) {
                tvName.setTextColor(itemView.getContext().getResources().getColor(isNight ? R.color.colorAccentNight : R.color.colorAccent));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean == null) return;

                    if (mOnItemClickListener != null)
                        mOnItemClickListener.itemClick(bean, mObjects.indexOf(bean));
                }
            });

            if (bean.getChaptershoptype() == 1)
                iv_lock.setImageResource(!isNight ? R.mipmap.ic_tag_lock_open_light : R.mipmap.ic_tag_lock_open_dark);
            else
                iv_lock.setImageResource(!isNight ? R.mipmap.ic_tag_lock_close_light : R.mipmap.ic_tag_lock_close_dark);
        }
    }
}
