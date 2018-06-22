package com.yn.reader.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：陆贺 on 17/3/15
 * Email：1019561500@qq.com
 */

public abstract class BaseCustomAdapter1 extends BaseAdapter {
    public String TAG = getClass().getSimpleName();
    public interface OnItemElementClickListener{
        void itemClick(Object item);
    }
    public List mObjects;
    public JSONArray mJsnObjects;
    public Context mContext;
    public Integer mLayoutId;
    public OnItemElementClickListener mOnItemElementClickListener;

    public BaseCustomAdapter1(Context context, List objects){
        mContext = context;
        mObjects = objects;
        mLayoutId = setLayoutId();
    }
    public BaseCustomAdapter1(Context context, JSONArray objects){
        mContext = context;
        mJsnObjects = objects;
        mLayoutId = setLayoutId();
    }

    public BaseCustomAdapter1(Context context, String[] objects){
        mContext = context;
        mObjects = new ArrayList();
        for (String text :objects){
            mObjects.add(text);
        }
        mLayoutId = setLayoutId();
    }
    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int i) {
        return mObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup parent) {
        Object item =  mObjects.get(i);
        if (contentView==null){
            contentView = View.inflate(mContext,mLayoutId,null);
            BaseHolder holder = setHolder(contentView);
            contentView.setTag(holder);
        }
        BaseHolder holder = (BaseHolder) contentView.getTag();
        holder.assign(item);
        return  contentView;
    }
    protected abstract BaseHolder setHolder(View view);
    protected abstract Integer setLayoutId();

    public void clear(){
        mObjects.clear();
        notifyDataSetChanged();
    }
    public void setOnItemElementClickListener(OnItemElementClickListener onItemElementClickListener){
        mOnItemElementClickListener = onItemElementClickListener;
    }
}
