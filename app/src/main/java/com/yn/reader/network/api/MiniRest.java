package com.yn.reader.network.api;

import android.app.Activity;

import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.TimeUtil;
import com.socks.library.KLog;
import com.yn.reader.BuildConfig;
import com.yn.reader.MiniApp;
import com.yn.reader.model.statistics.Statistics;
import com.yn.reader.network.converter.FastJsonConverterFactory;
import com.yn.reader.model.BaseData;
import com.yn.reader.model.LogOutResult;
import com.yn.reader.model.LoginResult;
import com.yn.reader.model.WeChatInfo;
import com.yn.reader.model.WeChatToken;
import com.yn.reader.model.adconfig.AdConfigResponse;
import com.yn.reader.model.aso.AsoAdResponse;
import com.yn.reader.model.booklist.BookListResponse;
import com.yn.reader.model.booklist.NavigationCategoryGroup;
import com.yn.reader.model.buy.BuyGroup;
import com.yn.reader.model.category.CategoryGroup;
import com.yn.reader.model.chapter.BookContentGroup;
import com.yn.reader.model.chapter.ChapterGroup;
import com.yn.reader.model.chaptersPrice.ChaptersPrice;
import com.yn.reader.model.comment.CommentResponse;
import com.yn.reader.model.comment.LikeComment;
import com.yn.reader.model.comment.ReportComment;
import com.yn.reader.model.consumptionRecord.ConsumptionRecordGroup;
import com.yn.reader.model.detail.BookDetailGroup;
import com.yn.reader.model.detail.GussYouLikeGroup;
import com.yn.reader.model.favorite.FavoriteDelete;
import com.yn.reader.model.favorite.FavoriteGroup;
import com.yn.reader.model.history.HistoryGroup;
import com.yn.reader.model.home.ChangeBatchGroup;
import com.yn.reader.model.home.HomeGroup;
import com.yn.reader.model.hotSearch.HotSearchGroup;
import com.yn.reader.model.notice.NoticeGroup;
import com.yn.reader.model.password.PasswordBack;
import com.yn.reader.model.pay.PayRequire;
import com.yn.reader.model.pay.PayResult;
import com.yn.reader.model.rechargeRecord.RechargeRecordGroup;
import com.yn.reader.model.register.RegisterResult;
import com.yn.reader.model.searchResult.SearchResultGroup;
import com.yn.reader.model.systemconfig.Config;
import com.yn.reader.util.AppPreference;
import com.yn.reader.util.ChannelUtil;
import com.yn.reader.util.ComUtils;
import com.yn.reader.util.Constant;
import com.yn.reader.util.DeviceUtil;
import com.yn.reader.util.HttpLoggingInterceptor;
import com.yn.reader.util.LogUtils;
import com.yn.reader.util.NetUtil;
import com.yn.reader.util.UserInfoManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * Created by sunxy
 * on 16/8/11.
 */

public class MiniRest {
    public static final String MINI_HOST_END_POINT_UPDATE_VERSION = "http://www.crazyzha.com/";  //更新版本
    public static final String MINI_HOST_STATISTICS = "http://collect.minillq.com/";  //统计
    public static final String MINI_HOST_END_POINT = "http://60.205.253.179/";  //官方建议以/结尾
    public static final String PRIVATE_AGREEMENT = "http://ad.minillq.com/html/ys.html";  //隐私协议
    private static final int DEFAULT_TIMEOUT = 20;
    private static final String MEDIA_TYPE_STRING = "application/x-www-form-urlencoded";
    private static final long WRITE_TIMEOUT = 120;
    private static final long CONNECT_TIMEOUT = 15;
    private static final long CACHE_AGE_SEC = 0;
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;


    private MiniService miniService;
    private OkHttpClient okHttpClient;


