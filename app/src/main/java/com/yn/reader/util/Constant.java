package com.yn.reader.util;

/**
 * Created : lts .
 * Date: 2017/12/26
 * Email: lts@aso360.com
 */

public final class Constant {

    public static final String DEFAUNT_SEARCH_URL = "http://wap.sogou.com/web/sl?bid=sogou-mobb-6e3adb1ae0e02c93&keyword=";
    public static final String REMMEND_URL = "http://www.crazyddz.com/adsapi/getappstore";
    public static final String KEY_FRAGMENT_TYPE = "key_fragment_type";
    public static final int FRAGMENT_BOOK_LIST = 1001;
    public static final String KEY_FRAGMENT_TITLE = "key_fragment_title";
    public static final int FRAGMENT_CATEGORY_DETAIL = 1002;
    public static final String KEY_JUMP_TYPE = "key_jump_type";
    public static final int JUMP_TYPE_LOGIN = 1001;
    public static final int JUMP_TYPE_READ = 1002;
    public static final String KEY_BOOK_ID = "key_book_id";
    public static final String KEY_ID = "key_id";
    public static String KEY_WORD = "key_word";
    public static String KEY_WORD_ANTHER = "key_word_anther";
    public static final String REGISTER_OR_FORGET_PASSWORD = "key_register_or_forget_password";
    public static final String KEY_MESSAGE_CONTENT = "key_message_content";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_CATEGORY_SEX = "key_category_sex";
    public static String KEY_BOOK = "key_book";
    public static String KEY_INT = "key_int";
    public static String KEY_FLOAT = "key_float";


    public static int uid = 0;
    public static final String PACKAGE_NAME = "com.yn.reader";
    //微信的key
    public static final String WXApp_id = "wx5d0cf9e5a7abd2b6";
//    public static final String WXApp_id = "wxd930ea5d5a258f4f";
    //微信的secret
    public static final String APP_SECRET = "75685c50377fbb7e9cd903cf6246fef6";
    //获取微信token需要的授权码
    public static final String AUTHORIZATION_CODE = "authorization_code";
    //书签的数据库名字
    public static final String MINI_DB = "reader.db";
    //个人资料数据库的key
    public static final long USER_ID = 1111110000L;
    public static final String USER_PROFILE = "userProfile";
    public static final String OPEN_ID = "openid";
    public static final String UNION_ID = "unionid";
    public static final String DEVICE_ID = "device_id";
    public static final String ZX_URL = "zxUrl";
    //广点通的 appid
    public static final String APPID = "1101152570";
    //是否是夜间模式
    public static final String IS_NIGHT_MODE = "IS_NIGHT_MODE";
    //是否无图模式
    public static final String IS_NO_PICTRUE = "IS_NO_PICTRUE";
    //是否无痕模式
    public static final String IS_TRACE_MODE = "IS_TRACE_MODE";
    public static final String TYPE = "TYPE";
    //搜索引擎索引
    public static final String ENGINE_INDEX = "ENGIN_INDEX";
    public static final String WEBSITE_TITLE = "website_title";
    public static final String WEBSITE_URL = "website_url";
    public static final String WEBSITE_ID = "website_id";
    public static final String KEY_CHANNEL_NAME = "key_channel_name";
    public static final String KEY_CHANNEL_ID = "key_channel_id";
    //天气文件
    public static final String WEATHER_IMAGE = "weather_image";
    //是否首次安装
    public static final String IS_FIRST_INSTALL = "IS_FIRST_INSTALL";
    public static final String YEAR_ID = "YEAR_ID";
    public static final String GENDER = "gender";
    public static final String LIKE_LIST = "like_list";
    public static final String KEY_JPUSH_MSG = "key_jpush_msg";
    //baidu地图 经纬度缓存
    public static final String LOCATION_INFO = "LOCATION_INTO";
    //百度地图定位的城市
    public static final String CITY = "CITY";
    public static final int REQUEST_LOGIN = 0x111;
    public static final String KEY_URL = "key_url";
//    public static final String ENGIN_URL = "engin";

    public static String SplashPosID = "8863364436303842593";
    //是否已经设置为全屏模式
    public static String IS_FULLSECREEN = "is_full_secreen";


    //广告类型
    /**
     * 自主广告的开屏id
     */
    public static final int ASOAD = 78;
    public static final int AdBannerID = 1001;    //Banner广告
    public static final int AdInstlID = 1002;   //插屏广告
    public static final int AdSpreadScreenID = 1003;    //开屏广告
    public static final int AdNativeID = 1004;   //原生广告
    public static final int AdVideoID = 1005;    //视频广告

    //广告商
    public static final int IndependentAdID = 1;   //自主投放平台
    public static final int GoogleAdID = 2;  //Google广告平台
    public static final int AdviewAdID = 3;   //Adview广告平台
    public static final int GDTAdID = 4;   //广点通广告平台
    public static final int VungleAdID = 5;   //Vungle广告平台
    public static final int baiduADID = 6;    //百度广告平台
    public static final int UnityAdID = 7;   //Unity广告平台
    public static final String KEY_CHANNEL_LIST = "key_channel_list";
    public static String adPlaceId = "2058622";
    public static final int HOME_TYPE_RECOMMEND = 0, HOME_TYPE_BOY = 1, HOME_TYPE_GIRL = 2;
    public static final String LINK_TYPE_WEB = "web";
    public static final int CHANNEL_TYPE_CHANGE_BATCH = 1;
    public static final int CHANNEL_TYPE_CHANGE_MORE = 2;
    public static final int CHANNEL_TYPE_CHANGE_HOT = 3;

    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_FORGET_PASSWORD = 2;
    public static final int TYPE_MARK_READ = 1, TYPE_DELETE = 2;
    public static final int SORT_TYPE_TIME = 1;
    public static final int SORT_TYPE_HOT = 2;
    public static final int BUY_TYPE_MONTHLY = 1;
    public static final int BUY_TYPE_READ_POINT = 2;

    public static long TIME_DELAY = 200;
    public static int PAY_TYPE_CHAPTER_ID = 1;
    public static int PAY_TYPE_CHAPTER_COUNT = 2;
    public static String[] BOOK_TAG_COLOR = new String[]{
            "#f19ec2", "#1d1778", "#721bbc"
    };
}
