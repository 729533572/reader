package com.yn.reader.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.buy.BuyChoice;
import com.yn.reader.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 包月购买
 * Created by sunxy on 2018/3/6.
 */

public class BuyChoiceAdapter extends RecyclerView.Adapter<BuyChoiceAdapter.ViewHolder> {

    public interface OnSelectChoiceChanged{
        void select(BuyChoice choice);
    }
    private Context mContext;
    private List<BuyChoice> posts;
    private int mType;

    private OnSelectChoiceChanged mOnSelectChoiceChanged;
    public void setOnSelectChoiceChanged(OnSelectChoiceChanged onSelectChoiceChanged) {
        mOnSelectChoiceChanged = onSelectChoiceChanged;
    }

    public BuyChoiceAdapter(Context context, List<BuyChoice> posts, int type) {
        this.posts = posts;
        mContext = context;
        mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_buy, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BuyChoiceAdapter.ViewHolder holder, int position) {
        BuyChoice post = getItem(position);
        if (post != null) holder.setData(post);
    }

    private BuyChoice getItem(int position) {
        if (posts != null && position < posts.size()) {
            return posts.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (posts == null) return 0;
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_time_duration)
        TextView tv_time_duration;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(BuyChoice buyChoice) {
            tv_price.setText(buyChoice.getPrice() + "");
            if (mType == Constant.BUY_TYPE_MONTHLY)
                tv_time_duration.setText(buyChoice.getDays() + "天");
            else if (mType == Constant.BUY_TYPE_READ_POINT)
                tv_time_duration.setText(buyChoice.getCoin() + "阅读点");

            itemView.setTag(buyChoice);
            itemView.setSelected(buyChoice.isSelected());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuyChoice tag = (BuyChoice) v.getTag();
                    if (tag.isSelected()) return;
                    for (BuyChoice bean : posts) {
                        bean.setSelected(false);
                    }
                    tag.setSelected(true);
                    if (mOnSelectChoiceChanged!=null)mOnSelectChoiceChanged.select(tag);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
