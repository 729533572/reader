package com.yn.reader.model.pay;

import com.yn.reader.model.BaseData;

/**
 * Created by luhe on 2018/3/27.
 */

public class PayRequire extends BaseData {
    /*
* 　"appid":"wxb4ba3c02aa476ea1",
　　"partnerid":"1900006771",
　　"package":"Sign=WXPay",
　　"noncestr":"b2e1266e1f996ee4316e9c410d1345d4",
　　"timestamp":1522203764,
　　"prepayid":"wx20180328102244634e428ea40245921175",
　　"sign":"31C8FD38EA76A65A18AED05D78DBDD09"
    * */

    private String appid;
    private String partnerid;
    private String prepayid;
    private String noncestr;
    private String timestamp;
    private String packageValue = "Sign=WXPay";
    private String sign;
    private String extData = "app data";

    @Override
    public int getStatus() {
        return 1;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppid() {
        return appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPrepayid() {
        return prepayid;
    }
}
