package com.orin.anders.orinmusicplayer;

import android.media.MediaPlayer;
import android.widget.ImageButton;

public class LayoutButtonController {

    MediaPlayer mediaPlayer;

    public void initListener(){

    }
    public void buttonPause(ImageButton imageButton){
        imageButton.setBackgroundResource(R.drawable.button_pause);
    }
    public void buttonPlay(ImageButton imageButton){
        imageButton.setBackgroundResource(R.drawable.button_play);
    }
}
