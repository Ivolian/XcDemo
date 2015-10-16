package com.unicorn.csp.xcdemo.utils;

import android.widget.EditText;


public class EditTextUtils {

    public static String getValue(EditText editText){

       return editText.getText().toString().trim();
    }

    public static boolean isEmpty(EditText editText){

        return getValue(editText).equals("");
    }

}
