package com.orin.anders.orinmusicplayer;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;

import com.orin.anders.orinmusicplayer.controllers.ButtonController;
import com.orin.anders.orinmusicplayer.utils.DeviceScreenCheck;


/*
    Class used to move around view on the screen, setting new X,Y coordinates for example.
 */
public class ViewMover {

    private static final String TAG = "ViewMover.Debug Message";
    private float buttonHeightDifference;
    private float targetYClosedList;
    private float targetYOpenList;
    private int deviceStatusBar;
    private Activity activity;
    private Context context;

    public ViewMover(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        deviceStatusBar = DeviceScreenCheck.getDeviceStatusBarHeight(context);
        targetYClosedList = ((DeviceScreenCheck.getDeviceHeight(activity) - deviceStatusBar) / 2) -
                buttonHeightDifference;
        targetYOpenList = DeviceScreenCheck.getDeviceHeight(activity) * 0.05f;
    }

    public void slideUpPlaybackButtons() {

        Log.d(TAG,"targetYOpen"+targetYOpenList);
        Log.d(TAG,"heightdif"+buttonHeightDifference);
        Log.d(TAG,"end play"+ButtonController.imageButtonPlay.getY());
        Log.d(TAG,"end stop"+ButtonController.imageButtonStop.getY());
        buttonHeightDifference = ButtonController.imageButtonStop.getY() - ButtonController.imageButtonPlay.getY();

        final ImageButton[] buttonArrayUpper = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext};
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


        Log.d(TAG,"end play"+ButtonController.imageButtonPlay.getY());
        Log.d(TAG,"end stop"+ButtonController.imageButtonStop.getY());
    }

    public void slideDownPlaybackButtons() {
        Log.d(TAG,"targetYOpen"+targetYClosedList);
        Log.d(TAG,"heightdif"+buttonHeightDifference);
        Log.d(TAG,"end play"+ButtonController.imageButtonPlay.getY());
        Log.d(TAG,"end stop"+ButtonController.imageButtonStop.getY());
        buttonHeightDifference = ButtonController.imageButtonStop.getY() - ButtonController.imageButtonPlay.getY();

        final ImageButton[] buttonArrayUpper = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext};
        final ImageButton[] buttonArrayLower = {
                ButtonController.imageButtonOpen,
                ButtonController.imageButtonStop,
                ButtonController.imageButtonPlaybackMode};

        for (int x = 0; x < buttonArrayUpper.length; x++) {
            buttonArrayUpper[x].setY(targetYClosedList - buttonHeightDifference);
        }
        for (int x = 0; x < buttonArrayLower.length; x++) {
            buttonArrayLower[x].setY(targetYClosedList);
        }

        Log.d(TAG,"end play"+ButtonController.imageButtonPlay.getY());
        Log.d(TAG,"end stop"+ButtonController.imageButtonStop.getY());
    }

}
