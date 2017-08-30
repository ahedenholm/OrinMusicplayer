package com.orin.anders.orinmusicplayer;

import android.widget.ImageButton;

public class ButtonController {

    private static final String TAG = "Debug Message";

    //TODO referencing views through statics can cause leaks if activities are changed, since GC
    //cant release anything that keeps referencing a view
    public static ImageButton imageButtonOpen;
    public static ImageButton imageButtonPlay;
    public static ImageButton imageButtonMenu;
    public static ImageButton imageButtonNext;
    public static ImageButton imageButtonPrev;
    public static ImageButton imageButtonStop;
    public static ImageButton imageButtonRepeatMode;

    public static void setImageButtonPauseImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }
    public static void setImageButtonPlayImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }
    public static void setImageButtonShuffleImage(){
        imageButtonRepeatMode.setBackgroundResource(R.drawable.button_shuffle);
    }

}
