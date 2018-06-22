package com.yn.reader.util;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hysoso.www.viewlibrary.ShrinkLinearLayout;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yn.reader.R;

import java.util.concurrent.ExecutionException;

/**
 * 分享好友
 * Created by sunxy on 2018/1/15.
 */

public class ShareUtils {
    public static void showShare(Activity context, String title, String shareUrl, String imageUrl) {
        showShare1(context, title, shareUrl, imageUrl);
    }

    public static void showShare1(final Activity context, final String title, final String shareUrl, final String imageUrl) {
        DialogUtil.showPopWindowFromBottom(context, new DialogUtil.OnPopupWindow() {

            @Override
            public int setLayoutId() {
                return R.layout.popupwindow_share;
            }

            @Override
            public void installPopupWindow(final PopupWindow popupWindow) {
                View content = popupWindow.getContentView();

                ShrinkLinearLayout ll_share_wx = content.findViewById(R.id.ll_share_wx);
                ShrinkLinearLayout ll_share_friend_circle = content.findViewById(R.id.ll_share_friend_circle);
                TextView tv_cancel = content.findViewById(R.id.tv_cancel);

                ll_share_wx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            WXUtil.wechatShare(context,
                                    SendMessageToWX.Req.WXSceneSession,
                                    shareUrl,
                                    title,
                                    "我正在使用Mini浏览器。浏览快，省流量，超级赞，还不赶快试一试",
                                    imageUrl);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();
                    }
                });
                ll_share_friend_circle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            WXUtil.wechatShare(
                                    context,
                                    SendMessageToWX.Req.WXSceneTimeline,
                                    shareUrl,
                                    title,
                                    "我正在使用Mini浏览器。浏览快，省流量，超级赞，还不赶快试一试",
                                    imageUrl);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {


        }
    };
}
