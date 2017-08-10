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

import com.bumptech.glide.Glide;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private AudioManager audioManager;


    private static final String TAG = "";
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private int songPosition;
    private int songPausedAt;
    private int audioFocusResult;
    private final IBinder musicBind = new MusicBinder();
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private BecomingNoisyReceiver becomingNoisyReceiver;

    public void onCreate() {
        super.onCreate();
        songPosition = 0;

        //songPausedAt set to 0 in onCreate, onCompletion and setSong
        songPausedAt = 0;
        mediaPlayer = new MediaPlayer();
        becomingNoisyReceiver = new BecomingNoisyReceiver();
        initMusicPlayer();
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
                        //Log.i(TAG, "AUDIOFOCUS_GAIN");
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        //Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        //Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
						pauseSong();
                        //Log.e(TAG, "AUDIOFOCUS_LOSS");
						break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
						pauseSong();
                        //Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                        // Temporary loss of audio focus - expect to get it back - you can keep your resources around
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        pauseSong();
                        //Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
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
        mediaPlayer.pause();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //set songPausedAt to 0 so next song will actually start from the beginning
        songPausedAt = 0;
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mediaPlayer.seekTo(songPausedAt);
        mediaPlayer.start();
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
            Song playSong = songs.get(songPosition);
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
            //TODO should also set button to button_play
        }
    }

    public void nextSong() {
        if (songPosition < songs.size()) {
            Log.i(TAG, "nextSong() before : " + String.valueOf(songPosition));
            songPosition++;
            Log.i(TAG, "nextSong() after ++ : " + String.valueOf(songPosition));
            playSong();
        } else {
            Log.i(TAG, "nextSong() !songPosition < songs.size, songPosition: " + String.valueOf(songPosition));
            songPosition = 1;
            playSong();
        }
    }
	
	public void prevSong() {
        if (songPausedAt > 1000) {
            songPausedAt = 0;
            playSong();
        } else if (songPosition > 1) {
            songPosition--;
            playSong();
        } else if (songPosition == 1){
			songPosition = 1;
			playSong();
		}
    }

    public void pauseSong() {
        unregisterReceiver(becomingNoisyReceiver);
        mediaPlayer.pause();
        songPausedAt = mediaPlayer.getCurrentPosition();
        //TODO should also set button to button_pause
    }
	
	public void stopSong(){
		unregisterReceiver(becomingNoisyReceiver);
		mediaPlayer.pause();
	}

    public void setSong(int songIndex) {
        songPosition = songIndex;
        songPausedAt = 0;
    }

    public boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }


}
