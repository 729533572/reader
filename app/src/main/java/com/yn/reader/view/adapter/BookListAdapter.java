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
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.booklist.Navigation;
import com.yn.reader.model.booklist.NavigationCategory;
import com.yn.reader.model.common.Book;
import com.yn.reader.snap.Snap;
import com.yn.reader.snap.SnapAdapter;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.widget.tag.OnTagClickListener;
import com.yn.reader.widget.tag.Tag;
import com.yn.reader.widget.tag.TagView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 收藏-历史
 */
public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public void setOnCategoryChanged(OnCategoryChanged onCategoryChanged) {
        mOnCategoryChanged = onCategoryChanged;
    }

    public BookListAdapter setShowAdvertisement(boolean showAdvertisement) {
        isShowAdvertisement = showAdvertisement;
        return this;
    }

    public interface OnCategoryChanged {
        void categoryChanged(
                String tags,
                Integer chargetype,
                Integer status,
                Integer word,
                Integer lastnevigate
        );
    }

    private static final int TYPE_HEADER = 1000;
    private static final int TYPE_ADVERTISEMENT = 1001;
    private static final int TYPE_NORMAL = 1002;
    private Context mContext;
    private List<Book> books;
    private boolean isShowCategory;
    private CategoryViewHolder categoryViewHolder;
    private OnCategoryChanged mOnCategoryChanged;
    private boolean isShowAdvertisement = false;

    public BookListAdapter(Context context, List<Book> books) {
        this.books = books;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_book_list, parent, false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_ADVERTISEMENT) {
            return new AdvertisementViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_advertisement_in_list, parent, false));
        } else {
            return categoryViewHolder;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final Book book = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            if (book != null) {
                viewHolder.setData(book);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.startBookDetailActivity(mContext, book.getBookid());
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCategory) {
            if (position == 0)
                return TYPE_HEADER;
            else if (position == 3 && isShowAdvertisement)
                return TYPE_ADVERTISEMENT;
        } else if (isShowAdvertisement) {
            if (position == 2)
                return TYPE_ADVERTISEMENT;
        }
        return TYPE_NORMAL;
    }

    private Book getItem(int position) {
        if (isShowCategory) {
            position -= 1;
        }
        if (isShowAdvertisement) {
            if (position > 2) {
                position -= 1;
            }
        }

        if (books != null && position < books.size()) {
            return books.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        int item_count = books == null || books.size() == 0 ? isShowCategory ? 1 : 0 : (isShowCategory ? books.size() + 1 : books.size());
        if (isShowAdvertisement) item_count += 1;
        return item_count;
    }

    public void setCategory(NavigationCategory navigationCategory) {
        this.isShowCategory = true;

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_book_category, null);
        categoryViewHolder = new CategoryViewHolder(view);

        categoryViewHolder.setupAdapter(navigationCategory);

        notifyDataSetChanged();
    }

    public class AdvertisementViewHolder extends RecyclerView.ViewHolder {

        public AdvertisementViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_icon)
        ImageView cover;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_sub_title)
        TextView subTitle;
        @BindView(R.id.tv_tags)
        TagView tv_tags;
        @BindView(R.id.book_des)
        TextView description;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(@NonNull final Book book) {
            GlideUtils.load(mContext, book.getBookimage(), cover, R.mipmap.ic_hold_place_book_cover);
            title.setText(book.getBookname());
            subTitle.setText(book.getBookauthor());
            description.setText(book.getBookdesc());

            tv_tags.removeAllTags();
            if (book.getBooktags() != null && book.getBooktags().size() > 0) {
                for (String bean : book.getBooktags()) {
                    Tag tag = new Tag(bean);

                    String tagColor = Constant.BOOK_TAG_COLOR[book.getBooktags().indexOf(bean) % Constant.BOOK_TAG_COLOR.length];
                    tag.tagTextColor = Color.parseColor(tagColor);

                    tag.layoutColor = Color.parseColor("#ffffff");
                    tag.layoutColorPress = Color.parseColor("#ffffff");

                    tag.radius = 24f;
                    tag.tagTextSize = 9f;
                    tag.layoutBorderSize = 1f;
                    tag.layoutBorderColor = Color.parseColor(tagColor);
                    tag.layoutBorderColorPress = Color.parseColor(tagColor);

                    tv_tags.addTag(tag);
                }
            }
            tv_tags.setOnTagClickListener(new OnTagClickListener() {
                @Override
                public void onTagClick(int position, Tag tag) {
                    IntentUtils.startBookDetailActivity(mContext, book.getBookid());
                }
            });
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private NavigationCategory mNavigationCategory;
        private @NonNull
        RecyclerView categoryRecy;


        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryRecy = (RecyclerView) itemView.findViewById(R.id.category_rec);
            categoryRecy.setLayoutManager(new LinearLayoutManager(mContext));
            categoryRecy.setHasFixedSize(true);
        }


        private void setupAdapter(NavigationCategory navigationCategory) {
            if (mNavigationCategory != null) return;
            mNavigationCategory = navigationCategory;

            final SnapAdapter snapAdapter = new SnapAdapter();

            snapAdapter.addSnap(new Snap(Gravity.END, navigationCategory.getNavigationTags(), 0));
            snapAdapter.addSnap(new Snap(Gravity.END, navigationCategory.getChargetype(), 1));
            snapAdapter.addSnap(new Snap(Gravity.END, navigationCategory.getStatus(), 2));
            snapAdapter.addSnap(new Snap(Gravity.END, navigationCategory.getWord(), 3));
            snapAdapter.addSnap(new Snap(Gravity.END, navigationCategory.getLastnevigate(), 4));

            snapAdapter.setOnSnapNavigationSelectionChanged(new SnapAdapter.OnSnapNavigationSelectionChanged() {
                @Override
                public void selected(Snap snap, Navigation beforeSelection, Navigation afterSelection) {

                    String tags = snapAdapter.getSnapByTag(0).getApps().indexOf(snapAdapter.getSelectionNavigationBySnapTag(0)) != 0 ? snapAdapter.getSelectionNavigationBySnapTag(0).getName() : null;

                    Integer chargetype = snapAdapter.getSelectionNavigationBySnapTag(1).getId();
                    Integer status = snapAdapter.getSelectionNavigationBySnapTag(2).getId();
                    Integer word = snapAdapter.getSelectionNavigationBySnapTag(3).getId();
                    Integer lastnevigate = snapAdapter.getSelectionNavigationBySnapTag(4).getId();

                    if (mOnCategoryChanged != null)
                        mOnCategoryChanged.categoryChanged(tags, chargetype, status, word, lastnevigate);
                }
            });
            categoryRecy.setAdapter(snapAdapter);
        }


    }
}
