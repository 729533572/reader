package com.yn.reader.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.yn.reader.view.MainActivity;
import com.yn.reader.model.jpush.JPushCustomMsg;
import com.yn.reader.util.Constant;
import com.yn.reader.util.LogUtils;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by sunxy on 2017/4/1.
 */

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null)
            LogUtils.log("onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LogUtils.log("JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.log("接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.log("接受到推送下来的通知");

//            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.log("用户点击打开了通知");
            receivingNotification(context,bundle);

        } else {
            LogUtils.log("Unhandled intent - " + intent.getAction());
        }
    }


    private void receivingNotification(Context context, Bundle bundle) {
        if(bundle!=null) {
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!TextUtils.isEmpty(extras)) {
                LogUtils.log("extras : " + extras);
                try {
                    JPushCustomMsg msg = JSON.parseObject(extras, JPushCustomMsg.class);
                    if (msg != null) {
                        String type = msg.getType();
                        if (!TextUtils.isEmpty(type) && type.equals("newsdetail")) {
                            Intent mIntent = new Intent(context, MainActivity.class);
                            mIntent.putExtra(Constant.KEY_JPUSH_MSG, msg);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(mIntent);
                        }
                    }
                }catch (com.alibaba.fastjson.JSONException e){

                }
            }
        }

    }

}
