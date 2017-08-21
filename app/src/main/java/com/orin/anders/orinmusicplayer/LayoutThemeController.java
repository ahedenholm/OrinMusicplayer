package com.orin.anders.orinmusicplayer;

import android.widget.LinearLayout;

import java.util.Random;

public class LayoutThemeController {
    private Random ran = new Random();
    public static final String THEME_GREENFIELD = "THEME_GREENFIELD";
    public static final String THEME_PURPLE = "THEME_PURPLE";
    public static final String THEME_BLUESKY = "THEME_BLUESKY";
    public static final String THEME_MARINE = "THEME_MARINE";
    public static String currentTheme;

    public void setThemeGreenfield(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_greenfield);
        currentTheme = THEME_GREENFIELD;
    }

    public void setThemePurple(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_purplegrad2);
        currentTheme = THEME_PURPLE;
    }

    public void setThemeBluesky(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_bluesky);
        currentTheme = THEME_BLUESKY;
    }

    public void setThemeMarine(LinearLayout linearLayout){
        linearLayout.setBackgroundResource(R.drawable.bg_marine);
        currentTheme = THEME_MARINE;
    }

    public void setTheme(String theme, LinearLayout linearLayout) {
        switch (theme) {
            case "THEME_GREENFIELD":
                setThemeGreenfield(linearLayout);
                break;
            case "THEME_PURPLE":
                setThemePurple(linearLayout);
                break;
            case "THEME_BLUESKY":
                setThemeBluesky(linearLayout);
                break;
            case "THEME_MARINE":
                setThemeMarine(linearLayout);
                break;
            default:
                break;
        }
    }

}
