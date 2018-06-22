package com.yn.reader.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.comment.Comment;
import com.yn.reader.util.GlideUtils;
import com.yn.reader.util.IntentUtils;
import com.yn.reader.util.UserInfoManager;
import com.yn.reader.view.LoginTipActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunxy on 2018/3/16.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    public CommentAdapter setOnCommentItemClick(OnCommentItemClick onCommentItemClick) {
        mOnCommentItemClick = onCommentItemClick;
        return this;
    }

    public CommentAdapter setShowLikeAndReport(Boolean showLikeAndReport) {
        this.showLikeAndReport = showLikeAndReport;
        return this;
    }

    public interface OnCommentItemClick {
        void like(Comment comment);

        void report(Comment comment);
    }

    private Context mContext;
    private List<Comment> commentList;
    private OnCommentItemClick mOnCommentItemClick;
    private Boolean showLikeAndReport = true;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.commentList = commentList;
        mContext = context;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        Comment post = getItem(position);
        if (post != null) {
            holder.setData(post);
        }

    }

    private Comment getItem(int position) {
        if (commentList != null && position < commentList.size()) {
            return commentList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (commentList == null) return 0;
        return commentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private @NonNull
        CircleImageView avatar;
        private @NonNull
        TextView userName;
        private @NonNull
        TextView time;
        private @NonNull
        TextView tvComment;
        TextView tv_like;
        ImageView iv_report;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            userName = itemView.findViewById(R.id.user_name);
            time = itemView.findViewById(R.id.time);
            tvComment = itemView.findViewById(R.id.comment);
            tv_like = itemView.findViewById(R.id.tv_like);
            iv_report = itemView.findViewById(R.id.iv_report);
        }

        void setData(@NonNull final Comment comment) {
            GlideUtils.load(mContext, comment.getAvatar(), avatar, R.mipmap.ic_hold_place_header_img);
            userName.setText(comment.getNickname());
            time.setText(comment.getCommentdate());
            tvComment.setText(comment.getCommentcontent());

            tv_like.setText(String.valueOf(comment.getNum()));
            tv_like.setSelected(comment.getHasfavorite() == 1);

            tv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!UserInfoManager.getInstance().isLanded()) {
                        IntentUtils.startActivity(mContext, LoginTipActivity.class);
                        return;
                    }

                    comment.setHasfavorite(1 - comment.getHasfavorite());
                    if (comment.getHasfavorite() == 1) comment.setNum(comment.getNum() + 1);
                    else comment.setNum(comment.getNum() - 1);

                    if (mOnCommentItemClick != null) mOnCommentItemClick.like(comment);

                    notifyDataSetChanged();
                }
            });

            iv_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnCommentItemClick != null) mOnCommentItemClick.report(comment);
                }
            });

            tv_like.setVisibility(showLikeAndReport ? View.VISIBLE : View.GONE);
            iv_report.setVisibility(showLikeAndReport ? View.VISIBLE : View.GONE);

        }
    }
}
