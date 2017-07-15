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

            /*TODO not working, crashes when e.g. phones are unplugged. crashes
            as long as anything happens inside pauseSong()*/
            try {
                Toast.makeText(context,"Headphones disconnected.",Toast.LENGTH_SHORT).show();
                musicService.pauseSong();
            } catch (RuntimeException re){
                re.printStackTrace();
            }
        }
    }
}
