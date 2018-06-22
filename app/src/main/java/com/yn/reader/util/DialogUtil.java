package com.yn.reader.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;

import com.hysoso.www.utillibrary.DensityUtil;
import com.yn.reader.R;
import com.yn.reader.base.BaseActivity;

/**
 * Created by luhe on 2017/9/24.
 */

public class DialogUtil {
    public interface OnListCenterListener {
        BaseAdapter installAdapter();

        String setTitle();

        void loadData(AbsListView listView, SwipeRefreshLayout swipeRefreshLayout);

        void clickObj(Object... obj);
    }

    public interface OnPopupWindow {
        int setLayoutId();

        void installPopupWindow(PopupWindow popupWindow);
    }

    public interface OnClickMakeTrue {
        void click();
    }

    public interface OnClickOption {
        void makeTrue();

        void cancel();
    }
//    /**
//     * @param context
//     * @param onListCenterListener
//     */
//    public static void showListCenter(final Context context, final OnListCenterListener onListCenterListener){
//        ViewGroup contentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dialog_center_list, null);
//
//        PopupWindowCardView cv_content = contentView.findViewById(R.id.cv_content);
//        cv_content.setMaxHeight(DensityUtil.getWindowWidth(context));
//
//        final ListView lv_dialog_center_list
//                =  contentView.findViewById(R.id.lv_dialog_center_list);
//
//        Float space = context.getResources().getDimension(R.dimen.activity_horizontal_margin_large);
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                DensityUtil.getWindowWidth(context)-space.intValue()*2, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setTouchable(true);
//
//        TextView tv_dialog_center_list_title
//                = (TextView) contentView.findViewById(R.id.tv_dialog_center_list_title);
//        tv_dialog_center_list_title.setText(StringUtil.isEmpty(onListCenterListener.setTitle())?"请选择您的选项":onListCenterListener.setTitle());
//
//        lv_dialog_center_list.setAdapter(onListCenterListener.installAdapter());
//        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                onListCenterListener.clickObj(item);
//                popupWindow.dismiss();
//            }
//        };
//        lv_dialog_center_list.setOnItemClickListener(onItemClickListener);
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                srl_dialog_center_list.setRefreshing(true);
////                onListCenterListener.loadData(lv_dialog_center_list,srl_dialog_center_list);
//                handler.removeCallbacks(this);
//            }
//        }, DataCenter.TIMEINTERVAL);
//
//        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        // 设置好参数之后再show
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(context,1f);
//            }
//        });
//
//        //为popWindow添加动画效果
//        popupWindow.setAnimationStyle(R.style.popWindow_animation_center);
//        // 点击弹出泡泡窗口
//        BaseActivity activity = (BaseActivity) context;
//        View parent = activity.getMainView();
//
//        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
//        backgroundAlpha(context,0.5f);
//    }
//
//    /**
//     * @param context
//     * @param onListCenterListener
//     */
//    public static void showListCenter1(final Context context, final OnListCenterListener onListCenterListener){
//        ViewGroup contentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dialog_center_list1, null);
//
//        PopupWindowCardView cv_content = contentView.findViewById(R.id.cv_content);
//        cv_content.setMaxHeight(DensityUtil.getWindowWidth(context));
//
//        final ListView lv_dialog_center_list
//                =  contentView.findViewById(R.id.lv_dialog_center_list);
//
//        Float space = context.getResources().getDimension(R.dimen.activity_horizontal_margin_large);
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                DensityUtil.getWindowWidth(context)-space.intValue()*2, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setTouchable(true);
//
//        TextView tv_dialog_center_list_title
//                = (TextView) contentView.findViewById(R.id.tv_dialog_center_list_title);
//        tv_dialog_center_list_title.setText(StringUtil.isEmpty(onListCenterListener.setTitle())?"请选择您的选项":onListCenterListener.setTitle());
//
//        lv_dialog_center_list.setAdapter(onListCenterListener.installAdapter());
//        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                onListCenterListener.clickObj(item);
//                popupWindow.dismiss();
//            }
//        };
//        lv_dialog_center_list.setOnItemClickListener(onItemClickListener);
//
//        final SwipeRefreshLayout srl_dialog_center_list = (SwipeRefreshLayout) contentView.findViewById(R.id.srl_dialog_center_list);
//        if (srl_dialog_center_list!=null) {
//            //设置卷内的颜色
//            srl_dialog_center_list.setColorSchemeResources(android.R.color.holo_blue_bright,
//                    android.R.color.holo_blue_light, android.R.color.holo_blue_dark);
//
//            SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    onListCenterListener.loadData(lv_dialog_center_list, srl_dialog_center_list);
//                }
//            };
//            srl_dialog_center_list.setOnRefreshListener(onRefreshListener);
//        }
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                srl_dialog_center_list.setRefreshing(true);
////                onListCenterListener.loadData(lv_dialog_center_list,srl_dialog_center_list);
//                handler.removeCallbacks(this);
//            }
//        }, DataCenter.TIMEINTERVAL);
//
//        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        // 设置好参数之后再show
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(context,1f);
//            }
//        });
//
//        //为popWindow添加动画效果
//        popupWindow.setAnimationStyle(R.style.popWindow_animation_center);
//        // 点击弹出泡泡窗口
//        BaseActivity activity = (BaseActivity) context;
//        View parent = activity.getMainView();
//
//        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
//        backgroundAlpha(context,0.5f);
//    }
//
//    public static void showGridCenter(final Context context, final OnListCenterListener onListCenterListener){
//        ViewGroup contentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dialog_center_grid, null);
//
//        Float space = context.getResources().getDimension(R.dimen.activity_horizontal_margin_large);
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                DensityUtil.getWindowWidth(context)-space.intValue()*2, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setHeight(2* DensityUtil.getWindowHeight(context)/3);
//        popupWindow.setTouchable(true);
//
//        TextView tv_dialog_center_list_title
//                = (TextView) contentView.findViewById(R.id.tv_dialog_center_grid_title);
//        tv_dialog_center_list_title.setText(StringUtil.isEmpty(onListCenterListener.setTitle())?"请选择您的选项":onListCenterListener.setTitle());
//
//        final GridView gv_dialog_center_grid
//                = (GridView) contentView.findViewById(R.id.gv_dialog_center_grid);
//        gv_dialog_center_grid.setAdapter(onListCenterListener.installAdapter());
//
////        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Object item = parent.getItemAtPosition(position);
////                onListCenterListener.clickObj(item);
////                popupWindow.dismiss();
////            }
////        };
////        gv_dialog_center_grid.setOnItemClickListener(onItemClickListener);
//
//        final SwipeRefreshLayout srl_dialog_center_grid = (SwipeRefreshLayout) contentView.findViewById(R.id.srl_dialog_center_grid);
//        //设置卷内的颜色
//        srl_dialog_center_grid.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_blue_light, android.R.color.holo_blue_dark);
//
//        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                onListCenterListener.loadData(gv_dialog_center_grid,srl_dialog_center_grid);
//            }
//        };
//        srl_dialog_center_grid.setOnRefreshListener(onRefreshListener);
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                srl_dialog_center_grid.setRefreshing(true);
//                onListCenterListener.loadData(gv_dialog_center_grid,srl_dialog_center_grid);
//                handler.removeCallbacks(this);
//            }
//        }, DataCenter.TIMEINTERVAL);
//
//        TextView tv_dialog_center_grid_make_true
//                = (TextView) contentView.findViewById(R.id.tv_dialog_center_grid_make_true);
//        tv_dialog_center_grid_make_true.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onListCenterListener.clickObj();
//                popupWindow.dismiss();
//            }
//        });
//
//        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        // 设置好参数之后再show
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(context,1f);
//            }
//        });
//
//        //为popWindow添加动画效果
//        popupWindow.setAnimationStyle(R.style.popWindow_animation_center);
//        // 点击弹出泡泡窗口
//        BaseActivity activity = (BaseActivity) context;
//        View parent = activity.getMainView();
//
//        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
//        backgroundAlpha(context,0.5f);
//    }

