package com.unicorn.csp.xcdemo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;


public class DrawableUtils {

    public static Drawable getIconDrawable(Context context,IIcon iIcon, @ColorInt int color, int size){
          return new IconicsDrawable(context)
                .icon(iIcon)
                .color(color)
                .sizeDp(size);

    }

}
