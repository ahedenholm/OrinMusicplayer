package com.orin.anders.orinmusicplayer.controllers;

import android.widget.ImageButton;

import com.orin.anders.orinmusicplayer.R;
import com.orin.anders.orinmusicplayer.helpers.Main_ActivityHelper;
import com.orin.anders.orinmusicplayer.utils.DeviceScreenCheck;

public class ButtonController {

    private static final String TAG = "ButtonController.Debug Message";

    //TODO referencing views through statics can cause leaks if activities are changed, since GC
    //cant release anything that keeps referencing a view
    public static ImageButton imageButtonPrev;
    public static ImageButton imageButtonPlay;
    public static ImageButton imageButtonNext;
    public static ImageButton imageButtonOpen;
    public static ImageButton imageButtonStop;
    public static ImageButton imageButtonPlaybackMode;
    public static ImageButton imageButtonSwitchtheme;

    public static void setImageButtonsToNull() {
        ButtonController.imageButtonPlay = null;
        ButtonController.imageButtonOpen = null;
        ButtonController.imageButtonNext = null;
        ButtonController.imageButtonPrev = null;
        ButtonController.imageButtonStop = null;
        ButtonController.imageButtonSwitchtheme = null;
        ButtonController.imageButtonPlaybackMode = null;
    }

    public static void setImageButtonPlayImage() {
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
    }

    public static void setImageButtonPauseImage() {
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }

    public static void setImageButtonShuffleImage() {
        imageButtonPlaybackMode.setBackgroundResource(R.drawable.button_shuffle);
    }

    public static void setImageButtonRepeatAllImage() {
        imageButtonPlaybackMode.setBackgroundResource(R.drawable.button_repeatall);
    }

    public static void setImageButtonRepeatOneImage() {
        imageButtonPlaybackMode.setBackgroundResource(R.drawable.button_repeatone);
    }

    public static void setDefaultButtonPositions() {
        ButtonController.imageButtonPrev.setY(getUpperButtonsDefaultPosition());
        ButtonController.imageButtonPlay.setY(getUpperButtonsDefaultPosition());
        ButtonController.imageButtonNext.setY(getUpperButtonsDefaultPosition());
        ButtonController.imageButtonOpen.setY(getLowerButtonsDefaultPosition());
        ButtonController.imageButtonStop.setY(getLowerButtonsDefaultPosition());
        ButtonController.imageButtonPlaybackMode.setY(getLowerButtonsDefaultPosition());
    }

    public static void setOpenlistButtonPositions() {
        ButtonController.imageButtonPrev.setY(getUpperButtonsOpenlistPosition());
        ButtonController.imageButtonPlay.setY(getUpperButtonsOpenlistPosition());
        ButtonController.imageButtonNext.setY(getUpperButtonsOpenlistPosition());
        ButtonController.imageButtonOpen.setY(getLowerButtonsOpenlistPosition());
        ButtonController.imageButtonStop.setY(getLowerButtonsOpenlistPosition());
        ButtonController.imageButtonPlaybackMode.setY(getLowerButtonsOpenlistPosition());
    }

    private static float getUpperButtonsDefaultPosition() {
        return (DeviceScreenCheck.getDeviceHeight(Main_ActivityHelper.activity) / 2)
                + (DeviceScreenCheck.getDeviceStatusBarHeight(Main_ActivityHelper.context) / 2);
    }

    private static float getLowerButtonsDefaultPosition() {
        float heightDifference = ButtonController.imageButtonStop.getY() -
                ButtonController.imageButtonPlay.getY();
        return (DeviceScreenCheck.getDeviceHeight(Main_ActivityHelper.activity) / 2)
                + heightDifference
                + (DeviceScreenCheck.getDeviceStatusBarHeight(Main_ActivityHelper.context) / 2);
    }

    private static float getUpperButtonsOpenlistPosition() {

        return (DeviceScreenCheck.getDeviceHeight(Main_ActivityHelper.activity) * 0.05f)
                + (DeviceScreenCheck.getDeviceStatusBarHeight(Main_ActivityHelper.context) / 2);
    }

    private static float getLowerButtonsOpenlistPosition() {
        float heightDifference = ButtonController.imageButtonStop.getY() -
                ButtonController.imageButtonPlay.getY();
        return (DeviceScreenCheck.getDeviceHeight(Main_ActivityHelper.activity) * 0.05f)
                + heightDifference
                + (DeviceScreenCheck.getDeviceStatusBarHeight(Main_ActivityHelper.context) / 2);
    }

}
