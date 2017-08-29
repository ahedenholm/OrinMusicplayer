package com.orin.anders.orinmusicplayer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class ButtonController {

    private static final String TAG = "Debug Message";

    public static ImageButton imageButtonOpen;
    public static ImageButton imageButtonPlay;
    public static ImageButton imageButtonMenu;
    public static ImageButton imageButtonNext;
    public static ImageButton imageButtonPrev;
    public static ImageButton imageButtonStop;

    public static void setImageButtonPauseImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }
    public static void setImageButtonPlayImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }

}
