package com.yn.reader.network.api;

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
import com.yn.reader.model.statistics.Statistics;
import com.yn.reader.model.systemconfig.Config;
import com.yn.reader.model.userInfo.UserInitializedInfo;
import com.yn.reader.model.weather.Weather;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.yn.reader.network.api.MiniRest.MINI_HOST_STATISTICS;

/**
 * Created by sunxy on 2017/5/18.
 * <p>
 * 网络请求接口
 */

public interface MiniService {

    //获取首页配置
    @GET("novelapi/gethomeconfig")
    Observable<HomeGroup> getHomePageInfo(
            @Query("type") int type
            , @Query("package") String package_name
            , @Query("version") String version);

    //获取首页换一批
    @GET("novelapi/gethomechannelbooks")
    Observable<ChangeBatchGroup> changeBatch(
            @Query("homechannelid") int homechannelid
            , @Query("pagesize") int pagesize
            , @Query("pageno") int pageno);

    //获取书籍详情
    @GET("novelapi/getbookdetail")
    Observable<BookDetailGroup> getBookDetailInfo(
            @Query("bookid") long bookid
            , @Query("userid") long userid
    );

    //猜你喜欢
    @GET("novelapi/getbookdetailguesslikes")
    Observable<GussYouLikeGroup> gussYouLike(
            @Query("bookid") long bookid
            , @Query("pagesize") int pagesize
            , @Query("pageno") int pageno
    );

    //分类
    @GET("novelapi/getcategory")
    Observable<CategoryGroup> getHotCategory(
            @Query("sex") int sex
            , @Query("package") String package_name
            , @Query("version") String version);

    //收藏
    @GET("novelapi/getfavorite")
    Observable<FavoriteGroup> getFavorite(
            @Query("userid") long userid
            , @Query("pageno") int pageno
            , @Query("pagesize") int pagesize
            , @Query("package") String package_name
            , @Query("version") String version);

    //历史
    @GET("novelapi/gethistory")
    Observable<HistoryGroup> getHistory(
            @Query("userid") long userid
            , @Query("pageno") int pageno
            , @Query("pagesize") int pagesize
            , @Query("package") String package_name
            , @Query("version") String version);

    //获取书籍章节列表
    @GET("novelapi/getbookchapter")
    Observable<ChapterGroup> getChapters(
            @Query("bookid") long bookid
            , @Query("userid") long userid
    );

    @GET("novelapi/sendsms")
    Observable<BaseData> getSmsCode(
            @Query("phone") String phone
            , @Query("idfa") String idfa
            , @Query("package") String packageName
            , @Query("version") String version
            , @Query("token") String token
            , @Query("type") int type);


    /**
     * 注册
     */

    @POST("novelapi/register")
    Observable<RegisterResult> register(@Body RequestBody body);

    /**
     * 忘记密码
     */

    @POST("novelapi/forgetpassword")
    Observable<PasswordBack> forgetPassword(@Body RequestBody body);

    /**
     * 登录
     */
    @POST("novelapi/login")
    Observable<LoginResult> login(@Body RequestBody body);

    /**
     * 添加修改收藏
     */
    @POST("novelapi/updatefavorite")
    Observable<BaseData> addFavorite(@Body RequestBody body);

    /**
     * 获取章节内容
     */
    @POST("novelapi/getbookcontentbychapter")
    Observable<BookContentGroup> getBookContent(@Body RequestBody body);


    /**
     * 获取用户信息
     */
    @POST("novelapi/getuserinfo")
    Observable<UserInitializedInfo> getUserInfo(@Body RequestBody body);

    /**
     * 标记已读或者删除
     */
    @POST("novelapi/updateusermessage")
    Observable<BaseData> markToReadOrDelete(@Body RequestBody body);


    /**
     * 获取消息
     */
    @POST("novelapi/getusermessage")
    Observable<NoticeGroup> getNotice(@Body RequestBody body);

    @GET("browserapi/recordnewsclick")
    Observable<BaseData> newsItemClickEvent(
            @Query("messageid") String messageid
            , @Query("userid") int uid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("ischarging") int ischarging
            , @Query("issiminstalled") int issiminstalled
            , @Query("adtrackingenabled") int adtrackingenabled
            , @Query("devicename") String devicename
    );

    @GET("novelapi/getbookcomments")
    Observable<CommentResponse> getComments(
            @Query("bookid") long bookid
            , @Query("userid") long userid
            , @Query("type") int type
            , @Query("pagesize") int pagesize
            , @Query("pageno") int pageno
    );

    /**
     * 评论点赞
     */
    @POST("novelapi/favoritebookcomment")
    Observable<LikeComment> likeComment(@Body RequestBody body);

    /**
     * 评论举报
     */
    @POST("novelapi/recordreport")
    Observable<ReportComment> reportComment(@Body RequestBody body);


