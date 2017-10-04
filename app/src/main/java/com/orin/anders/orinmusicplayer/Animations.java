package com.orin.anders.orinmusicplayer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Space;

import com.orin.anders.orinmusicplayer.controllers.ButtonController;

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
        Log.d(TAG, "play" + ButtonController.imageButtonPlay.getY());
        Log.d(TAG, "stop" + ButtonController.imageButtonStop.getY());

        final float moveYmultiplier = -2.4f;
        final ImageButton[] buttonArray = {
                ButtonController.imageButtonPrev,
                ButtonController.imageButtonPlay,
                ButtonController.imageButtonNext,
                ButtonController.imageButtonOpen,
                ButtonController.imageButtonStop,
                ButtonController.imageButtonPlaybackMode};
        final float[] oldPositionArray = new float[buttonArray.length];
        AnimationListener animationListener = new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG,"anim end");
                for (int x = 0;x < buttonArray.length; x++){
                    Log.d(TAG, ""+buttonArray[x].getY());
                    //buttonArray[x].setY(oldPositionArray[x] * moveYmultiplier);
                    Log.d(TAG, ""+buttonArray[x].getY());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, moveYmultiplier);
        //translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(animationListener);
        translateAnimation.setDuration(200);

        for (int x = 0;x < buttonArray.length; x++){
            oldPositionArray[x] = buttonArray[x].getY();
        }

        for (int x = 0; x < buttonArray.length; x++) {
            buttonArray[x].startAnimation(translateAnimation);
        }

        Log.d(TAG, "play" + ButtonController.imageButtonPlay.getY());
        Log.d(TAG, "stop" + ButtonController.imageButtonStop.getY());
    }


    //possibly to be used in a global fade method that fades any Object sent to it
    public Object checkClass(Object object) {
        if (object instanceof ListView) {
            return new ListView(null);
        } else if (object instanceof Button)
            return new Button(null);
        return 0;
    }


}
