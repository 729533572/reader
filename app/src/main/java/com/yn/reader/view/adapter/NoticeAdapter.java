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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.yn.reader.R;
import com.yn.reader.model.notice.Notice;

import java.util.List;


/**
 * 收藏-历史
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    public interface OnItemClick{
        void click(int position);
    }
    private Context mContext;
    private List<Notice> noticeList;
    private boolean isEditModel = false;
    private OnItemClick mOnItemClick;
    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public NoticeAdapter(Context context, List<Notice> noticeList) {
        this.noticeList = noticeList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.e(getClass().getSimpleName(),"onBindViewHolder");
        Notice post = getItem(position);
        if (post != null) holder.setData(post);
    }

    private Notice getItem(int position) {
        if (noticeList != null && position < noticeList.size()) {
            return noticeList.get(position);
        }
        return null;
    }


    @Override
    public int getItemCount() {
        if (noticeList == null) return 0;
        return noticeList.size();
    }

    public void setEditModel(boolean editModel) {
        LogUtil.e("setEditModel",editModel?"编辑模式":"完成模式");
        isEditModel = editModel;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        TextView title;
        private @NonNull
        TextView time;

        private @NonNull
        ImageView select_radio;
        private @NonNull
        View mask_view;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_title);
            time = itemView.findViewById(R.id.notice_time);
            select_radio = itemView.findViewById(R.id.select_radio);
            mask_view = itemView.findViewById(R.id.mask_view);

        }

        void setData(@NonNull final Notice notice) {
            try {
                title.setText(StringUtil.isEmpty(notice.getTitle())?"":notice.getTitle());
                time.setText(StringUtil.isEmpty(notice.getCreatedate())?"": TimeUtil.getDayDate(notice.getCreatedate()));

                if (isEditModel){
                    select_radio.setVisibility(View.VISIBLE);
                    select_radio.setSelected(notice.isSelect());
                }
                else {
                    select_radio.setVisibility(View.GONE);
                    select_radio.setSelected(false);
                    notice.setSelect(false);
                }

                mask_view.setVisibility(notice.getStatus()==1?View.VISIBLE:View.GONE);
                itemView.setTag(notice);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Notice notice = (Notice) view.getTag();
                        if(isEditModel) {
                            notice.setSelect(!notice.isSelect());
                            notifyDataSetChanged();
                        }
                        else if (mOnItemClick!=null)
                            mOnItemClick.click(noticeList.indexOf(notice));

                    }
                });
            }catch (Exception ex){
                LogUtil.e(getClass().getSimpleName(),ex.getMessage()+"/"+ex.getCause());
            }
        }
    }
}
