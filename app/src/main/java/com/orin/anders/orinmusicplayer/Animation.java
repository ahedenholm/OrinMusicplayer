package com.orin.anders.orinmusicplayer;

import android.animation.ValueAnimator;
import android.widget.Button;
import android.widget.ListView;

public class Animation {

    ValueAnimator valueAnimator;

    //used to fade the song list in and out
    public void animationFadeListView(final ListView listView) {
        if (listView.getAlpha() < 1) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    listView.setAlpha((float) animation.getAnimatedValue());
                    listView.setEnabled(true);
                }
            });
            valueAnimator.setDuration(200);
            valueAnimator.start();

        } else {
            valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    listView.setAlpha((float) animation.getAnimatedValue());
                    listView.setEnabled(false);
                }
            });
            valueAnimator.setDuration(200);
            valueAnimator.start();
        }
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
