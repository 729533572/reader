package com.yn.reader.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.yn.reader.R;
import com.yn.reader.model.rechargeRecord.RechargeRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<RechargeRecord> mRechargeRecords;

    public RechargeRecordAdapter(Context context, List<RechargeRecord> rechargeRecords){
        this.mContext = context;
        mRechargeRecords = rechargeRecords;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recharge_record, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder){
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.assign(mRechargeRecords.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mRechargeRecords.size();
    }
    public class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recharge_record_time)
        TextView tv_recharge_record_time;

        @BindView(R.id.tv_recharge_record_content)
        TextView tv_recharge_record_content;

        @BindView(R.id.tv_recharge_record_money)
        TextView tv_recharge_record_money;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void assign(RechargeRecord rechargeRecord) {
            tv_recharge_record_time.setText(StringUtil.isEmpty(rechargeRecord.getCreatedate())?"":rechargeRecord.getCreatedate().substring(TimeUtil.getDayDate(rechargeRecord.getCreatedate()).length()));
            tv_recharge_record_content.setText(StringUtil.isEmpty(rechargeRecord.getContent())?"":rechargeRecord.getContent());
            tv_recharge_record_money.setText(rechargeRecord.getMoney()+"元");
        }
    }
}
