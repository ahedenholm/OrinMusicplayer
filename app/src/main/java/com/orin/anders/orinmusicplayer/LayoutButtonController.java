package com.orin.anders.orinmusicplayer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class LayoutButtonController extends AppCompatActivity {

    LinearLayout linearLayout;
    private static final String TAG = "Debug Message";

    private ImageButton imageButtonOpen;
    private ImageButton imageButtonPlay;
    private ImageButton imageButtonMenu;
    private ImageButton imageButtonNext;
    private ImageButton imageButtonPrev;
    private ImageButton imageButtonStop;

    public void setImageButtonPlay(ImageButton imageButtonPlay) {

    }

    public void buttonPauseImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }
    public void buttonPlayImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }

}
