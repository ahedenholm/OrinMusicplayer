package com.orin.anders.orinmusicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

    /*
    This class handles the various playback funtions, play, play nexst song, play previous,
    pause and stop.
    It also:
    - Initializes the MediaPlayer,
    - Handles loss and gain of AUDIOFOCUS.
    - Handles AUDIO_BECOMING_NOISY.

     */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private final IBinder musicBind = new MusicBinder();
    private BroadcastReceiver becomingNoisyReceiver;
    private static final String TAG = "Debug Message";
    private ArrayList<Song> songArrayList;
    private int songPosition;
    private int songCurrentTimeMillisec;
    private int audioFocusResult;

    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        songCurrentTimeMillisec = 0;
        initMusicPlayer();
        becomingNoisyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                    try {
                        pauseSong();
                        Toast.makeText(context, "Headphones disconnected.", Toast.LENGTH_SHORT).show();
                    } catch (RuntimeException re) {
                        re.printStackTrace();
                    }
                }
            }
        };

    }

    //initialize listeners, audio focus, mediaplayer
    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        Log.d(TAG, "AUDIOFOCUS_GAIN");
                        volumeDefault();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        Log.d(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                        volumeDefault();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        Log.d(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                        volumeDefault();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.e(TAG, "AUDIOFOCUS_LOSS");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        volumeLower();
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                        // Temporary loss of audio focus - expect to get it back - you can keep your resources around
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        volumeLower();
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        // Lower the volume or pause
                        break;
                }
            }
        };
        audioFocusResult = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public void setList(ArrayList<Song> theSongs) {
        songArrayList = theSongs;
    }

    class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.pause();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mediaPlayer.seekTo(songCurrentTimeMillisec);
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //set songCurrentTimeMillisec to 0 so next song will actually start from the beginning
        songCurrentTimeMillisec = 0;
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }


    //listens for audio_noisy intent, resets mediaPlayer, get chosen song
    public void playSong() {
        //request audio focus
        audioFocusResult = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            //listen for noise, i.e unplugged headphones
            registerReceiver(becomingNoisyReceiver, intentFilter);
            mediaPlayer.reset();
            Song playSong = songArrayList.get(songPosition);
            MusicServiceHelper.selectedSong = songArrayList.get(songPosition);
            long currSong = playSong.getId();
            Uri trackUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
            try {
                mediaPlayer.setDataSource(getApplicationContext(), trackUri);
            } catch (Exception e) {
                Log.e("MUSIC SERVICE", "Error setting data source.");
            }
            //onPrepared() calls mediaPlayer.Start()
            mediaPlayer.prepareAsync();
            Main_Activity.setImageButtonPauseImage();
        }
    }

    public void nextSong() {
        Log.d(TAG, "getCurrentPosition value: " + String.valueOf(mediaPlayer.getCurrentPosition()));
        Log.d(TAG, "songlist size value: " + String.valueOf(songArrayList.size()));
        if (songPosition < (songArrayList.size() - 1)) {
            Log.d(TAG, "nextSong() value before : " + String.valueOf(songPosition));
            songPosition++;
            songCurrentTimeMillisec = 0;
            Log.d(TAG, "nextSong() value after ++ : " + String.valueOf(songPosition));
            playSong();
        } else {
            Log.d(TAG, "nextSong() !songPosition < songArrayList.size, songPosition: " + String.valueOf(songPosition));
            songPosition = 0;
            songCurrentTimeMillisec = 0;
            playSong();
        }
    }

    public void prevSong() {
        Log.d(TAG, "getCurrentPosition value: " + String.valueOf(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.getCurrentPosition() > 1500) {
            Log.d(TAG, "getCurrentPosition value > 1000: " + String.valueOf(mediaPlayer.getCurrentPosition()));
            songCurrentTimeMillisec = 0;
            Log.d(TAG, "song restarted, songPosition: " + String.valueOf(songPosition));
            playSong();
        } else if (mediaPlayer.getCurrentPosition() <= 1500 && songPosition > 0) {
            Log.d(TAG, "songPosition value before: " + String.valueOf(songPosition));
            songPosition--;
            songCurrentTimeMillisec = 0;
            //Log.d(TAG, "songPosition value after: " + String.valueOf(songPosition));
            playSong();
        } else if (mediaPlayer.getCurrentPosition() <= 1500 && songPosition == 0) {
            songPosition = songArrayList.size() - 1;
            songCurrentTimeMillisec = 0;
            playSong();
        }
    }

    public void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            unregisterReceiver(becomingNoisyReceiver);
            mediaPlayer.pause();
            Main_Activity.setImageButtonPlayImage();
            songCurrentTimeMillisec = mediaPlayer.getCurrentPosition();
        }
    }

    public void stopSong() {
        if (mediaPlayer.isPlaying()) {
            unregisterReceiver(becomingNoisyReceiver);
            mediaPlayer.reset();
            songCurrentTimeMillisec = 0;
            Main_Activity.setImageButtonPlayImage();
        }
    }

    public void setSong(int songIndex) {
        songPosition = songIndex;
        songCurrentTimeMillisec = 0;
    }

    public boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void volumeLower() {
        mediaPlayer.setVolume(0.01f, 0.01f);
    }

    public void volumeDefault() {
        mediaPlayer.setVolume(1, 1);
    }


}
