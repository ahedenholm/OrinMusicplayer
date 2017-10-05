package com.orin.anders.orinmusicplayer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.Space;

import com.orin.anders.orinmusicplayer.controllers.ButtonController;
import com.orin.anders.orinmusicplayer.utils.DeviceScreenCheck;

public class Animations extends android.view.animation.Animation {

    private static final String TAG = "Anim.Debug Message";
    private Animation animation;
    private Activity activity;
    private Context context;
    private Space centerSpace;


    public Animations(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    ValueAnimator valueAnimator;

    public void fadeView(final View view) {
        if (view.getAlpha() < 1) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setAlpha((float) animation.getAnimatedValue());
                    view.setEnabled(true);
                }
            });
            valueAnimator.setDuration(200);
            valueAnimator.start();

        } else {
            valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setAlpha((float) animation.getAnimatedValue());
                    view.setEnabled(false);
                }
            });
            valueAnimator.setDuration(200);
            valueAnimator.start();
        }
    }

    public void slideUpPlaybackButtons() {
        /*TODO
        This method is too convoluted, it's goal is to slide the playback buttons upwards on
        the screen, and make room for the song list.
        TranslateAnimation doesn't automatically save the new position of the buttons after the
        animation of the playback buttons is finished. Hence the buttons return to their first position.

        Current approach: attach animationListener and use setY() on buttons in onAnimationEnd()
         */

        final float heightDifference =
                ButtonController.imageButtonStop.getY() - ButtonController.imageButtonPlay.getY();
        final float targetYOpenList = DeviceScreenCheck.getDeviceHeight(activity) * 0.05f;
        final float moveAnimation = targetYOpenList - ButtonController.imageButtonPlay.getY();
        final Space centerSpace = (Space) activity.findViewById(R.id.centerSpace);
        final ImageButton[] buttonArrayUpper = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext,};
        final ImageButton[] buttonArrayLower = {
                ButtonController.imageButtonOpen,
                ButtonController.imageButtonStop,
                ButtonController.imageButtonPlaybackMode};

        Log.d(TAG, "device height: "+DeviceScreenCheck.getDeviceHeight(activity));
        Log.d(TAG, "height dif: "+heightDifference);
        Log.d(TAG, "play: "+ButtonController.imageButtonPlay.getY());
        Log.d(TAG, "center: "+centerSpace.getY());
        Log.d(TAG, "stop: "+ButtonController.imageButtonStop.getY());

        AnimationListener animationListener = new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (int x = 0;x < buttonArrayUpper.length; x++){
                    buttonArrayUpper[x].setY(targetYOpenList);
                }
                for (int x = 0;x < buttonArrayLower.length; x++){
                    buttonArrayLower[x].setY(targetYOpenList + heightDifference);
                }
                Log.d(TAG, "end play: "+ButtonController.imageButtonPlay.getY());
                Log.d(TAG, "end center: "+centerSpace.getY());
                Log.d(TAG, "end stop: "+ButtonController.imageButtonStop.getY());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, moveAnimation);
        //translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(animationListener);
        translateAnimation.setDuration(50);

        for (int x = 0; x < buttonArrayUpper.length; x++) {
            buttonArrayUpper[x].startAnimation(translateAnimation);
        }
        for (int x = 0; x < buttonArrayLower.length; x++) {
            buttonArrayLower[x].startAnimation(translateAnimation);
        }
    }
    public void slideDownPlaybackButtons() {
        /*TODO
        This method is too convoluted, it's goal is to slide the playback buttons upwards on
        the screen, and make room for the song list.
        TranslateAnimation doesn't automatically save the new position of the buttons after the
        animation of the playback buttons is finished. Hence the buttons return to their first position.

        Current approach: attach animationListener and use setY() on buttons in onAnimationEnd()
         */

        final float heightDifference =
                ButtonController.imageButtonStop.getY() - ButtonController.imageButtonPlay.getY();
        final float targetYClosedList = (DeviceScreenCheck.getDeviceHeight(activity) / 2) -
                heightDifference ;
        final float moveAnimation = targetYClosedList - ButtonController.imageButtonPlay.getY();
        final Space centerSpace = (Space) activity.findViewById(R.id.centerSpace);
        final ImageButton[] buttonArrayUpper = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext,};
        final ImageButton[] buttonArrayLower = {
                ButtonController.imageButtonOpen,
                ButtonController.imageButtonStop,
                ButtonController.imageButtonPlaybackMode};

        Log.d(TAG, "device height: "+DeviceScreenCheck.getDeviceHeight(activity));
        Log.d(TAG, "height dif: "+heightDifference);
        Log.d(TAG, "play: "+ButtonController.imageButtonPlay.getY());
        Log.d(TAG, "center: "+centerSpace.getY());
        Log.d(TAG, "stop: "+ButtonController.imageButtonStop.getY());

        AnimationListener animationListener = new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (int x = 0;x < buttonArrayUpper.length; x++){
                    buttonArrayUpper[x].setY(targetYClosedList);
                }
                for (int x = 0;x < buttonArrayLower.length; x++){
                    buttonArrayLower[x].setY(targetYClosedList + heightDifference);
                }
                Log.d(TAG, "end play: "+ButtonController.imageButtonPlay.getY());
                Log.d(TAG, "end center: "+centerSpace.getY());
                Log.d(TAG, "end stop: "+ButtonController.imageButtonStop.getY());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, moveAnimation);
        //translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(animationListener);
        translateAnimation.setDuration(50);

        for (int x = 0; x < buttonArrayUpper.length; x++) {
            buttonArrayUpper[x].startAnimation(translateAnimation);
        }
        for (int x = 0; x < buttonArrayLower.length; x++) {
            buttonArrayLower[x].startAnimation(translateAnimation);
        }
    }

}
