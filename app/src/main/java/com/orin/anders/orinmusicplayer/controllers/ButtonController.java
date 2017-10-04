package com.orin.anders.orinmusicplayer.controllers;

import android.widget.ImageButton;

import com.orin.anders.orinmusicplayer.R;

public class ButtonController {

    private static final String TAG = "ButtonController.Debug Message";

    //TODO referencing views through statics can cause leaks if activities are changed, since GC
    //cant release anything that keeps referencing a view
    public static ImageButton imageButtonPlay;
    public static ImageButton imageButtonOpen;
    public static ImageButton imageButtonNext;
    public static ImageButton imageButtonPrev;
    public static ImageButton imageButtonStop;
    public static ImageButton imageButtonSwitchtheme;
    public static ImageButton imageButtonPlaybackMode;

    public static void setImageButtonsToNull(){
        ButtonController.imageButtonPlay = null;
        ButtonController.imageButtonOpen = null;
        ButtonController.imageButtonNext = null;
        ButtonController.imageButtonPrev = null;
        ButtonController.imageButtonStop = null;
        ButtonController.imageButtonSwitchtheme = null;
        ButtonController.imageButtonPlaybackMode = null;
    }

    public static void setImageButtonPlayImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }
    public static void setImageButtonPauseImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }
    public static void setImageButtonShuffleImage(){
        imageButtonPlaybackMode.setBackgroundResource(R.drawable.button_shuffle);
    }
    public static void setImageButtonRepeatAllImage(){
        imageButtonPlaybackMode.setBackgroundResource(R.drawable.button_repeatall);
    }
    public static void setImageButtonRepeatOneImage(){
        imageButtonPlaybackMode.setBackgroundResource(R.drawable.button_repeatone);
    }

}
