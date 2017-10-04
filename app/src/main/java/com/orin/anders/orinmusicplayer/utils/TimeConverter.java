package com.orin.anders.orinmusicplayer.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeConverter {

    public static String convertDuration(long duration) {
        if (TimeUnit.MILLISECONDS.toHours(duration) == 0) {
            return String.format(Locale.ENGLISH, "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
        } else {
            return String.format(Locale.ENGLISH, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
        }
    }

}
