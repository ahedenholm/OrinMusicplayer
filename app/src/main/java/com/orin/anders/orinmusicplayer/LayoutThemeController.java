package com.orin.anders.orinmusicplayer;

import android.widget.LinearLayout;

import java.util.Random;

public class LayoutThemeController {
    private Random ran = new Random();
    private int themeID;

    public void setThemeField(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_greenfield);
        themeID = 1;
    }

    public void setThemeBG2(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_purplegrad);
        themeID = 2;
    }

    public void setThemeBG3(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_bluesky);
        themeID = 3;
    }

    public void setThemeBG4(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_marine);
        themeID = 4;
    }

    public void setThemeRandom(LinearLayout linearLayout) {
        switch (ran.nextInt(4)) {
            case 0:
                linearLayout.setBackgroundResource(R.drawable.bg_greenfield);
                themeID = 1;
                break;
            case 1:
                linearLayout.setBackgroundResource(R.drawable.bg_purplegrad);
                themeID = 2;
                break;
            case 2:
                linearLayout.setBackgroundResource(R.drawable.bg_bluesky);
                themeID = 3;
                break;
            case 3:
                linearLayout.setBackgroundResource(R.drawable.bg_marine);
                themeID = 4;
                break;
            default:
                break;
        }
    }

    public int getThemeID(){
        return themeID;
    }

}
