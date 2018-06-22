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
import com.yn.reader.model.consumptionRecord.ConsumptionRecordFileByDate;
import com.yn.reader.model.consumptionRecord.ConsumptionRecordFileByDateManager;
import com.yn.reader.widget.FullyLinearLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecordFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<ConsumptionRecordFileByDate> mConsumptionRecordFiles;

    public ConsumptionRecordFileAdapter(Context context, List<ConsumptionRecord> consumptionRecords){
        this.mContext = context;
        mConsumptionRecordFiles = ConsumptionRecordFileByDateManager.getInstance().getConsumptionRecordFilesByDate(consumptionRecords);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_consumption_record_file, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder){
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.assign(mConsumptionRecordFiles.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mConsumptionRecordFiles.size();
    }
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_day)
        TextView tv_day;
        @BindView(R.id.rv_content)
        RecyclerView rv_content;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mContext);
            rv_content.setHasFixedSize(true);
            rv_content.setNestedScrollingEnabled(false);
            rv_content.setLayoutManager(layoutManager);
        }

        public void assign(ConsumptionRecordFileByDate consumptionRecordFileByDate) {
            tv_day.setText(StringUtil.isEmpty(consumptionRecordFileByDate.getDate())?"":(
                    consumptionRecordFileByDate.getDate().equals(TimeUtil.getDataTime("yyyy-MM-dd"))?"今天":(
                            consumptionRecordFileByDate.getDate().equals(TimeUtil.getPreDataTime("yyyy-MM-dd"))?"昨天": consumptionRecordFileByDate.getDate()
                    ))
            );
            rv_content.setAdapter(new ConsumptionRecordAdapter(mContext,consumptionRecordFileByDate.getContain()));
        }
    }
}
