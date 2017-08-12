package com.orin.anders.orinmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class BecomingNoisyReceiver extends BroadcastReceiver {
    MusicService musicService = new MusicService();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){

            try {
                musicService.pauseSong(); //TODO playback is actually not paused when headphones are disconnected
                Toast.makeText(context,"Headphones disconnected.",Toast.LENGTH_SHORT).show();
            } catch (RuntimeException re){
                re.printStackTrace();
            }
        }
    }
}
