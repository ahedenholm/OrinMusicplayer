package com.orin.anders.orinmusicplayer.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DeviceScreenCheck {

    static DisplayMetrics displayMetrics = new DisplayMetrics();

    public static int[] getDeviceHeightAndWidthArray(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] array = {displayMetrics.widthPixels, displayMetrics.heightPixels};
        return array;
    }

    public static int getDeviceHeight(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getDeviceWidth(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


}

