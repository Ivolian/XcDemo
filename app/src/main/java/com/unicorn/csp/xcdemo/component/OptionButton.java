package com.unicorn.csp.xcdemo.component;

import android.content.Context;
import android.util.AttributeSet;

import com.beardedhen.androidbootstrap.BootstrapButton;

/**
 * Created by Administrator on 2015/11/13.
 */
public class OptionButton extends BootstrapButton {

    public String name;
    public String objectId;
    public int amount;
    public String unit;



    public OptionButton(Context context) {
        super(context);
    }

    public OptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
