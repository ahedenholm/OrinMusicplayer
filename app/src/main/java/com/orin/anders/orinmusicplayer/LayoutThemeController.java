package com.orin.anders.orinmusicplayer;

import android.widget.LinearLayout;

import java.util.Random;

public class LayoutThemeController {
    Random ran = new Random();


    public void setThemeField(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.orin_bg1_720x1280);
    }

    public void setThemeBG2(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.orin_bg2v2_720x1280);
    }

    public void setThemeBG3(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.orin_bg3_720x1280);
    }

    public void setThemeBG4(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.orin_bg4_720x1280);
    }

    public void setThemeRandom(LinearLayout linearLayout) {
        int a = ran.nextInt(4);
        switch (a) {
            case 1:
                linearLayout.setBackgroundResource(R.drawable.orin_bg1_720x1280);
                break;
            case 2:
                linearLayout.setBackgroundResource(R.drawable.orin_bg2v2_720x1280);
                break;
            case 3:
                linearLayout.setBackgroundResource(R.drawable.orin_bg3_720x1280);
                break;
            case 0:
                linearLayout.setBackgroundResource(R.drawable.orin_bg4_720x1280);
                break;
        }
    }


}