    /**
     * 整个界面的透明度调节
     *
     * @param context
     * @param bgAlpha
     */
    public static void backgroundAlpha(Context context, float bgAlpha) {
        BaseActivity activity = (BaseActivity) context;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    public static void showPopWindowFromBottom(final Context context, OnPopupWindow onPopupWindow) {
        if (onPopupWindow == null) return;
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(context).inflate(onPopupWindow.setLayoutId(), null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        onPopupWindow.installPopupWindow(popupWindow);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setTouchable(true);

        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });

        //为popWindow添加动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_animation_bottom);
        // 点击弹出泡泡窗口
        Activity activity = (Activity) context;
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(context, 0.5f);
    }

    public static void showPopWindowFromCenter(final Context context, OnPopupWindow onPopupWindow) {
        if (onPopupWindow == null) return;
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(context).inflate(onPopupWindow.setLayoutId(), null);

        int space = context.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin_large);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                DensityUtil.getWindowWidth(context) - space * 2, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        onPopupWindow.installPopupWindow(popupWindow);

        popupWindow.setTouchable(true);

        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });

        //为popWindow添加动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_animation_center);
        // 点击弹出泡泡窗口
        Activity activity = (Activity) context;
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        backgroundAlpha(context, 0.5f);
    }
//    /**
//     * 提示
//     *
//     * @param context
//     * @param des
//     */
//    public static void warn(final Context context, final String des){
//        showPopWindowFromCenter(context, new OnPopupWindow() {
//            @Override
//            public int setLayoutId() {
//                return R.layout.popup_window_center_warn;
//            }
//
//            @Override
//            public void installPopupWindow(final PopupWindow popupWindow) {
//                View contentView = popupWindow.getContentView();
//
//                TextView tv_desc = contentView.findViewById(R.id.tv_desc);
//                TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
//
//                tv_desc.setText(StringUtil.isEmpty(des)?"注意":des);
//                tv_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                    }
//                });
//            }
//        });
//    }
//    /**
//     * 提示
//     *
//     * @param context
//     * @param des
//     */
//    public static void warn(final Context context, final String des, final OnClickMakeTrue onClickMakeTrue){
//        showPopWindowFromCenter(context, new OnPopupWindow() {
//            @Override
//            public int setLayoutId() {
//                return R.layout.popup_window_center_tip;
//            }
//
//            @Override
//            public void installPopupWindow(final PopupWindow popupWindow) {
//                View contentView = popupWindow.getContentView();
//
//                TextView tv_desc = contentView.findViewById(R.id.tv_desc);
//                TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
//                TextView tv_make_true = contentView.findViewById(R.id.tv_make_true);
//
//                tv_desc.setText(StringUtil.isEmpty(des)?"注意":des);
//                tv_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                    }
//                });
//                tv_make_true.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onClickMakeTrue!=null)onClickMakeTrue.click();
//                        popupWindow.dismiss();
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * 提示
//     *
//     * @param context
//     * @param des
//     */
//    public static void warn(final Context context, final String des, final OnClickOption onClickOption){
//        showPopWindowFromCenter(context, new OnPopupWindow() {
//            @Override
//            public int setLayoutId() {
//                return R.layout.popup_window_center_tip;
//            }
//
//            @Override
//            public void installPopupWindow(final PopupWindow popupWindow) {
//                View contentView = popupWindow.getContentView();
//
//                TextView tv_desc = contentView.findViewById(R.id.tv_desc);
//                TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
//                TextView tv_make_true = contentView.findViewById(R.id.tv_make_true);
//
//                tv_desc.setText(StringUtil.isEmpty(des)?"注意":des);
//
//                tv_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                        if (onClickOption!=null)onClickOption.cancel();
//                    }
//                });
//                tv_make_true.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                        if (onClickOption!=null)onClickOption.makeTrue();
//                    }
//                });
//
//            }
//        });
//    }
}
