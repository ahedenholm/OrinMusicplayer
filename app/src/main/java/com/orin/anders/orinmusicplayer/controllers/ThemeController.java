package com.orin.anders.orinmusicplayer.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;

import com.orin.anders.orinmusicplayer.R;


    /*
    This class sets background images, or "themes", and saves them in shared preferences.
     */

public class ThemeController {
    public static final int THEME_GREENFIELD = 0;
    public static final int THEME_PURPLE = 1;
    public static final int THEME_SKYBLUE = 2;
    public static final int THEME_MARINE = 3;
    public static final int THEME_FREELOVE = 4;
    public static Integer currentTheme;

    private Context context;
    private SharedPreferences sharedPreferencesTheme;
    private SharedPreferences.Editor sharedPreferencesThemeEditor;

    public ThemeController(Context context){
        this.context = context;
    }

    public void setThemeGreenfield(ViewGroup viewGroup){
        viewGroup.setBackgroundResource(R.drawable.bg_greenfield);
        currentTheme = THEME_GREENFIELD;
    }

    public void setThemePurple(ViewGroup viewGroup){
        viewGroup.setBackgroundResource(R.drawable.bg_purple);
        currentTheme = THEME_PURPLE;
    }

    public void setThemeSkyblue(ViewGroup viewGroup){
        viewGroup.setBackgroundResource(R.drawable.bg_skyblue);
        currentTheme = THEME_SKYBLUE;
    }

    public void setThemeMarine(ViewGroup viewGroup){
        viewGroup.setBackgroundResource(R.drawable.bg_marine);
        currentTheme = THEME_MARINE;
    }
    public void setThemeFreeLove(ViewGroup viewGroup){
        viewGroup.setBackgroundResource(R.drawable.bg_marine);
        currentTheme = THEME_FREELOVE;
    }

    public void setTheme(int theme, ViewGroup viewGroup) {
        switch (theme) {
            case THEME_GREENFIELD:
                setThemeGreenfield(viewGroup);
                break;
            case THEME_PURPLE:
                setThemePurple(viewGroup);
                break;
            case THEME_SKYBLUE:
                setThemeSkyblue(viewGroup);
                break;
            case THEME_MARINE:
                setThemeMarine(viewGroup);
                break;
            case THEME_FREELOVE:
                setThemeFreeLove(viewGroup);
                break;
            default:
                break;
        }
    }

    public SharedPreferences getSharedPreferencesTheme() {
        sharedPreferencesTheme = context.getSharedPreferences("THEME_PREFS", context.MODE_PRIVATE);
        return sharedPreferencesTheme;
    }

    public void saveVisualTheme(){
        sharedPreferencesThemeEditor = sharedPreferencesTheme.edit();
        sharedPreferencesThemeEditor.putInt("savedTheme", ThemeController.currentTheme);
        sharedPreferencesThemeEditor.apply();
    }

}
