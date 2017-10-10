package com.orin.anders.orinmusicplayer;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;

import com.orin.anders.orinmusicplayer.controllers.ButtonController;
import com.orin.anders.orinmusicplayer.helpers.Main_ActivityHelper;
import com.orin.anders.orinmusicplayer.utils.DeviceScreenCheck;

import java.lang.ref.WeakReference;


/*
    Class used to move around view on the screen, setting new X,Y coordinates for example.
 */
public class ViewMover {

    private static final String TAG = "ViewMover.Debug Message";
    private Animations animations;
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
        animations = new Animations(context, activity);
    }

    public void slideUpPlaybackButtons() {
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
    }

    public void slideDownPlaybackButtons() {
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
    }

    private class MyHandler extends Handler{}
    private class MyRunnable implements Runnable{

        private final WeakReference<Activity> activity;
        public MyRunnable(Activity activity){
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            Log.d(TAG, "start runnable");

            Log.d(TAG, "songlist:"+Main_ActivityHelper.songListIsEnabled);
            if (Main_ActivityHelper.songListIsEnabled) {
                slideDownPlaybackButtons();
            } else slideUpPlaybackButtons();

            animations.fadeView(ButtonController.imageButtonPrev, 100);
            animations.fadeView(ButtonController.imageButtonPlay, 100);
            animations.fadeView(ButtonController.imageButtonNext, 100);
            animations.fadeView(ButtonController.imageButtonOpen, 100);
            animations.fadeView(ButtonController.imageButtonStop, 100);
            animations.fadeView(ButtonController.imageButtonPlaybackMode, 100);

            Main_ActivityHelper.songListIsEnabled = !Main_ActivityHelper.songListIsEnabled;

            Log.d(TAG, "runnable finished");
        }

    }
    private final MyHandler myHandler = new MyHandler();
    private final MyRunnable myRunnable = new MyRunnable(activity);

    public void openAndCloseSongList(int duration){
        animations.fadeView(ButtonController.imageButtonPrev, duration);
        animations.fadeView(ButtonController.imageButtonPlay, duration);
        animations.fadeView(ButtonController.imageButtonNext, duration);
        animations.fadeView(ButtonController.imageButtonOpen, duration);
        animations.fadeView(ButtonController.imageButtonStop, duration);
        animations.fadeView(ButtonController.imageButtonPlaybackMode, duration);

        myHandler.postDelayed(myRunnable, duration);
    }

}
