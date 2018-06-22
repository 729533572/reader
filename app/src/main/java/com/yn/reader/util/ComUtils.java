package com.yn.reader.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.yn.reader.BuildConfig;
import com.yn.reader.MiniApp;
import com.yn.reader.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 通用接口
 * Created by sunxy on 2017/12/26.
 */

public class ComUtils {



    public static boolean isNewsUrl(String url) {
        if(TextUtils.isEmpty(url))
            return false;
        return url.contains("/h5/detail?messageid");
    }

    public static int dip2px(float dipValue) {
        final float scale = MiniApp.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        float fontScale = MiniApp.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 检查Intent 是否在 系统存在
     */
    public static boolean isIntentSafe(Context context, Intent targetIntent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(targetIntent, 0);
        return activities.size() > 0;
    }
    /**
     * 将源字符串使用MD5加密为32位16进制数
     *
     * @param source
     * @return
     */
    public static String encode2hex(String source) {
        byte[] data = encode2bytes(source);

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * 后台token 生成方式
     * md5(openid +md5(openid + unionid + package + version + idfa + time))
     *
     * @param openid   微信的openID
     * @param unionid  微信的unionID
     * @param packages 应用包名
     * @param version  应用版本
     * @param idfa     设备ID
     * @param time     当前时间
     * @return string
     */
    public static String getToken(String openid, String unionid, String packages, String version, String idfa, String time) {
        return encode2hex(openid + encode2hex(openid + unionid + packages + version + idfa + time));
    }

    /**
     * 将源字符串使用MD5加密为字节数组
     *
     * @param source
     * @return
     */
    private static byte[] encode2bytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes("UTF-8"));
            result = md.digest();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取版本名称
     *
     * @param context context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName;
        try {
            versionName = context.getPackageManager().getPackageInfo(Constant.PACKAGE_NAME, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "1.0";
        }
        return versionName;
    }

    /**
     * 获取版本号
     *
     * @param context context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(Constant.PACKAGE_NAME, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = 1;
        }
        return versionCode;
    }

    public static String getEncodeString(String q) {
        if(!TextUtils.isEmpty(q)) {
            try {
                return URLEncoder.encode(q, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return q;
    }
    public static String getAppName() {
        return MiniApp.getInstance().getString(R.string.app_name);
    }


    public static void login(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        MiniApp.getInstance().getIWXAPI().sendReq(req);
    }

}
