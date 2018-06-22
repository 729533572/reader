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
import com.yn.reader.model.consumptionRecord.ConsumptionRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<ConsumptionRecord> mRechargeRecords;

    public ConsumptionRecordAdapter(Context context, List<ConsumptionRecord> rechargeRecords){
        this.mContext = context;
        mRechargeRecords = rechargeRecords;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_consumption_record, parent, false));
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

        @BindView(R.id.tv_consumption_record_time)
        TextView tv_consumption_record_time;

        @BindView(R.id.tv_consumption_record_content)
        TextView tv_consumption_record_content;

        @BindView(R.id.tv_consumption_record_money)
        TextView tv_consumption_record_money;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void assign(ConsumptionRecord consumptionRecord) {
            tv_consumption_record_time.setText(StringUtil.isEmpty(consumptionRecord.getCreatedate())?"":consumptionRecord.getCreatedate().substring(TimeUtil.getDayDate(consumptionRecord.getCreatedate()).length()));
            tv_consumption_record_content.setText(StringUtil.isEmpty(consumptionRecord.getContent())?"":consumptionRecord.getContent());
            tv_consumption_record_money.setText(consumptionRecord.getCoin()+"点");
        }
    }
}
