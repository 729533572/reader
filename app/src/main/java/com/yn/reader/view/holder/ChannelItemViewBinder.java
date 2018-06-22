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

package com.yn.reader.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.base.BaseHolder;
import com.yn.reader.mvp.presenters.HomePresenter;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.home.Channel;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.SharedWebActivity;
import com.yn.reader.widget.EightPicLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;


/**
 * @author drakeet
 */
public class ChannelItemViewBinder extends ItemViewBinder<Channel, ChannelItemViewBinder.ViewHolder> {
    private Context mContext;
    private HomePresenter homePresenter;

    public ChannelItemViewBinder(Context context, HomePresenter homePresenter) {
        this.mContext = context;
        this.homePresenter = homePresenter;
    }

    @Override
    protected @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_channel, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Channel channel) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.assign(channel);
            }
        }, 30);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_advertisement)
        ImageView iv_advertisement;

        @BindView(R.id.fl_title)
        FrameLayout fl_title;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_sub_title)
        TextView tv_sub_title;

        @BindView(R.id.eightPicLayout)
        EightPicLayout mEightPicLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

        public void assign(final Channel channel) {
            tv_title.setText(channel.getHome_channel_name());
            tv_sub_title.setText(channel.getHome_channel_type_text());

            if (channel.getHome_channel_type() == Constant.CHANNEL_TYPE_CHANGE_BATCH) {
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_tag_change_another);
                /**这一步必须要做,否则不会显示.*/
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩

                /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
                tv_sub_title.setCompoundDrawables(null, null, drawable, null);
            } else {
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_more);
                /**这一步必须要做,否则不会显示.*/
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩

                /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
                tv_sub_title.setCompoundDrawables(null, null, drawable, null);
            }
            fl_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (channel.getHome_channel_type() == Constant.CHANNEL_TYPE_CHANGE_BATCH) {
                        channel.setCurrentIndex(channel.getCurrentIndex() + 1);
                        homePresenter.changeBatch(channel.getHome_channel_id(), 8, channel.getCurrentIndex());
                    } else {
                        IntentUtils.startBookListActivity(mContext, Constant.FRAGMENT_BOOK_LIST, channel.getHome_channel_name(), channel.getHome_channel_id(), channel.getType());
                    }
                }
            });

            for (int i = 0; i < mEightPicLayout.getChildCount(); i++) {
                if (i < channel.getBooks().size()) {
                    Book book = channel.getBooks().get(i);
                    ChildViewHolder childViewHolder = new ChildViewHolder(mEightPicLayout.getChildAt(i));
                    childViewHolder.assign(book);
                }
            }
            if (channel.getAd() == null || !channel.getAd().isIsshow())
                iv_advertisement.setVisibility(View.GONE);
            else {
                GlideUtils.load(mContext, channel.getAd().getImage(), iv_advertisement);
                iv_advertisement.setVisibility(View.VISIBLE);
                iv_advertisement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO:待完善
                        IntentUtils.startActivity(mContext, SharedWebActivity.class, channel.getAd().getLink_content(), channel.getAd().getName());
                    }
                });
            }
        }

        public class ChildViewHolder extends BaseHolder {
            private View mView;
            @BindView(R.id.iv_cover)
            ImageView iv_cover;

            @BindView(R.id.tv_title)
            TextView tv_title;

            @BindView(R.id.tv_sub_title)
            TextView tv_sub_title;

            @BindView(R.id.iv_update)
            ImageView iv_update;

            public ChildViewHolder(View view) {
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
                        IntentUtils.startBookDetailActivity(mContext, book.getBookid());
                    }
                });
            }
        }
    }
}
