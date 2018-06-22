//package com.yn.reader.view.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.yn.mini.R;
//import com.yn.mini.base.BaseRecyclerViewHolder;
//import com.yn.mini.db.HistoryDao;
//import com.yn.mini.network.model.History;
//import com.yn.mini.util.DateUtils;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created : lts .
// * Date: 2018/1/24
// * Email: lts@aso360.com
// */
//
//public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private static final int HEARD_VIEW = 1;
//    private static final int ITEM_VIEW = 2;
//    private HistoryDao mHistoryDao;
//    private Context mContext;
//    private final LayoutInflater mInflater;
//    private OnItemClickListener mOnItemClickListener;
//
//    public HistoryAdapter(Context context, HistoryDao historyDao) {
//        this.mHistoryDao = historyDao;
//        this.mContext = context;
//        mInflater = LayoutInflater.from(context);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//    public List<History> getData() {
//        return mHistoryDao.loadAll() == null ? new ArrayList<History>() : mHistoryDao.loadAll();
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == HEARD_VIEW) {
//            View view = mInflater.inflate(R.layout.item_history_heard, parent, false);
//            return new BaseRecyclerViewHolder(mContext, view);
//        } else if (viewType == ITEM_VIEW) {
//            View inflate = mInflater.inflate(R.layout.item_history, parent, false);
//            return new HistoryViewHolder(inflate);
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        //查询今天的浏览记录
//        List<History> list = mHistoryDao.queryBuilder().where(HistoryDao.Properties.Id.gt(DateUtils.getTimesmorning())).build().list();
//
//
//        if (holder instanceof BaseRecyclerViewHolder) {
//            BaseRecyclerViewHolder recyclerViewHolder = (BaseRecyclerViewHolder) holder;
//            if (position == 0 && !list.isEmpty()) {
//                recyclerViewHolder.getTextView(R.id.date).setText(mContext.getResources().getString(R.string.today));
//            }
//
//            if (position == 0 && list.isEmpty()) {
//                recyclerViewHolder.getTextView(R.id.date).setText(mContext.getResources().getString(R.string.other_day));
//            }
//
//            if (!list.isEmpty() && position == list.size() +1) {
//                recyclerViewHolder.getTextView(R.id.date).setText(mContext.getResources().getString(R.string.other_day));
//            }
//
//        } else if (holder instanceof HistoryViewHolder) {
//            HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
//            List<History> histories = mHistoryDao.loadAll();
//            Collections.reverse(histories);
//            if (list.isEmpty()) {
//                if (histories != null && !histories.isEmpty()) {
//                    historyViewHolder.bindinData(histories.get(position - 2));
//                }
//            } else {
//
//                historyViewHolder.bindinData(position <= list.size() ? histories.get(position - 1) : histories.get(position - 2));
//
//            }
//
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        List<History> list = mHistoryDao.queryBuilder().where(HistoryDao.Properties.Id.gt(DateUtils.getTimesmorning())).build().list();
//
//        if (list != null) {
//            if (position == 0 || position == list.size() +1) {
//                return HEARD_VIEW;
//            } else {
//                return ITEM_VIEW;
//            }
//        } else {
//            if (position == 0) {
//                return HEARD_VIEW;
//            } else {
//                return ITEM_VIEW;
//            }
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return mHistoryDao.loadAll().isEmpty() ? 0 : mHistoryDao.loadAll().size() + 2;
//    }
//
//    class HistoryViewHolder extends RecyclerView.ViewHolder {
//        TextView mWebsiteTitle;
//        ImageView mIsFollow;
//        View mView;
//
//        public HistoryViewHolder(View itemView) {
//            super(itemView);
//
//            mWebsiteTitle = itemView.findViewById(R.id.websiteTitle) ;
//            mIsFollow = itemView.findViewById(R.id.follow);
//            mView = itemView;
//        }
//
//        void bindinData(final History history) {
//            mWebsiteTitle.setText(history.getTitle());
//            mIsFollow.setSelected(history.getIsFollow());
//
//            mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(history);
//                    }
//                }
//            });
//
//            mIsFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.isFollowClick(history,mIsFollow);
//                    }
//                }
//            });
//        }
//
//        String replaceStr(String str) {
//            String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//            Pattern p   =   Pattern.compile(regEx);
//            Matcher m   =   p.matcher(str);
//            return   m.replaceAll("").trim();
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(History history);
//        void isFollowClick(History history, ImageView imageView);
//    }
//
//
//}
