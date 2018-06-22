//package com.yn.reader.view.fragment.consume;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.yn.reader.R;
//
//
///**
// * Created : lts .
// * Date: 2018/1/4
// * Email: lts@aso360.com
// */
//
//public class ConsumeFragment extends Fragment {
//
//
//    private static final int DIVIDER_VIEW = 0xecf123;
//    private RecyclerView mRecyclerView;
//    private HistoryAdapter mAdapter;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_history, container, false);
//        mRecyclerView = view.findViewById(R.id.history_rec);
//        initAdapter();
//        return view;
//    }
//
//    private void initAdapter() {
////        mAdapter = new HistoryAdapter(mActivity, mDaoSession.getHistoryDao());
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        DividerItemDecoration decor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        decor.setDrawable(getResources().getDrawable(R.drawable.divider_line));
//        mRecyclerView.addItemDecoration(decor);
//        mRecyclerView.setAdapter(mAdapter);
//    }
//
//
//
//
//    private void showDialog() {
////        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
////                .setTitle(getResources().getString(R.string.prompt))
////                .setMessage(getResources().getString(R.string.clear_history_prompt))
////                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.dismiss();
////                        BookMarkActivity activity = (BookMarkActivity) mActivity;
////                        activity.clearHistory();
////                        clearAdapter();
////
////                    }
////                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.dismiss();
////                    }
////                }).create();
////
////        alertDialog.show();
//    }
//
//
//    public void clearAdapter() {
//        mAdapter.getData().clear();
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//    }
//
//
//}
