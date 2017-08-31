package com.orin.anders.orinmusicplayer;

import android.app.Activity;
import android.content.Context;

public class Main_ActivityHelper {

    public static boolean songListIsEnabled;

    //Used to access findviewbyid from outside activity classes, might be fine as
    //long as app only uses one activity and variables are nulled at onDestroy()
    public static Activity activity;
    public static Context context;
}
