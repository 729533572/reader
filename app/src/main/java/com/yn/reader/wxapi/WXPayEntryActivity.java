package com.yn.reader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hysoso.www.utillibrary.LogUtil;
import com.hysoso.www.utillibrary.StringUtil;
import com.hysoso.www.utillibrary.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yn.reader.util.Constant;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private String TAG = getClass().getSimpleName();

    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WXApp_id);
        api.registerApp(Constant.WXApp_id);

        api.handleIntent(getIntent(),this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        String code = String.valueOf(baseReq.transaction);
        LogUtil.e(TAG,"onReq:"+code);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String code = String.valueOf(baseResp.errCode);
        LogUtil.e(TAG,"onResp:"+code+"/"+baseResp.errStr);
        String toastContent =null;
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (code.equals("0")) {
                toastContent  = "支付成功";
                //TODO:
            }else if (code.equals("-1")) {
                toastContent  = "支付失败";
            }else if (code.equals("-2")) {
                toastContent  = "交易放弃";
            }
            if (!StringUtil.isEmpty(toastContent)) ToastUtil.showShort(this,toastContent);
            finish();
        }
    }
}