    public class BaseMiniInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl url;
            HttpUrl.Builder builder = original.url().newBuilder()
                    .addQueryParameter("c", "android");
            url = builder.build();

            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .url(url)
                    .build();
            return chain.proceed(request);
        }
    }

    //构造方法私有
    private MiniRest() {
//        Cache cache = new Cache(new File(MiniApp.getInstance().getCacheDir(), "MiniCache"), 1024 * 1024 * 100);
        //        手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor(new BaseMiniInterceptor());
        okHttpClientBuilder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
        okHttpClientBuilder.addInterceptor(mRewriteCacheControlInterceptor);
//        okHttpClientBuilder.cache(cache);
        debug(okHttpClientBuilder);

        okHttpClient = okHttpClientBuilder.build();
        //        用于以.cn结尾的域名
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(MINI_HOST_END_POINT)
                .callFactory(okHttpClient)
                .build();
        miniService = retrofit.create(MiniService.class);


    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // 在这里统一配置请求头缓存策略以及响应头缓存策略
            if (NetUtil.isConnected(MiniApp.getInstance())) {
                // 在有网的情况下CACHE_AGE_SEC秒内读缓存，大于CACHE_AGE_SEC秒后会重新请求数据
                request = request.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + CACHE_AGE_SEC).build();
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + CACHE_AGE_SEC).build();
            } else {
                // 无网情况下CACHE_STALE_SEC秒内读取缓存，大于CACHE_STALE_SEC秒缓存无效报504
                request = request.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC).build();
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC).build();
            }

        }
    };

    private void debug(OkHttpClient.Builder httpClientBuilder) {
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
    }

    // 打印返回的json数据拦截器
    private Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            request = requestBuilder.build();

            final Response response = chain.proceed(request);

            KLog.e("请求网址: \n" + request.url() + " \n " + "请求头部信息：\n" + request.headers() + "响应头部信息：\n" + response.headers());

            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    KLog.e("");
                    KLog.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                KLog.d("--------------------------------------------开始打印返回数据----------------------------------------------------");
                KLog.json(buffer.clone().readString(charset));
                KLog.d("--------------------------------------------结束打印返回数据----------------------------------------------------");
            }

            return response;
        }
    };

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final MiniRest INSTANCE = new MiniRest();
    }

    //获取单例
    public static MiniRest getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取首页配置
     *
     * @return
     */
    public Observable<HomeGroup> getHomePageInfo(int type) {

        return miniService.getHomePageInfo(type, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()));
    }

    /**
     * 获取换一批
     *
     * @return
     */
    public Observable<ChangeBatchGroup> changeBatch(int homechannelid, int pagesize, int current_page) {
        return miniService.changeBatch(homechannelid, pagesize, current_page);
    }

    /**
     * 获取书籍详情
     *
     * @return
     */
    public Observable<BookDetailGroup> getBookDetailInfo(long bookId) {
        return miniService.getBookDetailInfo(bookId, AppPreference.getInstance().getUid());

    }

    /**
     * 获取书籍详情-猜你喜欢
     *
     * @return
     */
    public Observable<GussYouLikeGroup> gussYouLike(long bookId) {
        return miniService.gussYouLike(bookId, 3, 1);

    }

    /**
     * 获取书籍分类
     *
     * @return
     */
    public Observable<CategoryGroup> getHotCategory(int sex) {
        return miniService.getHotCategory(sex, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()));

    }

    /**
     * 获取书籍收藏
     *
     * @return
     */
    public Observable<HistoryGroup> getHistory(int currentPage) {

        return miniService.getHistory(AppPreference.getInstance().getUid(), currentPage, 20, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()));
    }

    /**
     * 获取书籍历史
     *
     * @return
     */
    public Observable<FavoriteGroup> getFavorite(int currentPage) {

        return miniService.getFavorite(AppPreference.getInstance().getUid(), currentPage, 20, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()));
    }


    /**
     * 获取章节列表
     *
     * @return
     */
    public Observable<ChapterGroup> getChapters(long bookId) {
        return miniService.getChapters(bookId, AppPreference.getInstance().getUid());
    }


    public Observable<BookContentGroup> getBookContent(long chapterid) {
        long time = System.currentTimeMillis() / 1000;

        String bodyString =
                "chapterid=" + chapterid +
                        "&userid=" + AppPreference.getInstance().getUid() +
                        "&package=" + Constant.PACKAGE_NAME +
                        "&version=" + BuildConfig.VERSION_NAME +
                        "&token=" + getBookContentToken(chapterid, time) +
                        "&time=" + time;

        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.getBookContent(body);
    }

    private String getBookContentToken(long chapterid, long time) {
        return ComUtils.encode2hex(chapterid + ComUtils.encode2hex(Constant.PACKAGE_NAME + BuildConfig.VERSION_NAME + time));
    }

    /**
     * 发送短信验证码
     * type:1注册 2：找回密码
     *
     * @return
     */
    public Observable<BaseData> getSmsCode(Activity activity, String phone, int type) {
        String imei = DeviceUtil.getIMEI(activity);
        String version = ComUtils.getVersionName(activity);
        return miniService.getSmsCode(phone, imei, Constant.PACKAGE_NAME, version, getSmsToken(phone, imei, version), type);
    }


    /**
     * 注册
     *
     * @return
     */
    public Observable<RegisterResult> register(Activity activity, String phone, String verifycode, String password, String repassword) {
        String imei = DeviceUtil.getIMEI(activity);
        String version = ComUtils.getVersionName(activity);

        String bodyString = "phone=" + phone
                + "&idfa=" + imei + "&package=" + Constant.PACKAGE_NAME
                + "&version=" + BuildConfig.VERSION_NAME + "&token=" + getSmsToken(phone, imei, version) + "&verifycode=" + verifycode
                + "&password=" + password + "&repassword=" + repassword;
        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.register(body);
    }

    /**
     * 注册
     *
     * @return
     */
    public Observable<PasswordBack> forgetPassword(Activity activity, String phone, String verifycode, String password, String repassword) {
        String imei = DeviceUtil.getIMEI(activity);
        String version = ComUtils.getVersionName(activity);

        String bodyString = "phone=" + phone
                + "&idfa=" + imei + "&package=" + Constant.PACKAGE_NAME
                + "&version=" + BuildConfig.VERSION_NAME + "&token=" + getSmsToken(phone, imei, version) + "&verifycode=" + verifycode
                + "&password=" + password + "&repassword=" + repassword;
        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.forgetPassword(body);
    }

    /**
     * 登录
     *
     * @return
     */
    public Observable<LoginResult> login(String phone, String password) {
        String bodyString = "username=" + phone + "&password=" + password;
        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.login(body);
    }

    /**
     * 添加收藏
     *
     * @return
     */
    public Observable<BaseData> addFavorite(String favoriteJson) {
        //TODO:上传失败
        String bodyString =
                "package=" + Constant.PACKAGE_NAME +
                        "&version=" + BuildConfig.VERSION_NAME +
                        "&data=" + favoriteJson;
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.addFavorite(body);
    }

