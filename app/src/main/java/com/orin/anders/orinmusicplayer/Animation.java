package com.orin.anders.orinmusicplayer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

public class Animation {

    Context context;

    public Animation(Context context){
        this.context = context;
    }
    ValueAnimator valueAnimator;

    public void fadeView(final View view){
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

    public void slideUp(){
        AnimationUtils.loadAnimation(context, R.anim.slide_up_playback_buttons);
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
