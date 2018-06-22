package com.yn.reader.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hysoso.www.utillibrary.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陆贺(QQ1019561500) on 17/2/18.
 */

public class AlipayUtil {
    private static String TAG = AlipayUtil.class.getSimpleName();
    private Context mActivity;
    // 商户PID
    private static final String PARTNER = "2088121869816989";
    // 商户收款账号
    private static final String SELLER = "pay@mhouse.cc";

    private static final int SDK_PAY_FLAG = 1;

    private static Map<String, Object> requestParams = new HashMap<>();
    private static Context mContext;


    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showShort(mContext, "支付成功");
//                        UserInfo.updateUserInfo(mContext,false);
//                        BroadCastUtil.sendBroadcast(mContext, PayBroadReceiver.class,true);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）

                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * get the sdk version. 获取SDK版本号
     */
    public static void getSDKVersion(Context context) {
        PayTask payTask = new PayTask((Activity) context);
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * 支付宝支付
//     *
//     * @param context
//     * @param rechargeOption
//     * @param loading_progress
//     */
//    public static void pay(Context context, RechargeOption rechargeOption, ViewGroup loading_progress) {
//        mContext = context;
//        getOrderInfo(rechargeOption);
//        getSignDeel(context,loading_progress);
//    }
//
//    public static void pay(Context context,
//                           ProductDeal deal,
//                           MFSContentLoadingProgressBar loading_progress) {
//        mContext = context;
//        getOrderInfoBuyGoods(deal);
//        getSignDeelBuyGoods(context,loading_progress);
//    }
//    private static void getSignDeelBuyGoods(final Context context, final MFSContentLoadingProgressBar loading_progress){
//        /**
//         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
//         */
//        HttpUtil.postByXUtil(requestParams, NetAddress.ZHBSIGNBUYGOODS, new HttpUtil.CallBack() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.e(TAG,"onSuccess:"+result);
//                if (!StringUtil.isEmpty(result))gotoZHB(context,result);
//                if (loading_progress!=null)loading_progress.hideProgress();
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                if (loading_progress!=null)loading_progress.hideProgress();
//                ToastUtil.showShort(context,"支付宝支付获取签名订单失败");
//            }
//        });
//    }
//
//    private static void getSignDeel(final Context context, final ViewGroup loading_progress){
//        /**
//         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
//         */
//        HttpUtil.postByXUtil(requestParams, NetAddress.ZHBSIGN, new HttpUtil.CallBack() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.e(TAG,"onSuccess:"+result);
//                Class<?> cls = null;
//                String toastContent = null;
//                try {
//                    if (!StringUtil.isEmpty(result))gotoZHB(context,result);
//
//                    if (toastContent!=null) ToastUtil.showShort(context,toastContent);
//                    if (cls!=null)IntentUtils.startActivity(context,cls);
//                    if (toastContent!=null||cls!=null){
//                        if (loading_progress!=null)loading_progress.setVisibility(View.GONE);
//                    }
//                }catch (Exception e){
//                    LogUtil.e(TAG,"Exception:"+e.getMessage()+"/"+e.getCause());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                if (loading_progress!=null)loading_progress.setVisibility(View.GONE);
//                ToastUtil.showShort(context,"支付宝支付获取签名订单失败");
//            }
//        });
//    }
//
//    private static void gotoZHB(final Context context, final String string) {
//        Runnable payRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // 构造PayTask 对象
//                PayTask alipay = new PayTask((Activity) context);
//                // 调用支付接口，获取支付结果
//                String result = alipay.pay(string, true);
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }
//
//    /**
//     * create the order info. 创建订单信息
//     *
//     */
//    private static String getOrderInfo(RechargeOption rechargeOption) {
//
//        // 签约合作者身份ID
//        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
//        requestParams.put("partner","\""+PARTNER+"\"");
//
//        // 签约卖家支付宝账号
//        orderInfo += "&seller_id=" + "\"" +SELLER + "\"";
//        requestParams.put("seller_id","\""+SELLER+"\"");
//
//        // 商户网站唯一订单号
//        orderInfo += "&out_trade_no=" + "\"" + PayUtil.getOutTradeNo() + "\"";
//        requestParams.put("out_trade_no","\""+PayUtil.getOutTradeNo()+"\"");
//
//        // 商品名称
//        orderInfo += "&subject=" + "\"" + rechargeOption.title + "\"";
//        requestParams.put("subject","\""+rechargeOption.title+"\"");
//        // 商品详情
//        orderInfo += "&body=" + "\"" + rechargeOption.des + "\"";
//        requestParams.put("body","\""+rechargeOption.des+"\"");
//        // 商品金额
//        orderInfo += "&total_fee=" + "\"" + (rechargeOption.price+".00") + "\"";
//        requestParams.put("total_fee","\""+(rechargeOption.price+".00")+"\"");
//
////        orderInfo += "&total_fee=" + "\"" + 0.01 + "\"";
////        requestParams.put("total_fee","\""+0.01+"\"");
//
//        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + NetAddress.ZHBNOTIFY+ "\"";
//        requestParams.put("notify_url","\""+ NetAddress.ZHBNOTIFY+"\"");
//        // 服务接口名称， 固定值
//        orderInfo += "&service=\"mobile.securitypay.pay\"";
//        requestParams.put("service","\""+"mobile.securitypay.pay"+"\"");
//        // 支付类型， 固定值
//        orderInfo += "&payment_type=\"1\"";
//        requestParams.put("payment_type","\""+"1"+"\"");
//        // 参数编码， 固定值
//        orderInfo += "&_input_charset=\"utf-8\"";
//        requestParams.put("_input_charset","\""+"utf-8"+"\"");
//        // 设置未付款交易的超时时间
//        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//        // 取值范围：1m～15d。
//        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//        // 该参数数值不接受小数点，如1.5h，可转换为90m。
//        orderInfo += "&it_b_pay=\"30m\"";
//        requestParams.put("it_b_pay","\""+"30m"+"\"");
//        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";
//        requestParams.put("return_url","\""+"m.alipay.com"+"\"");
//
//        orderInfo += "&user="+"\""+ UserInfo.getVerID(mContext)+"\"";
//        requestParams.put("user","\""+UserInfo.getVerID(mContext)+"\"");
//
//        orderInfo += "&money="+"\""+rechargeOption.price+"\"";
//        requestParams.put("money","\""+rechargeOption.price+"\"");
//
//        orderInfo += "&idType="+"\""+rechargeOption.id+"\"";
//        requestParams.put("idType","\""+rechargeOption.id+"\"");
//
//        orderInfo += "&deviceType=\"android\"";
//        requestParams.put("deviceType","\""+"android"+"\"");
//
//        return orderInfo;
//    }
//
//    /**
//     * create the order info. 创建订单信息
//     *
//     */
//    private static String getOrderInfoBuyGoods(ProductDeal deal) {
//
//        // 签约合作者身份ID
//        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
//        requestParams.put("partner","\""+PARTNER+"\"");
//
//        // 签约卖家支付宝账号
//        orderInfo += "&seller_id=" + "\"" +SELLER + "\"";
//        requestParams.put("seller_id","\""+SELLER+"\"");
//
//        // 商户网站唯一订单号
//        orderInfo += "&out_trade_no=" + "\"" + deal.out_trade_no + "\"";
//        requestParams.put("out_trade_no","\""+ deal.out_trade_no +"\"");
//
//        // 商品名称
//        orderInfo += "&subject=" + "\"" + deal.subject + "\"";
//        requestParams.put("subject","\""+deal.subject+"\"");
//
//
//        // 商品金额
//        orderInfo += "&total_fee=" + "\"" + (deal.total_fee*1.00f/100 ) + "\"";
//        requestParams.put("total_fee","\""+(deal.total_fee*1.00f/100)+"\"");
//
//        // 商品ID
//        orderInfo += "&shopid=" + "\"" + deal.shopid + "\"";
//        requestParams.put("shopid","\""+deal.shopid+"\"");
//
//        // 产品ID
//        orderInfo += "&itemid=" + "\"" + deal.itemid + "\"";
//        requestParams.put("itemid","\""+deal.itemid+"\"");
//
////        orderInfo += "&total_fee=" + "\"" + 0.01 + "\"";
////        requestParams.put("total_fee","\""+0.01+"\"");
//
//        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + NetAddress.ZHBNOTIFY+ "\"";
//        requestParams.put("notify_url","\""+ NetAddress.ZHBNOTIFY+"\"");
//
//        // 服务接口名称， 固定值
//        orderInfo += "&service=\"mobile.securitypay.pay\"";
//        requestParams.put("service","\""+"mobile.securitypay.pay"+"\"");
//        // 支付类型， 固定值
//        orderInfo += "&payment_type=\"1\"";
//        requestParams.put("payment_type","\""+"1"+"\"");
//        // 参数编码， 固定值
//        orderInfo += "&_input_charset=\"utf-8\"";
//        requestParams.put("_input_charset","\""+"utf-8"+"\"");
//        // 设置未付款交易的超时时间
//        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//        // 取值范围：1m～15d。
//        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//        // 该参数数值不接受小数点，如1.5h，可转换为90m。
//        orderInfo += "&it_b_pay=\"30m\"";
//        requestParams.put("it_b_pay","\""+"30m"+"\"");
//        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=";
//        requestParams.put("return_url","");
//
//        orderInfo +=  "&device=" + "\"" + deal.device + "\"";
//        requestParams.put("device","\""+deal.device+"\"");
//
//        orderInfo +=  "&name=" + "\"" + deal.name + "\"";
//        requestParams.put("name","\""+deal.name+"\"");
//
//        orderInfo +=  "&phone=" + "\"" + deal.phone + "\"";
//        requestParams.put("phone","\""+deal.phone+"\"");
//
//        orderInfo +=  "&address=" + "\"" + deal.address + "\"";
//        requestParams.put("address","\""+deal.address+"\"");
//
//        return orderInfo;
//    }

    private static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(String rawResult) {

            if (TextUtils.isEmpty(rawResult))
                return;

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        private String gatValue(String content, String key) {
            String prefix = key + "={";
            return content.substring(content.indexOf(prefix) + prefix.length(),
                    content.lastIndexOf("}"));
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }
}
