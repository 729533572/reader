package com.yn.reader.util;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.yn.reader.R;

/**
 * Created by luhe on 2018/5/26.
 */

public class RecyclerViewUtil {

    public static void setDecoration(RecyclerView recyclerView, int dividerItemDecoration) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), dividerItemDecoration);
        itemDecoration.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.divider_line));
        recyclerView.addItemDecoration(itemDecoration);
    }
}
