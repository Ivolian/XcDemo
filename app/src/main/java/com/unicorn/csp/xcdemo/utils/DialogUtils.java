package com.unicorn.csp.xcdemo.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;


public class DialogUtils {

    public static MaterialDialog showIndeterminateDialog(Context context, String title, String content) {

        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .show();
    }

}
