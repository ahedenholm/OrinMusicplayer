package com.orin.anders.orinmusicplayer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

public class Animations extends android.view.animation.Animation {

    private static final String TAG = "Anim.Debug Message";
    private ValueAnimator valueAnimator;
    private Animation animation;
    private Activity activity;
    private Context context;

    public Animations(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void fadeView(final View view, int duration) {
        if (view.getAlpha() < 1) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setAlpha((float) animation.getAnimatedValue());
                    view.setEnabled(true);
                }
            });
            valueAnimator.setDuration(duration);
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
            valueAnimator.setDuration(duration);
            valueAnimator.start();
        }
    }

}
