package com.yn.reader.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.yn.reader.MiniApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取设备信息
 * Created by sunxy on 2017/7/27.
 */

public class DeviceUtil {

    /**
     * 获得系统亮度
     *
     * @return
     */
    public static int getSystemBrightness(Activity activity) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            AppPreference.getInstance().setNormalBrightness(systemBrightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 改变App当前Window亮度
     *
     * @param brightness
     */
    public static void changeAppBrightness(Activity activity,int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }
    /**
//     * 设置亮度
//     *
//     * @param activity
//     * @param brightness
//     */
//    public static void setBrightness(Activity activity, float brightness) {
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        AppPreference.getInstance(activity).setNormalBrightness(lp.screenBrightness);
//        lp.screenBrightness = brightness * (1f / 255f);
//        activity.getWindow().setAttributes(lp);
//
//        //保存亮度设置
//        Uri uri = android.provider.Settings.System
//                .getUriFor("screen_brightness");
//        ContentResolver resolver=activity.getContentResolver();
//        android.provider.Settings.System.putInt(resolver, "screen_brightness",
//                (int)brightness);
//        resolver.notifyChange(uri, null);
//    }
  /*
  * 获取手机名称
  * */
    public static String getDeviceName() {
        return Build.MANUFACTURER;
    }
    /**
     * 获取手机唯一标识
     *
     * @return uuid
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String deviceId = telephonyManager.getDeviceId();
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String uuid;
        if (!TextUtils.isEmpty(deviceId)) {
            uuid = deviceId;
        } else {
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = androidId;
            } else {
                uuid = UUID.randomUUID().toString();
            }
        }
        LogUtils.log("uuid=" + uuid);
        return uuid;
    }

    /**
     * 获取渠道Id
     *
     * @return
     */
    public static String getChannelID() {
        return ChannelUtil.getChannelID();
    }

    public static String getSystemName() {
        return "Android";
    }

    public static String getOsverName() {
        return Build.VERSION.RELEASE;
    }

    public static String getOsversionCode() {
        return Build.VERSION.SDK;
    }

    /*
   * 获取手机型号
   * */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getWifiName(Context context) {
        String wifiName = "";
        if (NetworkUtil.isWifiConnected()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            //去掉特殊字符
            wifiName = wifiInfo.getSSID();
            String regs = "([^\\u4e00-\\u9fa5\\w\\(\\)（）])+?";
            Pattern pattern = Pattern.compile(regs);
            Matcher matcher = pattern.matcher(wifiName);
            wifiName=matcher.replaceAll("");
        }

        LogUtils.log("wifi name=" + wifiName);
        if(!TextUtils.isEmpty(wifiName)){
            wifiName=wifiName.replace("<","");
            wifiName=wifiName.replace(">","");
            if(wifiName.equals("<unknown ssid>"))
                wifiName="unknown";
        }
        return wifiName;
    }

    public static int isCharging(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        if (isCharging)
            return 1;
        return 0;
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static int hasSimCard() {
        Context context = MiniApp.getInstance();
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        int result = 1;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = 0; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = 1;
                break;
        }
        return result;
    }



    public static String getResolution() {
        Resources resources=MiniApp.getInstance().getResources();
        DisplayMetrics metrics=resources.getDisplayMetrics();
        return metrics.heightPixels+"x"+metrics.widthPixels;
    }

    public static String getOperator(Activity activity) {

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();

        if (operatorString == null) {
            return "未知";
        }

        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            //中国移动
            return "中国移动";
        } else if (operatorString.equals("46001")) {
            //中国联通
            return "中国联通";
        } else if (operatorString.equals("46003")) {
            //中国电信
            return "中国电信";
        }

        //error
        return "未知";

    }



    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getMacAddress(Context context) {
        WifiManager wifiMan = (WifiManager)context.getSystemService(Context.WIFI_SERVICE) ;
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf !=null && marshmallowMacAddress.equals(wifiInf.getMacAddress())){
            String result = null;
            try {
                result= getAdressMacByInterface();
                if (result != null){
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else{
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }

    public static String getAndroidId(Activity activity) {
        return  Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