//    /**
//     * 获取用户信息
//     *
//     * @return
//     */
//    public Observable<UserInitializedInfo> getUserInfo() {
//        String bodyString = "userid=" + AppPreference.getInstance().getUid() + "&key=" + AppPreference.getInstance().getKey();
//        LogUtils.log("bodyString=" + bodyString);
//        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);
//
//        return miniService.getUserInfo(body);
//    }


    private String getSmsToken(String phone, String imei, String version) {
        return ComUtils.encode2hex(ComUtils.encode2hex(phone) + imei + Constant.PACKAGE_NAME + version);
    }


    /**
     * 获取微信token
     *
     * @param code 微信临时授权票据
     * @return
     */
    public Observable<WeChatToken> getWechatToken(String code) {
        return miniService.getWeChatToken(Constant.WXApp_id, Constant.APP_SECRET, code, Constant.AUTHORIZATION_CODE);
    }

    public Observable<WeChatInfo> getWeChatInfo(String toke, String openid) {
        return miniService.getWeChatInfo(toke, openid);
    }

    public Observable<BaseData> markToReadOrDelete(int type, String messageid) {
        String bodyString = "type=" + type + "&messageid=" + messageid;
        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);
        return miniService.markToReadOrDelete(body);
    }

    /**
     * 登录
     *
     * @param openid  用户唯一标示
     * @param unionid unionid
     * @param IMEI    设备IMEI号
     * @param token   token
     * @param source  source *
     *                1 个人中心直接登录
     *                2 个人中心点击消费记录之后登录
     *                3 个人中心点击充值记录之后登录
     *                4 阅读页需要付费时登录
     * @return
     */


    public Observable<LoginResult> Login(String openid, String unionid, String IMEI, String time, String token
            , String avatar, String nickname, int sex, String province, String city, String country, int source) {

        String bodyString = "openid=" + openid
                + "&unionid=" + unionid + "&package=" + Constant.PACKAGE_NAME
                + "&version=" + BuildConfig.VERSION_NAME + "&idfa=" + IMEI + "&time=" + time + "&token=" + token
                + "&avatar=" + avatar + "&nickname=" + nickname + "&sex=" + sex + "&province=" + province + "&city=" + city + "&country=" + country + "&source=" + source
                + "&sysname=" + DeviceUtil.getOsverName() + "&devtype=" + android.os.Build.MODEL + "&wifiname=" + DeviceUtil.getWifiName(MiniApp.getInstance()) + "&devicename=" + DeviceUtil.getDeviceModel()
                + "&appname=" + ComUtils.getAppName();
        LogUtils.log("bodyString=" + bodyString);

        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);
        return miniService.WxLogin(body);
    }

    /**
     * 根据分类获取数据
     *
     * @return
     */
    public Observable<BookListResponse> getBooksByCategory(int categoryid, int sex, int pagesize, int pageno, Integer status, Integer chargetype, Integer lastnevigate, Integer word, String tags) {
        return miniService.getBooksByCategory(Constant.PACKAGE_NAME, BuildConfig.VERSION_NAME, categoryid, sex, pagesize, pageno, status, chargetype, lastnevigate, word, tags);
    }

    /**
     * 根据分类获取数据
     *
     * @return
     */
    public Observable<BookListResponse> getBooksByCategory(int categoryid, int sex, int pagesize, int pageno) {
        return miniService.getBooksByCategory(Constant.PACKAGE_NAME, BuildConfig.VERSION_NAME, categoryid, sex, pagesize, pageno);
    }

    /**
     * 根据分类获取数据
     *
     * @return
     */
    public Observable<NavigationCategoryGroup> getNavigationCategory(int categoryid) {
        return miniService.getNavigationCategory(Constant.PACKAGE_NAME, BuildConfig.VERSION_NAME, categoryid);
    }


    /**
     * 退出登录
     *
     * @return
     */
    public Observable<LogOutResult> logOut() {
        return miniService.logOut();
    }

    /**
     * 修改用户资料
     *
     * @return
     */

    public Observable<LoginResult> upDataUrerProfile(RequestBody body) {
        return miniService.upDateUserProfile(body);
    }

    /**
     * 修改用户资料
     *
     * @return
     */

    public Observable<LoginResult> upDataUserInfo(String key, String value) {
        //TODO:返回值key没有了

        String bodyString = key + "=" + value +
                "&key=" + UserInfoManager.getInstance().getKey() +
                "&userid=" + UserInfoManager.getInstance().getUid() +
                "&appversion=" + BuildConfig.VERSION_NAME +
                "&wifiname=" + DeviceUtil.getWifiName(MiniApp.getInstance()) +
                "&idfa=" + AppPreference.getInstance().getString(Constant.DEVICE_ID) +
                "&sysname=" + DeviceUtil.getOsverName() +
                "&devtype=" + android.os.Build.MODEL +
                "&bid=" + Constant.PACKAGE_NAME +
                "&devicename=" + DeviceUtil.getDeviceModel() +
                "&appname=" + ComUtils.getAppName();
        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);
        return miniService.upDateUserProfile(body);
    }

    /**
     * 修改用户资料
     *
     * @return
     */

    public Observable<LoginResult> upDataUserInfoFile(String key, File file) {
        AppPreference ap = AppPreference.getInstance();
        RequestBody build = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(key, file == null ? null : file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("key", UserInfoManager.getInstance().getKey())
                .addFormDataPart("userid", String.valueOf(AppPreference.getInstance().getUid()))
                .addFormDataPart("appversion", BuildConfig.VERSION_NAME)
                .addFormDataPart("wifiname", DeviceUtil.getWifiName(MiniApp.getInstance()))
                .addFormDataPart("idfa", ap.getString(Constant.DEVICE_ID))
                .addFormDataPart("sysname", DeviceUtil.getOsverName())
                .addFormDataPart("devtype", android.os.Build.MODEL)
                .addFormDataPart("bid", Constant.PACKAGE_NAME)
                .addFormDataPart("devicename", DeviceUtil.getDeviceModel())
                .addFormDataPart("appname", ComUtils.getAppName())
                .build();
        return miniService.upDateUserProfile(build);
    }

    /**
     * 获取用户以及系统消息
     *
     * @return
     */
    public Observable<NoticeGroup> getNotice(int type) {
        String bodyString = "userid=" + AppPreference.getInstance().getUid() + "&type=" + type;
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.getNotice(body);
    }

    public Observable<CommentResponse> getComments(long bookId, int currentPage, int sortType, int pageSize) {
        return miniService.getComments(bookId, AppPreference.getInstance().getUid(), sortType, pageSize, currentPage);
    }

    public Observable<LikeComment> likeComment(int commentId) {
        String bodyString = "userid=" + (UserInfoManager.getInstance().isLanded() ? UserInfoManager.getInstance().getUid() : "")
                + "&commentid=" + commentId
                + "&package=" + Constant.PACKAGE_NAME
                + "&version=" + ComUtils.getVersionName(MiniApp.getInstance().getApplicationContext());
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);
        return miniService.likeComment(body);
    }

    public Observable<ReportComment> report(int commentId) {
        String bodyString = "idfa=" + AppPreference.getInstance().getString(Constant.DEVICE_ID)
                + "&sysname=" + DeviceUtil.getOsverName()
                + "&devtype=" + android.os.Build.MODEL
                + "&bid=" + Constant.PACKAGE_NAME
                + "&appversion=" + BuildConfig.VERSION_NAME
                + "&wifiname=" + DeviceUtil.getWifiName(MiniApp.getInstance())
                + "&devicename=" + DeviceUtil.getDeviceModel()
                + "&appname=" + ComUtils.getAppName()
                + "&commentid=" + commentId;
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);
        return miniService.reportComment(body);
    }

    /**
     * 浏览器首页元素点击记录事件
     */
    public Observable<BaseData> clickEvent(String type) {
        return miniService.clickEvent(type, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 激活统计
     */
    public Observable<BaseData> appActive() {
        return miniService.appActive(DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.isCharging(MiniApp.getInstance()), DeviceUtil.hasSimCard(), 1, DeviceUtil.getDeviceModel());
    }

    /**
     * 搜索统计
     */
    public Observable<BaseData> searchRecord(String keywords) {
        return miniService.searchRecord(keywords, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 自家广告点击统计
     */
    public Observable<BaseData> asoAdClickRecord(String adid, int positionId) {
        return miniService.asoAdClickRecord(adid, positionId, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 自家广告请求统计
     */
    public Observable<BaseData> asoAdRequestRecord(int positionId) {
        return miniService.asoAdRequestRecord(positionId, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 自家广告请求结果统计
     */
    public Observable<BaseData> asoAdRequestResultRecord(int type, String adid, int positionId) {
        return miniService.asoAdRequestResultRecord(type, adid, positionId, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }


    /**
     * 广告点击统计
     */
    public Observable<BaseData> adClickRecord(int type, int adprovider, int postion) {
        return miniService.adClickRecord(type, adprovider, postion, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 广告请求统计
     */
    public Observable<BaseData> adRequestRecord(int type, int adprovider, int postion) {
        return miniService.adRequestRecord(type, adprovider, postion, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 广告请求结果统计
     */
    public Observable<BaseData> adRequestResultRecord(int type, int adtype, int adprovider, int postion) {
        return miniService.adRequestResultRecord(type, adtype, adprovider, postion, DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 获取系统配置
     */
    public Observable<Config> getSystemConfig() {
        return miniService.getSystemConfig(Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()));
    }


    /**
     * 获取广告配置
     *
     * @return
     */
    public Observable<AdConfigResponse> getAdConfig() {
        return miniService.getAdConfig(DeviceUtil.getIMEI(MiniApp.getInstance()), DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(), DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }


    /**
     * 获取自主广告信息
     *
     * @return
     */
    public Observable<AsoAdResponse> getAsoSplash(int positionId) {

        return miniService.getAsoSplash(positionId, DeviceUtil.getIMEI(MiniApp.getInstance()),
                DeviceUtil.getOsverName(), android.os.Build.MODEL, Constant.PACKAGE_NAME,
                ComUtils.getVersionName(MiniApp.getInstance()), ComUtils.getAppName(),
                DeviceUtil.getWifiName(MiniApp.getInstance()), DeviceUtil.getDeviceModel());
    }

    /**
     * 发表书评
     *
     * @return
     */
    public Observable<BaseData> sendBookComment(long bookId, String content) {

        String bodyString = "bookid=" + bookId + "&content=" + content
                + "&userid=" + AppPreference.getInstance().getUid() + "&package=" + Constant.PACKAGE_NAME
                + "&version=" + BuildConfig.VERSION_NAME;
        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.sendBookComment(body);
    }

    /**
     * 获取热搜关键字
     *
     * @return
     */
    public Observable<HotSearchGroup> getHotSearchKeyWords() {
        return miniService.getHotSearchKeyWords();
    }

    /**
     * 搜书
     *
     * @return
     */
    public Observable<SearchResultGroup> searchBookByKeyword(String keyword, int pageno, int pagesize) {
        return miniService.searchBookByKeyword(
                keyword,
                pageno,
                pagesize,
                UserInfoManager.getInstance().isLanded() ? String.valueOf(UserInfoManager.getInstance().getUid()) : "",
                Constant.PACKAGE_NAME,

                DeviceUtil.getOsverName(),
                android.os.Build.MODEL,
                DeviceUtil.getWifiName(MiniApp.getInstance()),
                DeviceUtil.getDeviceModel(),
                ComUtils.getAppName(),

                ComUtils.getVersionName(MiniApp.getInstance()),
                DeviceUtil.getIMEI(MiniApp.getInstance())
        );
    }

    /**
     * 获取充值历史记录
     *
     * @return
     */
    public Observable<RechargeRecordGroup> getRechargeRecord() {
        return miniService.getRechargeRecord(AppPreference.getInstance().getUid());
    }

    /**
     * 获取消费历史记录
     *
     * @return
     */
    public Observable<ConsumptionRecordGroup> getConsumptionRecord() {
        return miniService.getConsumptionRecord(AppPreference.getInstance().getUid());
    }

    /**
     * 获取包月购买或阅读点购买页面信息
     *
     * @return
     */
    public Observable<BuyGroup> getBuyChoices(int buyType) {
        return miniService.getBuyChoices(buyType, Constant.PACKAGE_NAME, ComUtils.getVersionName(MiniApp.getInstance()));
    }


    /**
     * 购买章节
     *
     * @param bookId       书id
     * @param bookname     书名
     * @param type         购买类型 1,选择章节id购买;2,选择章节数量购买
     * @param nowchapterid 当前章节id
     * @param allchapterid 所有需要购买的章节id 当type=1时该字段不能为空，当type=2时该字段可为空
     * @param chaptertype  需要购买的章节数量 当type=2时该字段不能为空，type=1时该字段可为空
     * @return
     */
    public Observable<PayResult> payChapter(
            long bookId,
            String bookname,
            int type,
            long nowchapterid,
            String allchapterid,
            Integer chaptertype) {

        String bodyString =
                "bookname=" + bookname +
                        "&bookid=" + bookId +
                        "&type=" + type +
                        "&nowchapterid=" + nowchapterid +
                        (allchapterid != null ? ("&allchapterid=" + allchapterid) : "") +
                        (chaptertype != null ? ("&chaptertype=" + chaptertype) : "") +

                        "&userid=" + AppPreference.getInstance().getUid() +
                        "&package=" + Constant.PACKAGE_NAME +
                        "&version=" + BuildConfig.VERSION_NAME +
                        "&appname=" + ComUtils.getAppName() +
                        "&wifiname=" + DeviceUtil.getWifiName(MiniApp.getInstance()) +
                        "&devicename=" + DeviceUtil.getDeviceModel() +
                        "&devtype=" + android.os.Build.MODEL +
                        "&sysname=" + DeviceUtil.getOsverName() +
                        "&idfa=" + DeviceUtil.getIMEI(MiniApp.getInstance().getApplicationContext());

        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.payChapter(body);
    }

    /**
     * 获取购买的章节的价格
     *
     * @param bookId       书id
     * @param type         购买类型 1,选择章节id购买;2,选择章节数量购买
     * @param nowchapterid 当前章节id
     * @param allchapterid 所有需要购买的章节id 当type=1时该字段不能为空，当type=2时该字段可为空
     * @param chaptertype  需要购买的章节数量 当type=2时该字段不能为空，type=1时该字段可为空
     * @return
     */
    public Observable<ChaptersPrice> getChaptersPrice(
            long bookId,
            int type,
            long nowchapterid,
            String allchapterid,
            Integer chaptertype) {

        String bodyString =
                "bookid=" + bookId +
                        "&type=" + type +
                        "&nowchapterid=" + nowchapterid +
                        (allchapterid != null ? ("&allchapterid=" + allchapterid) : "") +
                        (chaptertype != null ? ("&chaptertype=" + chaptertype) : "") +
                        "&userid=" + AppPreference.getInstance().getUid();

        LogUtils.log("bodyString=" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.getChaptersPrice(body);
    }

    public Observable<PayRequire> pay() {
        return miniService.pay();
    }

    public Observable<FavoriteDelete> deleteFavorites(String bookIds) {
        return miniService.deleteFavorites(bookIds, AppPreference.getInstance().getUid(), Constant.PACKAGE_NAME, BuildConfig.VERSION_NAME);
    }

    /**
     * type
     * datetime
     * idfa
     * uuid
     * app_name
     * app_version
     * <p>
     * package_name
     * platform
     * product_type
     * channel
     * device_name
     * <p>
     * phone_type
     * system_version
     * user_ip
     * network_type
     * wifiname
     **/
    private String getReportBaseLog() {
        String dateTime = String.valueOf(Long.parseLong(TimeUtil.getTimeTag(TimeUtil.getDataTime(null), null)) / 1000);
        String private_key = "4u89";
        NetUtil.NetWorkType netWorkType = NetUtil.getNetworkType(MiniApp.getInstance().getApplicationContext());

        String reportBaseLog = "idfa=" + DeviceUtil.getIMEI(MiniApp.getInstance().getApplicationContext())
                + "&timestamp=" + dateTime
                + "&sign=" + ComUtils.encode2hex(dateTime + private_key)
                + "&app_name=" + ComUtils.getAppName()
                + "&app_version=" + BuildConfig.VERSION_NAME

                + "&package_name=" + Constant.PACKAGE_NAME
                + "&platform=" + "Android"
                + "&product_type=" + "browser"
                + "&channel=" + ChannelUtil.getChannelID()
                + "&device_name=" + DeviceUtil.getDeviceName()

                + "&phone_type=" + DeviceUtil.getDeviceModel()
                + "&system_version=" + DeviceUtil.getOsverName()
                + "&uid=" + (UserInfoManager.getInstance().isLanded() ? UserInfoManager.getInstance().getUid() : "")
                + "&network_type=" + netWorkType.value
                + "&wifiname=" + (netWorkType.value.equals("wifi") ? NetUtil.getConnectWifiName(MiniApp.getInstance().getApplicationContext()) : "");

        LogUtils.log("reportBaseLog=" + reportBaseLog);

        return reportBaseLog;
    }

    /**
     * adprovider 广告商 1自主 2谷歌 3adview 4广点通 5vungle 6百度 7unity
     * positionid 广告位id
     * adtype 广告类型 1001Banner 1002插屏 1003开屏 1004原生 1005视频
     * adid 广告id
     * <p>
     * 广告点击记录统计接口日志
     **/

    public Observable<Statistics> advertisementClickStatistics(
            int adprovider,
            int positionid,
            int adtype,
            String adid) {

        String bodyString = getReportBaseLog()
                + "&adprovider=" + adprovider
                + "&positionid=" + positionid
                + "&adtype=" + adtype
                + "&adid=" + adid;

        LogUtil.i("Statistics", "advertisementClickStatistics:" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.advertisementClickStatistics(body);
    }

    /**
     * result_type 结果类型  1请求成功 2请求失败 3展示成功 4展示失败
     * adprovider 广告商 1自主 2谷歌 3adview 4广点通 5vungle 6百度 7unity
     * positionid 广告位id
     * adtype 广告类型 1001Banner 1002插屏 1003开屏 1004原生 1005视频
     * adid 广告id
     * <p>
     * 广告请求结果统计接口日志
     **/

    public Observable<Statistics> advertisementRequestStatistics(
            int result_type,
            int adprovider,
            int positionid,
            int adtype,
            String adid) {

        String bodyString = getReportBaseLog()
                + "&result_type=" + result_type
                + "&adprovider=" + adprovider
                + "&positionid=" + positionid
                + "&adtype=" + adtype
                + "&adid=" + adid;

        LogUtil.i("Statistics", "advertisementRequestStatistics:" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.advertisementRequestStatistics(body);
    }

    /**
     * adprovider 广告商 1自主 2谷歌 3adview 4广点通 5vungle 6百度 7unity
     * positionid 广告位id
     * adid 广告id
     * <p>
     * 广告跳过统计接口日志
     **/

    public Observable<Statistics> advertisementSkipStatistics(
            int adprovider,
            int positionid,
            String adid) {

        String bodyString = getReportBaseLog()
                + "&adprovider=" + adprovider
                + "&positionid=" + positionid
                + "&adid=" + adid;

        LogUtil.i("Statistics", "advertisementSkipStatistics:" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.advertisementSkipStatistics(body);
    }

    /**
     * param1 预留
     * param2 预留
     * <p>
     * 新增日活行为
     **/

    public Observable<Statistics> newlyAddedStatistics() {

        String bodyString = getReportBaseLog();

        LogUtil.i("Statistics", "newlyAddedStatistics:" + bodyString);
        RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE_STRING), bodyString);

        return miniService.newlyAddedStatistics(body);
    }

}