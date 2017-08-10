package com.orin.anders.orinmusicplayer;

import android.widget.LinearLayout;

import java.util.Random;

public class LayoutThemeController {
    Random ran = new Random();


    public void setThemeField(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_greenfield);
    }

    public void setThemeBG2(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_purplegrad);
    }

    public void setThemeBG3(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_bluesky);
    }

    public void setThemeBG4(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_marine);
    }

    public void setThemeRandom(LinearLayout linearLayout) {
        switch (ran.nextInt(4)) {
            case 0:
                linearLayout.setBackgroundResource(R.drawable.bg_greenfield);
                break;
            case 1:
                linearLayout.setBackgroundResource(R.drawable.bg_purplegrad);
                break;
            case 2:
                linearLayout.setBackgroundResource(R.drawable.bg_bluesky);
                break;
            case 3:
                linearLayout.setBackgroundResource(R.drawable.bg_marine);
                break;
            default:
                break;
        }
    }


}
