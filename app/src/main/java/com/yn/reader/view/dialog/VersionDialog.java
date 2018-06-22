package com.yn.reader.view.dialog;

import android.app.Dialog;
import android.content.Context;

import com.yn.reader.R;

/**
 * Created : lts .
 * Date: 2018/1/9
 * Email: lts@aso360.com
 */

public class VersionDialog extends Dialog {


    public VersionDialog(Context context) {
        this(context, R.style.dialog);

    }

    private void init() {
        setContentView(R.layout.dialog_version);
    }

    public VersionDialog(Context context, int style) {
        super(context, style);
        init();
    }
}
