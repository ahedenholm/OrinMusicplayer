package com.orin.anders.orinmusicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private AudioManager audioManager;

    private static final String TAG = "";
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPosn;
    private int songPausedAt;
    int audioFocusResult;
    private final IBinder musicBind = new MusicBinder();
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private BecomingNoisyReceiver myNoisyAudioStreamReceiver;

    public void onCreate() {
        super.onCreate();
        songPosn = 0;

        //songPausedAt set to 0 in onCreate, onCompletion and setSong
        songPausedAt = 0;
        player = new MediaPlayer();
        myNoisyAudioStreamReceiver = new BecomingNoisyReceiver();
        initMusicPlayer();
    }

    //initialize listeners, audio focus, mediaplayer
    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        Log.i(TAG, "AUDIOFOCUS_GAIN");
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.e(TAG, "AUDIOFOCUS_LOSS");
                        pauseSong();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                        // Temporary loss of audio focus - expect to get it back - you can keep your resources around
                        pauseSong();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        pauseSong();
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        // Lower the volume
                        break;
                }
            }
        };
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
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
        player.pause();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //set songPausedAt to 0 so next song will actually start from the beginning
        songPausedAt = 0;
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.seekTo(songPausedAt);
        mediaPlayer.start();
    }


    //listens for audio_noisy intent, resets player, get chosen song
    public void playSong() {
        //request audio focus
        audioFocusResult = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (audioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            //listen for noise, i.e unplugged headphones
            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
            player.reset();
            Song playSong = songs.get(songPosn);
            long currSong = playSong.getId();
            Uri trackUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
            try {
                player.setDataSource(getApplicationContext(), trackUri);
            } catch (Exception e) {
                Log.e("MUSIC SERVICE", "Error setting data source.");
            }
            //onPrepared() calls mediaPlayer.Start()
            player.prepareAsync();
            //TODO should also set button to button_play
        }
    }

    public void playNext() {
        if (songPosn < songs.size()) {
            songPosn++;
            playSong();
        } else {
            songPosn = 1;
            playSong();
        }
    }

    public void pauseSong() {
        unregisterReceiver(myNoisyAudioStreamReceiver);
        player.pause();
        songPausedAt = player.getCurrentPosition();
        //TODO should also set button to button_pause
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
        songPausedAt = 0;
    }

    public boolean getIsPlaying() {
        return player.isPlaying();
    }


}
