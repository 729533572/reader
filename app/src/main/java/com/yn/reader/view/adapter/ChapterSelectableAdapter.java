package com.yn.reader.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.StringUtil;
import com.yn.reader.R;
import com.yn.reader.model.chapter.ChapterListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/3/23.
 */

public class ChapterSelectableAdapter extends RecyclerView.Adapter<ChapterSelectableAdapter.ViewHolder> {
    public interface OnSelectionsChanged {
        void selectionsChange();
    }

    private List<ChapterListBean> mChapterListBeans;
    private List<ChapterListBean> mSelections;
    private Context mContext;
    private OnSelectionsChanged mOnSelectionsChanged;

    public void setOnSelectionsChanged(OnSelectionsChanged onSelectionsChanged) {
        mOnSelectionsChanged = onSelectionsChanged;
    }

    public ChapterSelectableAdapter(Context context, List<ChapterListBean> chapterListBeans) {
        mContext = context;
        mChapterListBeans = chapterListBeans;
        mSelections = new ArrayList<>();
    }

    @Override
    public ChapterSelectableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chapter_selectable, parent, false));
    }

    public List<ChapterListBean> getSelections() {
        return mSelections;
    }

    @Override
    public void onBindViewHolder(ChapterSelectableAdapter.ViewHolder holder, int position) {
        ChapterListBean bean = mChapterListBeans.get(position);
        if (bean == null) return;
        holder.assign(bean);
    }

    @Override
    public int getItemCount() {
        return mChapterListBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_chapter_name)
        TextView tv_chapter_name;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_free)
        TextView tv_free;

        @BindView(R.id.iv_lock)
        ImageView iv_lock;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void assign(ChapterListBean bean) {
            tv_chapter_name.setText(StringUtil.isEmpty(bean.getChaptername()) ? "" : bean.getChaptername());
            if (bean.getChapterprice() == 0) {
                ((ViewGroup) tv_price.getParent()).setVisibility(View.GONE);
                tv_free.setVisibility(View.VISIBLE);
            } else {
                ((ViewGroup) tv_price.getParent()).setVisibility(View.VISIBLE);
                tv_free.setVisibility(View.GONE);
                tv_price.setText(bean.getChapterprice() + "");
            }
            if (bean.getChaptershoptype() == 0)
                iv_lock.setVisibility(View.VISIBLE);
            else iv_lock.setVisibility(View.GONE);

            itemView.setSelected(mSelections.contains(bean));
            itemView.setTag(bean);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChapterListBean data = (ChapterListBean) v.getTag();
                    if (mSelections.contains(data)) mSelections.remove(data);
                    else mSelections.add(data);
                    if (mOnSelectionsChanged!=null)mOnSelectionsChanged.selectionsChange();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
