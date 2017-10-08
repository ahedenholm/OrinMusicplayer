package com.orin.anders.orinmusicplayer;


import android.app.Activity;
import android.content.Context;
import android.widget.ImageButton;

import com.orin.anders.orinmusicplayer.controllers.ButtonController;
import com.orin.anders.orinmusicplayer.utils.DeviceScreenCheck;


/*
    Class used to move around view on the screen, setting new X,Y coordinates for example.
 */
public class ViewMover {

    private static final String TAG = "ViewMover.Debug Message";

    private Activity activity;
    private Context context;
    private int deviceStatusBar;
    private float buttonHeightDifference;
    private float targetYClosedList;
    private float targetYOpenList;


    public ViewMover(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        buttonHeightDifference = ButtonController.imageButtonStop.getY() - ButtonController.imageButtonPlay.getY();
        deviceStatusBar = DeviceScreenCheck.getDeviceStatusBarHeight(context);
        targetYClosedList = ((DeviceScreenCheck.getDeviceHeight(activity) - deviceStatusBar) / 2) -
                buttonHeightDifference;
        targetYOpenList = DeviceScreenCheck.getDeviceHeight(activity) * 0.05f;
    }

    public void slideUpPlaybackButtons() {
        final ImageButton[] buttonArrayUpper = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext,};
        final ImageButton[] buttonArrayLower = {
                ButtonController.imageButtonOpen,
                ButtonController.imageButtonStop,
                ButtonController.imageButtonPlaybackMode};

        for (int x = 0; x < buttonArrayUpper.length; x++) {
            buttonArrayUpper[x].setY(targetYOpenList);
        }
        for (int x = 0; x < buttonArrayLower.length; x++) {
            buttonArrayLower[x].setY(targetYOpenList + buttonHeightDifference);
        }
    }

    public void slideDownPlaybackButtons() {
        final ImageButton[] buttonArrayUpper = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext,};
        final ImageButton[] buttonArrayLower = {
                ButtonController.imageButtonOpen,
                ButtonController.imageButtonStop,
                ButtonController.imageButtonPlaybackMode};

        for (int x = 0; x < buttonArrayUpper.length; x++) {
            buttonArrayUpper[x].setY(targetYClosedList);
        }
        for (int x = 0; x < buttonArrayLower.length; x++) {
            buttonArrayLower[x].setY(targetYClosedList + buttonHeightDifference);
        }
    }

}