    @GET("novelapi/getbooksbycategory")
    Observable<BookListResponse> getBooksByCategory(
            @Query("package") String packageName
            , @Query("version") String version
            , @Query("categoryid") int categoryid
            , @Query("sex") int sex
            , @Query("pagesize") int pagesize
            , @Query("pageno") int pageno
            , @Query("status") Integer status
            , @Query("chargetype") Integer chargetype
            , @Query("lastnevigate") Integer lastnevigate
            , @Query("word") Integer word
            , @Query("tags") String tags
    );

    @GET("novelapi/getbooksbycategory")
    Observable<BookListResponse> getBooksByCategory(
            @Query("package") String packageName
            , @Query("version") String version
            , @Query("categoryid") int categoryid
            , @Query("sex") int sex
            , @Query("pagesize") int pagesize
            , @Query("pageno") int pageno
    );

    @GET("novelapi/getcategorytopnevigate")
    Observable<NavigationCategoryGroup> getNavigationCategory(
            @Query("package") String packageName
            , @Query("version") String version
            , @Query("categoryid") int categoryid
    );

    /**
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     *
     * @param appid      在微信开发平台申请的key
     * @param secret     微信开发平台生成的secret
     * @param code       微信票据
     * @param grant_type 授权码
     * @return wechatToekn
     */
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<WeChatToken> getWeChatToken(@Query("appid") String appid,
                                           @Query("secret") String secret,
                                           @Query("code") String code,
                                           @Query("grant_type") String grant_type);

    /**
     * 获取微信资料
     *
     * @param token  微信返回的token
     * @param openid 微信的openid
     * @return @see WechatInfo
     */
    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<WeChatInfo> getWeChatInfo(@Query("access_token") String token,
                                         @Query("openid") String openid);

    /**
     * 微信登录
     */

    @POST("novelapi/wxlogin")
    Observable<LoginResult> WxLogin(@Body RequestBody body);

    /**
     * 退出登录
     *
     * @return
     */
    @GET("browseruser/logout")
    Observable<LogOutResult> logOut();


    /**
     * 获取系统配置
     *
     * @return
     */
    @GET("ddz/systemconfig")
    Observable<Config> getSystemConfig(
            @Query("package") String packageName
            , @Query("version") String version);


