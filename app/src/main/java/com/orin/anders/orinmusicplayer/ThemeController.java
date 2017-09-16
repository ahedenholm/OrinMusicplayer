package com.orin.anders.orinmusicplayer;

import android.content.SharedPreferences;
import android.widget.RelativeLayout;

public class ThemeController {
    public static final int THEME_GREENFIELD = 0;
    public static final int THEME_PURPLE = 1;
    public static final int THEME_SKYBLUE = 2;
    public static final int THEME_MARINE = 3;
    public static Integer currentTheme;

    public SharedPreferences sharedPreferencesTheme;
    public SharedPreferences.Editor sharedPreferencesThemeEditor;


    public void setThemeGreenfield(RelativeLayout relativeLayout){
        relativeLayout.setBackgroundResource(R.drawable.bg_greenfield);
        currentTheme = THEME_GREENFIELD;
    }

    public void setThemePurple(RelativeLayout relativeLayout){
        relativeLayout.setBackgroundResource(R.drawable.bg_purple);
        currentTheme = THEME_PURPLE;
    }

    public void setThemeSkyblue(RelativeLayout relativeLayout){
        relativeLayout.setBackgroundResource(R.drawable.bg_skyblue);
        currentTheme = THEME_SKYBLUE;
    }

    public void setThemeMarine(RelativeLayout relativeLayout){
        relativeLayout.setBackgroundResource(R.drawable.bg_marine);
        currentTheme = THEME_MARINE;
    }

    public void setTheme(int theme, RelativeLayout relativeLayout) {
        switch (theme) {
            case THEME_GREENFIELD:
                setThemeGreenfield(relativeLayout);
                break;
            case THEME_PURPLE:
                setThemePurple(relativeLayout);
                break;
            case THEME_SKYBLUE:
                setThemeSkyblue(relativeLayout);
                break;
            case THEME_MARINE:
                setThemeMarine(relativeLayout);
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
