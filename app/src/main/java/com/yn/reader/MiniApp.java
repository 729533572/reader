package com.yn.reader;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yn.reader.util.ChannelUtil;
import com.yn.reader.util.Constant;
import com.yn.reader.util.GlideImageLoader;

import cn.jpush.android.api.JPushInterface;

/**
 * Created : lts .
 * Date: 2017/12/26
 * Email: lts@aso360.com
 */

public class MiniApp extends Application {
    private static MiniApp application;
    private IWXAPI mIWXAPI;
    private long startTime;

    public static MiniApp getInstance() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        startTime = System.currentTimeMillis();
        leakCanary();
        initUmeng();
        jpush();
        initWeChat();
        initImagePicker();


    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }



    public IWXAPI getIWXAPI() {
        return mIWXAPI;
    }

    /**
     * 将应用注册到微信
     */
    private void initWeChat() {
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constant.WXApp_id, true);
        mIWXAPI.registerApp(Constant.WXApp_id);
    }


    private void leakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void jpush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    private void initUmeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "5a7a6a3ea40fa3448d000161", ChannelUtil.getChannelID()));
    }

    public long getStartTime() {
        return startTime;
    }
}
