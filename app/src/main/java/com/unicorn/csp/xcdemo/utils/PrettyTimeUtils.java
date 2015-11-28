package com.unicorn.csp.xcdemo.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;


public class PrettyTimeUtils {

    public static PrettyTime prettyTime;

    public static String pretty(long time) {
        if (prettyTime == null) {
            prettyTime = new PrettyTime();
        }
        return prettyTime.format(new Date(time));
    }

}
