package com.hysoso.www.utillibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * Created by LuHe on 2016/11/10.
 */

public class PhoneUtil {
    /**
     * 打电话到指定号码
     *
     * @param context
     * @param tel
     */
    public static void callTel(Context context, String tel) {
        if (StringUtil.isEmpty(tel))return;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 发短信到指定号码
     *
     * @param mActivity
     * @param mTel_num
     */
    public static void sendMessageToSomebody(Activity mActivity,String mTel_num) {
        Uri uri = Uri.parse("smsto:"+mTel_num);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        mActivity.startActivity(sendIntent);
    }

    /**
     * @param mActivity
     * @param message 内容
     */
    public static void sendMessage(Context mActivity,String message) {
        Uri uri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.putExtra("sms_body",message);
        mActivity.startActivity(sendIntent);
    }
}
