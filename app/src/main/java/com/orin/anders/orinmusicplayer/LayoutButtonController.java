package com.orin.anders.orinmusicplayer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class LayoutButtonController {

    LinearLayout linearLayout;
    private static final String TAG = "Debug Message";

    private Activity activity;
    private Context context;
    public static ImageButton imageButtonOpen;
    public static ImageButton imageButtonPlay;
    public static ImageButton imageButtonMenu;
    public static ImageButton imageButtonNext;
    public static ImageButton imageButtonPrev;
    public static ImageButton imageButtonStop;

    public LayoutButtonController(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void initiateImageButtons(){
        imageButtonOpen = (ImageButton)activity.findViewById(R.id.imageButtonOpen);
        imageButtonPlay = (ImageButton)activity.findViewById(R.id.imageButtonPlay);
        imageButtonMenu = (ImageButton)activity.findViewById(R.id.imageButtonMenu);
        imageButtonNext = (ImageButton)activity.findViewById(R.id.imageButtonNext);
        imageButtonPrev = (ImageButton)activity.findViewById(R.id.imageButtonPrev);
        imageButtonStop = (ImageButton)activity.findViewById(R.id.imageButtonStop);
    }

    public ImageButton getImageButtonOpen() {
        return imageButtonOpen;
    }

    public ImageButton getImageButtonPlay() {
        return imageButtonPlay;
    }

    public ImageButton getImageButtonMenu() {
        return imageButtonMenu;
    }

    public ImageButton getImageButtonNext() {
        return imageButtonNext;
    }

    public ImageButton getImageButtonPrev() {
        return imageButtonPrev;
    }

    public ImageButton getImageButtonStop() {
        return imageButtonStop;
    }

    public static void setImageButtonPauseImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }
    public static void setImageButtonPlayImage(){
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }

}
