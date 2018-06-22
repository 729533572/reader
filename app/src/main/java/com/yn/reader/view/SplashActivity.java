package com.yn.reader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.bumptech.glide.Glide;
import com.hysoso.www.utillibrary.LogUtil;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;
import com.yn.reader.MiniApp;
import com.yn.reader.R;
import com.yn.reader.mvp.presenters.SplashPresenter;
import com.yn.reader.mvp.views.SplashView;
import com.yn.reader.model.adconfig.AdConfig;
import com.yn.reader.model.aso.AsoAdResponse;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.Constant;
import com.yn.reader.util.LogUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created : lts .
 * Date: 2018/1/4
 * Email: lts@aso360.com
 */
public class SplashActivity extends Activity implements SplashView {

    private static final int ASO_AD_REQUEST_CODE = 1001;
    private static final String SKIP_TEXT = "跳过 %d";
    private ViewGroup container;
    private TextView skipView;
    private View splashHolder;
    private SplashPresenter splashPresenter;
    private String appId, adId;
    private CountDownTimer countDownTimer;
    private SplashAd splashAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresenter = new SplashPresenter(this);
        //开屏
        container = this.findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        splashHolder = findViewById(R.id.splash_holder);
        skipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int platform = AppPreference.getInstance().getAdPlatform();
                try {
                    splashPresenter.advertisementSkipStatistics(
                            platform,
                            AppPreference.getInstance().getPositionId(),
                            adId);

                } catch (Exception ex) {
                    LogUtil.e(getClass().getSimpleName(), ex.getMessage() + "/" + ex.getCause());
                }
                goHome(1);
            }
        });
        launchEvent();
        if (System.currentTimeMillis() - MiniApp.getInstance().getStartTime() < 10 * 1000 && (System.currentTimeMillis() - AppPreference.getInstance().getLastAsoAdShowTime()) > 1000 * 60 * 60) {

        AppPreference.getInstance().setLastAsoAdShowTime(System.currentTimeMillis());

        splashPresenter.loadAdConfig();

        appId = AppPreference.getInstance().getAdAppId();
        adId = AppPreference.getInstance().getAdId();
        int platform = AppPreference.getInstance().getAdPlatform();

        if (platform != -1) {

            switch (platform) {
                case Constant.IndependentAdID:
                    LogUtils.log("delata time=" + (System.currentTimeMillis() - MiniApp.getInstance().getStartTime()));
                    splashPresenter.getAsoSplash(AppPreference.getInstance().getPositionId());
                    break;
                case Constant.GDTAdID:
                    if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(adId)) {
                        showGdtSplash();
                    } else {
                        goHome(2);
                    }
                    break;

                case Constant.baiduADID:
                    if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(adId)) {
                        AdView.setAppSid(SplashActivity.this, appId);
                        showBaiduSplash();
                    } else {
                        goHome(3);
                    }
                    break;
                default:
                    goHome(4);
            }
        } else {
            goHome(5);
        }
        splashPresenter.newlyAddedStatistics();

        } else {
            goHome(6);
        }
    }

    private void launchEvent() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        LogUtils.log("hour=" + hour);
        if (hour <= 2) {
            MobclickAgent.onEvent(this, "LaunchTime_0_2");
        } else if (hour <= 4) {
            MobclickAgent.onEvent(this, "LaunchTime_2_4");
        } else if (hour <= 6) {
            MobclickAgent.onEvent(this, "LaunchTime_4_6");
        } else if (hour <= 8) {
            MobclickAgent.onEvent(this, "LaunchTime_6_8");
        } else if (hour <= 10) {
            MobclickAgent.onEvent(this, "LaunchTime_8_10");
        } else if (hour <= 12) {
            MobclickAgent.onEvent(this, "LaunchTime_10_12");
        } else if (hour <= 14) {
            MobclickAgent.onEvent(this, "LaunchTime_12_14");
        } else if (hour <= 16) {
            MobclickAgent.onEvent(this, "LaunchTime_14_16");
        } else if (hour <= 18) {
            MobclickAgent.onEvent(this, "LaunchTime_16_18");
        } else if (hour <= 20) {
            MobclickAgent.onEvent(this, "LaunchTime_18_20");
        } else if (hour <= 22) {
            MobclickAgent.onEvent(this, "LaunchTime_20_22");
        } else if (hour <= 24) {
            MobclickAgent.onEvent(this, "LaunchTime_22_24");
        }
    }

    /**
     * 启动主页
     */
    private void goHome(int i) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
        finish();
    }

    /**
     * 显示自主广告
     */
    private void showAsoSplash(final AsoAdResponse asoAd) {
        adId = asoAd.getData().getAdid();
        skipView.setVisibility(View.VISIBLE);

        splashHolder.setVisibility(View.GONE); // 广告展示后一定要把预设的开屏图片隐藏起来

        LogUtil.i(getClass().getSimpleName(), "广告总时间：" + asoAd.getData().getDuration());
        countDown(asoAd.getData().getDuration());

        //展示成功
        splashPresenter.adRecord(
                SplashPresenter.ASO_AD_REQUEST_RESULT,
                SplashPresenter.PRESENT_SUCCESS,
                asoAd.getData().getAdid(),
                AppPreference.getInstance().getPositionId(),
                Constant.IndependentAdID);

        View view = LayoutInflater.from(this).inflate(R.layout.ad_aso, container, false);
        ImageView adImageView = view.findViewById(R.id.adImage);
        container.addView(view);
        KLog.d(asoAd.getData().getImg_vertical());
        Glide.with(this).load(asoAd.getData().getImg_vertical()).into(adImageView);

        adImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                //点击统计
                splashPresenter.adRecord(SplashPresenter.ASO_AD_CLICK, Constant.AdSpreadScreenID, asoAd.getData().getAdid()
                        , AppPreference.getInstance().getPositionId(), Constant.IndependentAdID);
                startDefaultBrowser(asoAd);
            }
        });


    }


    /**
     * CountDownTimer 实现倒计时
     */
    private void countDown(int duration) {
        skipView.setText(String.format(SKIP_TEXT, duration));

        countDownTimer = new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                LogUtil.i(getClass().getSimpleName(), "广告剩余时间：" + time);
                skipView.setText(String.format(SKIP_TEXT, time));
            }

            @Override
            public void onFinish() {
                skipView.setText(String.format(SKIP_TEXT, 0));
                next();
            }
        };
        countDownTimer.start();
    }

    /**
     * 启动默认浏览器
     *
     * @param asoAd
     */
    private void startDefaultBrowser(AsoAdResponse asoAd) {
        Intent intent = new Intent(this, SharedWebActivity.class);
        intent.putExtra(Constant.KEY_URL, asoAd.getData().getLink());
        startActivityForResult(intent, ASO_AD_REQUEST_CODE);

    }


    /**
     * 显示Baidu开屏
     */
    private void showBaiduSplash() {
        splashAd = new SplashAd(this, container, listener, adId, true);
    }

    /**
     * 显示广点通开屏
     */
    private void showGdtSplash() {
        fetchSplashAD(this, container, skipView, appId, adId, mSplashADListener, 3000);
    }


    /**
     * 百度广告的监听
     */
    private SplashAdListener listener = new SplashAdListener() {
        @Override
        public void onAdDismissed() {
            Log.i("RSplashActivity", "onAdDismissed");
            jumpWhenCanClick(); // 跳转至您的应用主界面
        }

        @Override
        public void onAdFailed(String arg0) {
            Log.i("RSplashActivity", "onAdFailed");
            splashPresenter.adResultRecord(SplashPresenter.REQUEST_FAIL, Constant.AdSpreadScreenID
                    , AppPreference.getInstance().getPositionId(),
                    Constant.baiduADID);

            goHome(6);
        }


        @Override
        public void onAdPresent() {
            splashPresenter.adResultRecord(SplashPresenter.PRESENT_SUCCESS, Constant.AdSpreadScreenID
                    , AppPreference.getInstance().getPositionId(),
                    Constant.baiduADID);
            Log.i("RSplashActivity", "onAdPresent");
            splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
        }


        @Override
        public void onAdClick() {
            Log.i("RSplashActivity", "onAdClick");
            // 设置开屏可接受点击时，该回调可用
            splashPresenter.adRecord(SplashPresenter.AD_CLICK, Constant.AdSpreadScreenID, AppPreference.getInstance().getAdId()
                    , AppPreference.getInstance().getPositionId(), Constant.baiduADID);
        }
    };

    /**
     * 广点通的广告监听
     */
    private SplashADListener mSplashADListener = new SplashADListener() {
        @Override
        public void onADDismissed() {
            Log.i("RSplashActivity", "onAdDismissed");
            next();
        }

        @Override
        public void onNoAD(AdError adError) {
            KLog.d("adError code " + adError.getErrorCode() + "aderrorMessage" + adError.getErrorMsg());
            splashPresenter.adResultRecord(SplashPresenter.REQUEST_FAIL, Constant.AdSpreadScreenID
                    , AppPreference.getInstance().getPositionId(),
                    Constant.GDTAdID);
            goHome(7);
        }

        @Override
        public void onADPresent() {
            skipView.setVisibility(View.VISIBLE);
            splashPresenter.adResultRecord(SplashPresenter.PRESENT_SUCCESS, Constant.AdSpreadScreenID
                    , AppPreference.getInstance().getPositionId(),
                    Constant.GDTAdID);
            splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
        }

        @Override
        public void onADClicked() {
            Log.i("RSplashActivity", "onADClicked");
            splashPresenter.adRecord(SplashPresenter.AD_CLICK, Constant.AdSpreadScreenID, AppPreference.getInstance().getAdId()
                    , AppPreference.getInstance().getPositionId(), Constant.GDTAdID);
        }

        @Override
        public void onADTick(long l) {
            skipView.setText(String.format(SKIP_TEXT, Math.round(l / 1000f)));
        }
    };

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                       String appId, String posId, SplashADListener adListener, int fetchDelay) {
        new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public void onAdConfigLoaded(List<AdConfig> adConfigs) {
        for (AdConfig adConfig : adConfigs) {
            //开屏
            if (adConfig != null && adConfig.getType() == Constant.AdSpreadScreenID) {
                AppPreference.getInstance().setPositionId(adConfig.getId());

                List<AdConfig.KeyInfo> order = adConfig.getOrder();

                if (order != null) {
                    AdConfig.KeyInfo keyInfo = order.get(0);
                    if (keyInfo != null) {
                        if (!TextUtils.isEmpty(keyInfo.getKey1())) {
                            AppPreference.getInstance().setAdAppid(keyInfo.getKey1());
                        }
                        if (!TextUtils.isEmpty(keyInfo.getKey2())) {
                            AppPreference.getInstance().setAdid(keyInfo.getKey2());
                        }
                        AppPreference.getInstance().setAdPlatform(keyInfo.getType());
                    }
                }
            }
        }
    }

    @Override
    public void onAsoSplashLoaded(AsoAdResponse asoAd) {

        //请求成功
        splashPresenter.adRecord(
                SplashPresenter.ASO_AD_REQUEST_RESULT,
                SplashPresenter.REQUEST_SUCCESS,
                asoAd.getData().getAdid(),
                AppPreference.getInstance().getPositionId(),
                Constant.IndependentAdID);

        showAsoSplash(asoAd);
    }

    @Override
    public void onAsoSplashLoadFailed() {
        //请求失败
        splashPresenter.adRecord(SplashPresenter.ASO_AD_REQUEST_RESULT, SplashPresenter.REQUEST_FAIL, AppPreference.getInstance().getAdId()
                , AppPreference.getInstance().getPositionId(), Constant.IndependentAdID);
        goHome(8);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ASO_AD_REQUEST_CODE) {
            goHome(9);
        }
    }

    /**
     * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加canJumpImmediately判断。 另外，点击开屏还需要在onResume中调用jumpWhenCanClick接口。
     */
    public boolean canJumpImmediately = false;

    private void jumpWhenCanClick() {
        Log.d("test", "this.hasWindowFocus():" + this.hasWindowFocus());
        if (canJumpImmediately) {
            goHome(10);
        } else {
            canJumpImmediately = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //百度
        canJumpImmediately = false;
        //广点通
        canJump = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //百度
        if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;

        //广点通
        if (canJump) {
            next();
        }
        canJump = true;
    }


    public boolean canJump = false;

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        if (canJump) {
            goHome(11);
        } else {
            canJump = true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashAd != null) {
            splashAd.destroy();
        }
    }
}
