package com.yn.reader.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.yn.reader.MiniApp;
import com.yn.reader.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by luhe on 17/2/17.
 */

public class WXUtil {
    /**
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     * @param webpageUrl
     * @param title
     * @param desc
     */
    public static SendMessageToWX.Req mReq = null;

    public static void wechatShare(Context context, int flag, String webpageUrl, String title, String desc, String thumbUrl) throws ExecutionException, InterruptedException {
        if (isWebchatAvaliable(context)) {
            WXWebpageObject textObj = new WXWebpageObject();
            if (!StringUtil.isEmpty(webpageUrl)) textObj.webpageUrl = webpageUrl;

            WXMediaMessage msg = new WXMediaMessage(textObj);
            if (!StringUtil.isEmpty(title)) msg.title = title;
            if (!StringUtil.isEmpty(desc)) msg.description = desc;

            Bitmap thumb = null;

            if (!StringUtil.isEmpty(thumbUrl)) {
                thumb = Glide.with(context)
                        .load(thumbUrl)
                        .asBitmap() //必须
                        .centerCrop()
                        .into(200, 200)
                        .get();
            }
            if (thumb == null) {
                thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_icon);
                thumb = Bitmap.createScaledBitmap(thumb, 200, 200, true);
            }
            msg.setThumbImage(thumb);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis()); //唯一表识
            req.message = msg;

            int TIMELINE_SUPPORTED_VERSION = 0x21020001;
            if (flag == 1) {
                if (MiniApp.getInstance().getIWXAPI().getWXAppSupportAPI() < TIMELINE_SUPPORTED_VERSION) {
                    flag = 0;
                    ToastUtil.showShort(context, "微信版本低，不支持分享到朋友圈功能");
                }
            }
            req.scene = (flag == 1 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession);//WXSceneTimeline 朋友圈;WXSceneSession 好友会话；
            mReq = req;
            MiniApp.getInstance().getIWXAPI().sendReq(req);
        } else ToastUtil.showShort(context, "请检查您手机是否已经安装了微信");
    }
    public static boolean isWebchatAvaliable(Context context) {
        //检测手机上是否安装了微信
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void wechatShare(Context context,int flag, String title, String desc){
        if (isWebchatAvaliable(context)){
            WXTextObject textObj = new WXTextObject();
            textObj.text = desc;

            WXMediaMessage msg = new WXMediaMessage(textObj);
            if (!StringUtil.isEmpty(title))msg.title = title;
            if (!StringUtil.isEmpty(desc))msg.description = desc;

//            Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//            msg.setThumbImage(thumb);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis()); //唯一表识
            req.message = msg;

            int TIMELINE_SUPPORTED_VERSION = 0x21020001;
            if(flag==1){
                if (MiniApp.getInstance().getIWXAPI().getWXAppSupportAPI()<TIMELINE_SUPPORTED_VERSION){
                    flag=0;
                    ToastUtil.showShort(context,"微信版本低，不支持分享到朋友圈功能");
                }
            }
            req.scene = (flag==1? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession);//WXSceneSession 好友会话；WXSceneTimeline 朋友圈

            MiniApp.getInstance().getIWXAPI().sendReq(req);
        }else ToastUtil.showShort(context,"请检查您手机是否已经安装了微信");
    }
    public static boolean isSupportPay(){
        boolean isPaySupported = MiniApp.getInstance().getIWXAPI().getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        return isPaySupported;
    }
}
