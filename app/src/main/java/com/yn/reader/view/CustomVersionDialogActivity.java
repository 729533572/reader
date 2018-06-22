package com.yn.reader.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.allenliu.versionchecklib.core.VersionDialogActivity;
import com.yn.reader.R;
import com.yn.reader.view.dialog.VersionDialog;

/**
 * Created : lts .
 * Date: 2018/1/9
 * Email: lts@aso360.com
 */

public class CustomVersionDialogActivity extends VersionDialogActivity {


    private VersionDialog mDialog;

    @Override
    protected void showVersionDialog() {
        mDialog = new VersionDialog(this);

        mDialog.findViewById(R.id.iv_version_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.findViewById(R.id.iv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                CustomVersionDialogActivity.super.dealAPK();
            }
        });

        TextView updateMessage = mDialog.findViewById(R.id.updateMessage);
        if(!TextUtils.isEmpty(getVersionUpdateMsg())) {
            updateMessage.setText(getVersionUpdateMsg().replace("\\n", "\n"));
        }

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(this);
        mDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mDialog.dismiss();
    }
}
