package com.orin.anders.orinmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class BecomingNoisyReceiver extends BroadcastReceiver {
    MusicService musicService = new MusicService();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
            musicService.pauseSong();
        }
    }
}
