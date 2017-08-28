package com.orin.anders.orinmusicplayer;

import android.content.SharedPreferences;
import android.widget.LinearLayout;

public class ThemeController {
    public static final int THEME_GREENFIELD = 1;
    public static final int THEME_PURPLE = 2;
    public static final int THEME_BLUESKY = 3;
    public static final int THEME_MARINE = 4;
    public static Integer currentTheme;

    public SharedPreferences sharedPreferencesTheme;
    public SharedPreferences.Editor sharedPreferencesThemeEditor;


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

    public void setTheme(int theme, LinearLayout linearLayout) {
        switch (theme) {
            case THEME_GREENFIELD:
                setThemeGreenfield(linearLayout);
                break;
            case THEME_PURPLE:
                setThemePurple(linearLayout);
                break;
            case THEME_BLUESKY:
                setThemeBluesky(linearLayout);
                break;
            case THEME_MARINE:
                setThemeMarine(linearLayout);
                break;
            default:
                break;
        }
    }

    public void saveVisualTheme(){
        sharedPreferencesThemeEditor.putInt("savedTheme", ThemeController.currentTheme);
        sharedPreferencesThemeEditor.apply();
    }

}
