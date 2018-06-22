package com.yn.reader.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.base.BaseCustomAdapter1;
import com.yn.reader.base.BaseHolder;
import com.yn.reader.model.common.Book;
import com.yn.reader.util.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/5/21.
 */

public class ShelfAdapter1 extends BaseCustomAdapter1 {
    public ShelfAdapter1(Context context, List objects) {
        super(context, objects);
    }

    @Override
    protected BaseHolder setHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    protected Integer setLayoutId() {
        return R.layout.item_shelf;
    }

    public class ItemViewHolder extends BaseHolder {
        private View mView;
        @BindView(R.id.iv_cover)
        ImageView iv_cover;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_sub_title)
        TextView tv_sub_title;

        @BindView(R.id.iv_update)
        ImageView iv_update;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

        @Override
        public void assign(Object item) {
            final Book book = (Book) item;
            GlideUtils.load(mContext, book.getBookimage(), iv_cover, R.mipmap.ic_hold_place_book_cover);
            tv_title.setText(book.getBookname());
            if (book.getBookprogress() != null) {
                tv_sub_title.setText(String.format(mContext.getString(R.string.alread_read), book.getBookprogress()));
            }
            iv_update.setVisibility(book.getIsupdate() == 1 ? View.VISIBLE : View.GONE);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemElementClickListener != null)
                        mOnItemElementClickListener.itemClick(book);
                }
            });
        }
    }
}
