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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.viewlibrary.MFSStaticGridView;
import com.yn.reader.R;
import com.yn.reader.base.BaseCustomAdapter1;
import com.yn.reader.model.common.Book;
import com.yn.reader.model.common.BookForShowList;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.view.adapter.ShelfAdapter1;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;


/**
 * 首页各种分类合集
 */
public class HomePostsViewBinder extends ItemViewBinder<BookForShowList, HomePostsViewBinder.ViewHolder> {

    private Context mContext;

    public HomePostsViewBinder(Context context) {
        mContext = context;
    }

    @Override
    protected @NonNull
    ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_eight_pic, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BookForShowList bookForShowList) {
        LogUtil.i(getClass().getSimpleName(), bookForShowList.getHome_channel_id() + "==>" + JSON.toJSON(getBookNameList(bookForShowList.books)).toString());
        holder.assign(bookForShowList.books);
    }

    private List<String> getBookNameList(List<Book> books) {
        List<String> bookNames = new ArrayList<>();
        for (Book bean : books) {
            bookNames.add(bean.getBookname());
        }
        return bookNames;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mg_books)
        MFSStaticGridView mg_books;

        ShelfAdapter1 mAdapter;
        List<Book> mBooks;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mBooks = new ArrayList<>();
            mAdapter = new ShelfAdapter1(mContext, mBooks);
            mAdapter.setOnItemElementClickListener(new BaseCustomAdapter1.OnItemElementClickListener() {
                @Override
                public void itemClick(Object item) {
                    Book book = (Book) item;
                    IntentUtils.startBookDetailActivity(mContext, book.getBookid());
                }
            });
            mg_books.setAdapter(mAdapter);
        }

        public void assign(List<Book> books) {
            mBooks.clear();
            mBooks.addAll(books);
            mAdapter.notifyDataSetChanged();
        }
    }
}
