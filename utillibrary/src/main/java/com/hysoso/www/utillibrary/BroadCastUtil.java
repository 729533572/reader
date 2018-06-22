package com.hysoso.www.utillibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by luhe on 17/1/7.
 */

public class BroadCastUtil {
    public static void sendBroadcast(Context context , Class<? extends BroadcastReceiver> cls,String msgContent) {
        Intent intent = new Intent();
        intent.setAction(cls.getSimpleName());
        intent.putExtra(cls.getSimpleName(),msgContent);
        context.sendBroadcast(intent);
    }


    public static void sendBroadcast(Context context , Class<? extends BroadcastReceiver> cls,Boolean msgContent) {
        Intent intent = new Intent();
        intent.setAction(cls.getSimpleName());
        intent.putExtra(cls.getSimpleName(),msgContent);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context , Class<? extends BroadcastReceiver> cls,Boolean msgContent,String msgName) {
        if (StringUtil.isEmpty(msgName)){
            sendBroadcast(context,cls,msgContent);
        }else {
            Intent intent = new Intent();
            intent.setAction(cls.getSimpleName());
            intent.putExtra(msgName,msgContent);
            context.sendBroadcast(intent);
        }
    }
    public static void sendLocalBroadcast(Context context,Class<?> cls,String arg){
        Intent intent = new Intent(cls.getSimpleName());
        intent.putExtra(cls.getSimpleName(),arg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public static void sendLocalBroadcast(Context context,Class<?> cls,int arg){
        Intent intent = new Intent(cls.getSimpleName());
        intent.putExtra(cls.getSimpleName(),arg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public static void sendLocalBroadcast(Context context,Class<?> cls,Boolean arg){
        Intent intent = new Intent(cls.getSimpleName());
        intent.putExtra(cls.getSimpleName(),arg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
