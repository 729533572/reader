package com.yn.reader.snap;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yn.reader.R;
import com.yn.reader.model.booklist.Navigation;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public void setOnNavigationSelectionChanged(OnNavigationSelectionChanged onNavigationSelectionChanged) {
        mOnNavigationSelectionChanged = onNavigationSelectionChanged;
    }

    public interface OnNavigationSelectionChanged {
        void selected(Navigation beforeSelection, Navigation afterSelection);
    }

    private List<Navigation> mNavigations;
    private boolean mHorizontal;
    private boolean mPager;
    private OnNavigationSelectionChanged mOnNavigationSelectionChanged;

    public Adapter(boolean horizontal, boolean pager, List<Navigation> navigations) {
        mHorizontal = horizontal;
        mNavigations = navigations;
        mPager = pager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mPager) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_pager, parent, false));
        } else {
            return mHorizontal ? new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter, parent, false)) :
                    new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_vertical, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Navigation navigation = mNavigations.get(position);
        if (navigation != null){
            if (getSelection()==null&&mNavigations.indexOf(navigation)==0)
                navigation.setSelected(true);
            holder.assign(navigation);
        }

    }
    private Navigation getSelection(){
        Navigation navigation = null;
        try {
            for (Navigation bean :mNavigations) {
                if (bean.isSelected()){
                    navigation = bean;
                    break;
                }
            }
        }catch (Exception ex){}
        return navigation;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mNavigations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        @Override
        public void onClick(View v) {
            Navigation navigation = (Navigation) v.getTag();
            if (navigation == null || navigation.isSelected()) return;
            Navigation beforeNavigation = null;
            for (Navigation bean : mNavigations) {
                if (bean.isSelected()) beforeNavigation = bean;
                bean.setSelected(false);
            }
            navigation.setSelected(true);
            notifyDataSetChanged();
            Log.d("App", navigation.getName());
            if (mOnNavigationSelectionChanged != null)
                mOnNavigationSelectionChanged.selected(beforeNavigation, navigation);
        }

        public void assign(Navigation navigation) {
            nameTextView.setText(navigation.getName());
            nameTextView.setSelected(navigation.isSelected());

            itemView.setOnClickListener(this);
            itemView.setTag(navigation);
        }
    }

}

