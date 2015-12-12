package com.unicorn.csp.xcdemo.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;


public class MenuUtils {

    public static Drawable getSearchIconDrawable(Context context) {
        return new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.WHITE)
                .sizeDp(18);
    }

}
