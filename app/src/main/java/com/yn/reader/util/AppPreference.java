package com.yn.reader.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yn.reader.MiniApp;

import java.util.Random;

/**
 * Created by sunxy on 2014/10/31.
 */
public class AppPreference {
    private static final String PREFERENCE_NAME = "all_preference";
    private static SharedPreferences preferences;
    private static AppPreference utils;

    public static AppPreference getInstance() {
        if (utils == null) {
            utils = new AppPreference();
            preferences = MiniApp.getInstance()
                    .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        if (preferences == null) {
            preferences = MiniApp.getInstance()
                    .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return utils;
    }

    public String getChannelID() {
        return preferences.getString("channel_id", "");
    }

    public void setChannelID(String channelID) {
        preferences.edit().putString("channel_id", channelID).apply();
    }

    /**
     * 将String 字符串存储到sp
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    /**
     * 获取sp里面的字符串
     *
     * @param key
     */
    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void setBoolean(String key, Boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void setInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void setAdPlatform(int key) {
        preferences.edit().putInt("adv_platform", key).apply();
    }

    public int getAdPlatform() {
        return preferences.getInt("adv_platform", -1);
    }

    public void setAdAppid(String key1) {
        preferences.edit().putString("ad_app_id", key1).apply();
    }

    public String getAdAppId() {
        return preferences.getString("ad_app_id", "");
    }

    public void setAdid(String key2) {
        preferences.edit().putString("ad_ad_id", key2).apply();
    }

    public String getAdId() {
        return preferences.getString("ad_ad_id", "");
    }

    public int getDarkBrightness() {
        return preferences.getInt("key_brightness", 60);
    }

    public void setNormalBrightness(int brightness) {
        preferences.edit().putInt("key_normal_brightness", brightness).apply();
    }

    public int getNormalBrightness() {
        return preferences.getInt("key_normal_brightness", 100);
    }

    public void setShareUrl(String shareUrl) {
        preferences.edit().putString("key_share_url", shareUrl).apply();
    }

    public String getShareUrl() {
        return preferences.getString("key_share_url", "http://sj.qq.com/myapp/detail.htm?apkName=com.yn.reader");
    }

    public void setShareTitle(String shareUrl) {
        preferences.edit().putString("key_share_title", shareUrl).apply();
    }

    public String getShareTitle() {
        return preferences.getString("key_share_title", "Mini浏览器，极速内核，快速浏览");
    }

    public void setPositionId(int id) {
        preferences.edit().putInt("position_id", id).apply();
    }

    public int getPositionId() {
        return preferences.getInt("position_id", 0);
    }

    public void updateAdBlockCount() {
        int count = getAdBlockCount();
        ++count;
        preferences.edit().putInt("key_adblock", count).apply();
    }

    public int getAdBlockCount() {
        return preferences.getInt("key_adblock", new Random().nextInt(10));
    }

    public boolean isNeedShowInvestigate() {
        return preferences.getBoolean("key_need_show_investigate", true);
    }

    public void investigateShowed() {
        preferences.edit().putBoolean("key_need_show_investigate", false).apply();
    }

    public void setDefaultEnginUrl(String url) {
        preferences.edit().putString("engin", url).apply();
    }

    public String getDefaultEnginUrl() {
        return preferences.getString("engin", Constant.DEFAUNT_SEARCH_URL);
    }

    public void setDefaultHomeChannelCount(int size) {
        preferences.edit().putInt("default_home_channel_count", size).apply();
    }

    public int getDefaultHomeChannelCount() {
        return preferences.getInt("default_home_channel_count", 0);
    }

    public void setLastAsoAdShowTime(long timeMillis) {
        preferences.edit().putLong("last_open_time", timeMillis).apply();
    }

    public long getLastAsoAdShowTime() {
        return preferences.getLong("last_open_time", 0L);
    }

    public void setSearchKeyWord(String keywords) {
        preferences.edit().putString("key_keyword", keywords).apply();
    }

    public String getSearchKeyWord() {
        return preferences.getString("key_keyword", "");
    }

    public void setIsSearchFormSearchBox(boolean b) {
        preferences.edit().putBoolean("search_from_box", b).apply();
    }

    public boolean isSearchFormSearchBox() {
        return preferences.getBoolean("search_from_box", false);
    }

    public String getInjectJs() {
        return preferences.getString("key_inject_js", "");
    }

    public void setInjectJs(String js) {
        preferences.edit().putString("key_inject_js", "javascript:" + js + ";").apply();
    }

    public void setTemperature(String temperature) {
        preferences.edit().putString("key_temperature", temperature).apply();
    }

    public String getTemperature() {
        return preferences.getString("key_temperature", "0");
    }

    public void setWeather(String weather) {
        preferences.edit().putString("key_weather", weather).apply();
    }

    public String getWeather() {
        return preferences.getString("key_weather", "晴");
    }

    public void setWeatherCode(String code) {
        preferences.edit().putString("key_code", code).apply();
    }

    public String getWeatherCode() {
        return preferences.getString("key_code", "0");
    }

    public boolean isNightModel() {
        return preferences.getBoolean("night_model", false);
    }

    public void toggleNightModel() {
        boolean night = isNightModel();
        preferences.edit().putBoolean("night_model", !night).apply();
    }

    public long getUid() {
        return UserInfoManager.getInstance().getUid();
    }

    public void setAccount(String account) {
        preferences.edit().putString("account", account).apply();
    }

    public String getAccount() {
        return preferences.getString("account", "");
    }
}
