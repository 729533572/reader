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
import com.yn.reader.model.rechargeRecord.RechargeRecordFileByDate;
import com.yn.reader.model.rechargeRecord.RechargeRecordFileByDateManager;
import com.yn.reader.widget.FullyLinearLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecordFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<RechargeRecordFileByDate> mRechargeRecordFiles;

    public RechargeRecordFileAdapter(Context context, List<RechargeRecord> rechargeRecords){
        this.mContext = context;
        mRechargeRecordFiles = RechargeRecordFileByDateManager.getInstance().getRechargeRecordFilesByDate(rechargeRecords);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recharge_record_file, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder){
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.assign(mRechargeRecordFiles.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mRechargeRecordFiles.size();
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

        public void assign(RechargeRecordFileByDate rechargeRecordFileByDate) {
            tv_day.setText(StringUtil.isEmpty(rechargeRecordFileByDate.getDate())?"":(
                    rechargeRecordFileByDate.getDate().equals(TimeUtil.getDataTime("yyyy-MM-dd"))?"今天":(
                            rechargeRecordFileByDate.getDate().equals(TimeUtil.getPreDataTime("yyyy-MM-dd"))?"昨天": rechargeRecordFileByDate.getDate()
                            ))
                    );
            rv_content.setAdapter(new RechargeRecordAdapter(mContext,rechargeRecordFileByDate.getContain()));
        }
    }
}
