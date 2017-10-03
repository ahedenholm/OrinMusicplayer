package com.orin.anders.orinmusicplayer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

        animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_playback_buttons);
        centerSpace = (Space) activity.findViewById(R.id.centerSpace);

        for (int currentButton = 0; currentButton < ButtonController.playbackButtons.length; currentButton++) {
            final int x = currentButton;

            //TODO adjust movement calculation, to scale depending on device resolution
            valueAnimator = ValueAnimator.ofFloat(ButtonController.playbackButtons[currentButton].getY(),
                    (ButtonController.playbackButtons[currentButton].getY() - 50));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ButtonController.playbackButtons[x].setY((float) animation.getAnimatedValue());
                }
            });

        }
        valueAnimator.setDuration(500);
        valueAnimator.start();
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
