package com.orin.anders.orinmusicplayer;

import android.content.Context;
import android.widget.ImageButton;

public class Main_ActivityHelper {

    public static boolean songListIsEnabled;
    public static Context mainActivityContext;
    public static ImageButton imageButtonPlay;

    public static void setImageButtonPauseImage() {
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }

    public static void setImageButtonPlayImage() {
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }
}