    /**
     * 点击事件统计
     *
     * @return
     */
    @GET("browserapi/recordmainpageclick")
    Observable<BaseData> clickEvent(
            @Query("type") String type
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 激活统计
     *
     * @return
     */
    @GET("ddz/activation")
    Observable<BaseData> appActive(
            @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("ischarging") int ischarging
            , @Query("issiminstalled") int issiminstalled
            , @Query("adtrackingenabled") int adtrackingenabled
            , @Query("devicename") String devicename

    );

    /**
     * 搜索统计
     *
     * @return
     */
    @GET("browserapi/recordsearch")
    Observable<BaseData> searchRecord(
            @Query("keyword") String keyword
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 自家广告点击统计
     *
     * @return
     */
    @GET("http://crazyddz.com/adsapi/recordadclick")
    Observable<BaseData> asoAdClickRecord(
            @Query("adid") String adid
            , @Query("positionid") int positionid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 自家广告请求结果统计
     *
     * @return
     */
    @GET("http://crazyddz.com/adsapi/recordadrequestresult")
    Observable<BaseData> asoAdRequestResultRecord(
            @Query("type") int type
            , @Query("adid") String adid
            , @Query("positionid") int positionid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 自家广告请求统计
     *
     * @return
     */
    @GET("http://crazyddz.com/adsapi/ad")
    Observable<BaseData> asoAdRequestRecord(
            @Query("positionid") int positionid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 广告请求统计
     *
     * @return
     */
    @GET("http://crazyddz.com/api/recordadrequest")
    Observable<BaseData> adRequestRecord(
            @Query("adtype") int adtype
            , @Query("adprovider") int adprovider
            , @Query("position") int positionid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 广告请求统计
     *
     * @return
     */
    @GET("http://crazyddz.com/api/recordadclick")
    Observable<BaseData> adClickRecord(
            @Query("adtype") int adtype
            , @Query("adprovider") int adprovider
            , @Query("position") int positionid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );

    /**
     * 广告请求统计
     *
     * @return
     */
    @GET("http://crazyddz.com/api/recordadrequestresult")
    Observable<BaseData> adRequestResultRecord(
            @Query("type") int type
            , @Query("adtype") int adtype
            , @Query("adprovider") int adprovider
            , @Query("position") int positionid
            , @Query("idfa") String idfa
            , @Query("sysname") String sysname
            , @Query("devtype") String devtype
            , @Query("bid") String bid
            , @Query("appversion") String appversion
            , @Query("appname") String appname
            , @Query("wifiname") String wifiname
            , @Query("devicename") String devicename

    );


    /**
     * 修改用户资料
     *
     * @return
     */

    @POST("novelapi/updateuserinfo")
    Observable<LoginResult> upDateUserProfile(@Body RequestBody body);


    /**
     * 获取广告配置信息
     *
     * @return
     */
    @GET("http://crazyddz.com/api/getpositionlist")
    Observable<AdConfigResponse> getAdConfig(@Query("idfa") String idfa,
                                             @Query("sysname") String sysname,
                                             @Query("devtype") String devtype,
                                             @Query("bid") String bid,
                                             @Query("appversion") String appversion,
                                             @Query("appname") String appname,
                                             @Query("wifiname") String wifiname,
                                             @Query("devicename") String devicename

    );

    /**
     * 获取自主广告信息
     *
     * @return
     */
    @GET("http://crazyddz.com/adsapi/ad")
    Observable<AsoAdResponse> getAsoSplash(@Query("positionid") int positionid,
                                           @Query("idfa") String idfa,
                                           @Query("sysname") String sysname,
                                           @Query("devtype") String devtype,
                                           @Query("bid") String bid,
                                           @Query("appversion") String appversion,
                                           @Query("appname") String appname,
                                           @Query("wifiname") String wifiname,
                                           @Query("devicename") String devicename);


    /**
     * 获取天气信息
     *
     * @param lonlat 经纬度
     * @param city   城市
     * @return
     */
    @GET("browserapi/getweather")
    Observable<Weather> getWeathe(@Query("lonlat") String lonlat, @Query("city") String city);

    /**
     * 发表书评
     *
     * @return
     */

    @POST("novelapi/commentbookdetail")
    Observable<BaseData> sendBookComment(@Body RequestBody body);


    /**
     * 获取热搜关键字
     *
     * @return
     */
    @GET("novelapi/getsearchhotkeywords")
    Observable<HotSearchGroup> getHotSearchKeyWords();


    /**
     * 搜索
     *
     * @param keyword     关键字
     * @param pageno      请求的第几页
     * @param pagesize    请求一页多少数据
     * @param userid      用户id
     * @param packageName 包名
     * @param sysname     系统名
     * @return
     */
    @GET("novelapi/getsearch")
    Observable<SearchResultGroup> searchBookByKeyword(
            @Query("keyword") String keyword,
            @Query("pageno") int pageno,
            @Query("pagesize") int pagesize,
            @Query("userid") String userid,
            @Query("package") String packageName,

            @Query("sysname") String sysname,
            @Query("devtype") String devtype,
            @Query("wifiname") String wifiname,
            @Query("devicename") String devicename,
            @Query("appname") String appname,

            @Query("version") String version,
            @Query("idfa") String idfa
    );

    /**
     * 获取充值记录
     *
     * @param userid 用户id
     * @return
     */
    @GET("novelapi/getpayhistorylist")
    Observable<RechargeRecordGroup> getRechargeRecord(@Query("userid") long userid);

    /**
     * 获取消费记录
     *
     * @param userid 用户id
     * @return
     */
    @GET("novelapi/getshophistorylist")
    Observable<ConsumptionRecordGroup> getConsumptionRecord(@Query("userid") long userid);

    /**
     * 获取包月购买或阅读点购买页面信息
     *
     * @param type 1,获取包月购买页面;2,获取阅读点购买页面
     * @return
     */
    @GET("novelapi/getvipcoinpricelist")
    Observable<BuyGroup> getBuyChoices(
            @Query("type") int type,
            @Query("package") String packageName,
            @Query("version") String version
    );

    /**
     * 购买章节
     *
     * @return
     */

    @POST("novelapi/paychapter")
    Observable<PayResult> payChapter(@Body RequestBody body);

    /**
     * 获取章节价格
     *
     * @return
     */
    @POST("novelapi/getchapterprice")
    Observable<ChaptersPrice> getChaptersPrice(@Body RequestBody body);

    @GET("http://wxpay.wxutil.com/pub_v2/app/app_pay.php")
    Observable<PayRequire> pay();

    /**
     * 批量删除书架
     *
     * @param bookid
     * @return
     */
    @GET("novelapi/deletefavorite")
    Observable<FavoriteDelete> deleteFavorites(
            @Query("bookid") String bookid,
            @Query("userid") long userid,
            @Query("package") String packageName,
            @Query("version") String version
    );

    /**
     * @param body
     * @return 新增日活行为
     */
    @POST(MINI_HOST_STATISTICS + "browser/activation")
    Observable<Statistics> newlyAddedStatistics(@Body RequestBody body);

    /**
     * @param body
     * @return 广告点击记录统计接口日志
     */
    @POST(MINI_HOST_STATISTICS + "advertisement/click")
    Observable<Statistics> advertisementClickStatistics(@Body RequestBody body);

    /**
     * @param body
     * @return 广告请求结果统计接口日志
     */
    @POST(MINI_HOST_STATISTICS + "advertisement/request")
    Observable<Statistics> advertisementRequestStatistics(@Body RequestBody body);

    /**
     * @param body
     * @return 广告跳过统计接口日志
     */
    @POST(MINI_HOST_STATISTICS + "advertisement/skip")
    Observable<Statistics> advertisementSkipStatistics(@Body RequestBody body);
}
