package com.yn.reader.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.allenliu.versionchecklib.core.AVersionService;
import com.yn.reader.MiniApp;
import com.yn.reader.R;
import com.yn.reader.model.systemconfig.Config;
import com.yn.reader.util.LogUtils;

/**
 * Created : lts .
 * Date: 2018/1/9
 * Email: lts@aso360.com
 */

public class CheckVersionService extends AVersionService {


    @Override
    public void onResponses(AVersionService service, String response) {
        try {
            Config systemConfig = JSON.parseObject(response, Config.class);

            if (systemConfig.isBUpdate()) {

                service.showVersionDialog(
                        systemConfig.getUpdateUrl(),
                        MiniApp.getInstance().getString(R.string.update_title),
                        systemConfig.getUpdateDes());
            }
        }catch (JSONException exception){
            LogUtils.log("CheckVersionService JSONException");
        }



    }
}
