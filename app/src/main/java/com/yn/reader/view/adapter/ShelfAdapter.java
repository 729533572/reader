/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yn.reader.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.view.MainActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 收藏-历史
 */
public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.ViewHolder> {
    private Context mContext;
    private List<Book> books;
    private int post_width;
    private boolean isEditModel;
    private OnItemClickListener mOnItemClickListener;

    public ShelfAdapter(Context context, List<Book> books) {
        this.books = books;
        mContext = context;
        post_width = (context.getResources().getDisplayMetrics().widthPixels - ComUtils.dip2px(116)) / 3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shelf, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Book book = getItem(position);
        if (book != null) {
            holder.setData(book);
        }

    }

    private Book getItem(int position) {
        if (books != null && position < books.size()) {
            return books.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (books == null) return 0;
        return books.size();
    }

    public void setEditModel(boolean editModel) {
        isEditModel = editModel;
        for (Book bean : books) {
            bean.setSelect(false);
        }
        notifyDataSetChanged();
    }

    public boolean hasSelection() {
        return getSelections().size() > 0;
    }

    public List<Book> getSelections() {
        List<Book> selections = new ArrayList<>();
        for (Book bean : books) {
            if (bean.isSelect()) selections.add(bean);
        }
        return selections;
    }

    public void deleteSelections() {
        books.removeAll(getSelections());
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        ImageView cover;
        private @NonNull
        TextView title;
        private @NonNull
        TextView subTitle;
        View mask;
        ImageView update;
        ImageView select;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iv_cover);
            title = itemView.findViewById(R.id.tv_title);
            subTitle = itemView.findViewById(R.id.tv_sub_title);
            itemView.getLayoutParams().width = post_width;
            mask = itemView.findViewById(R.id.view_mask);
            update = itemView.findViewById(R.id.iv_update);
            select = itemView.findViewById(R.id.iv_select);
        }

        void setData(@NonNull final Book book) {
            GlideUtils.load(mContext, book.getBookimage(), cover, R.mipmap.ic_hold_place_book_cover);
            title.setText(book.getBookname());
            if (book.getBookprogress() != null) {
                subTitle.setText(String.format(mContext.getString(R.string.alread_read), book.getBookprogress()));
            }
            update.setVisibility(book.getIsupdate() == 1 ? View.VISIBLE : View.GONE);

            if (isEditModel) {
                mask.setVisibility(View.VISIBLE);
                select.setVisibility(View.VISIBLE);
                select.setSelected(book.isSelect());
            } else {
                mask.setVisibility(View.GONE);
                select.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isEditModel) {
                        book.setSelect(!book.isSelect());
                        if (getSelections().size() == getItemCount()) {
                            if (mContext instanceof MainActivity) {
                                MainActivity mainActivity = (MainActivity) mContext;
                                mainActivity.setBottomLeft("取消");
                            }
                        } else {
                            if (mContext instanceof MainActivity) {
                                MainActivity mainActivity = (MainActivity) mContext;
                                mainActivity.setBottomLeft("全选");
                            }
                        }
                        notifyDataSetChanged();
                    } else if (mOnItemClickListener != null) {
                        mOnItemClickListener.clickItem(book);
                    }
                }
            });
        }
    }
}
