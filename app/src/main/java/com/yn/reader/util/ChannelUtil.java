package com.yn.reader.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.yn.reader.BuildConfig;
import com.yn.reader.MiniApp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 渠道号
 * Created by xixh on 2014/12/12.
 */
public class ChannelUtil {
    public static final String CHANNEL_KEY = "channel";

    /**
     * 获取channel ID
     */
    public static String getChannelID() {
        if (BuildConfig.DEBUG) return "1000";
        String channelID = AppPreference.getInstance().getChannelID();

        if (TextUtils.isEmpty(channelID)) {
            Context context = MiniApp.getInstance();
            channelID = getChannelFromApk(context, CHANNEL_KEY);
            if (!TextUtils.isEmpty(channelID)) AppPreference.getInstance().setChannelID(channelID);
        }
        LogUtils.log("channelID="+channelID);
        return channelID;
    }

    /**
     * 从apk中获取版本信息
     */
    private static String getChannelFromApk(Context context, String channelKey) {


        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        String channel = "1";
        if (split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }
        return channel;
    }
}
