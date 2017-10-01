package com.orin.anders.orinmusicplayer;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.orin.anders.orinmusicplayer.controllers.ButtonController;
import com.orin.anders.orinmusicplayer.helpers.Main_ActivityHelper;
import com.orin.anders.orinmusicplayer.helpers.MusicServiceHelper;

import java.util.ArrayList;
import java.util.Random;

    /*
    This class handles the various playback funtions.
    It also:
    - Initializes the MediaPlayer,
    - Handles loss and gain of AUDIOFOCUS.
    - Handles AUDIO_BECOMING_NOISY.
     */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private OrinNotification orinNotification;
    private BroadcastReceiver becomingNoisyReceiver;
    private ArrayList<Song> songArrayList;
    private AudioManager audioManager;
    private IntentFilter intentFilterAudioBecomingNoisy = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private MediaPlayer mediaPlayer;
    private ListView songList;
    private Random random = new Random();
    private Context context;
    private Activity activity;
    private int songPosition;
    private int audioFocusResult;
    private int songCurrentTimeMillisec;
    private final IBinder musicBind = new MusicBinder();
    private static final String TAG = "Debug Message";

    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        songCurrentTimeMillisec = 0;
        initMusicPlayer();
        MusicServiceHelper.setPlaybackMode(MusicServiceHelper.REPEAT_ALL);
        songList = (ListView) Main_ActivityHelper.activity.findViewById(R.id.song_list);
        orinNotification = new OrinNotification(this);
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
                        //illegalStateException is thrown here on isPlaying() in pauseSong() upon
                        //reentering the app after closing it.
                        try {
                            pauseSong();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.e(TAG, "AUDIOFOCUS_LOSS");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        volumeLower();
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        volumeLower();
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        break;
                }
            }
        };
        audioFocusResult = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
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

    //initialize listeners, audio focus, mediaplayer, set playbackMode
    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs) {
        songArrayList = theSongs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
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
        orinNotification.foregroundNotificationUpdate(mediaPlayer);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //set songCurrentTimeMillisec to 0 so next song will actually start from the beginning
        songCurrentTimeMillisec = 0;
        choosePlaybackByPlaybackMode();
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
            registerReceiver(becomingNoisyReceiver, intentFilterAudioBecomingNoisy);
            mediaPlayer.reset();
            Song playSong = songArrayList.get(songPosition);
            MusicServiceHelper.previousSong = MusicServiceHelper.selectedSong;
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
            songList.setSelection(songPosition);
            ButtonController.setImageButtonPauseImage();
        }
    }

    public void nextSong() {
        switch (MusicServiceHelper.playbackMode) {
            case MusicServiceHelper.SHUFFLE:
                setSong(random.nextInt(songArrayList.size()));
                playSong();
                break;
            default:
                if (songPosition < (songArrayList.size() - 1)) {
                    songPosition++;
                    songCurrentTimeMillisec = 0;
                    playSong();
                } else {
                    songPosition = 0;
                    songCurrentTimeMillisec = 0;
                    playSong();
                }
                break;
        }
    }

    public void prevSong() {
        if (mediaPlayer.getCurrentPosition() > 1500) {
            songCurrentTimeMillisec = 0;
            playSong();
        } else if (mediaPlayer.getCurrentPosition() <= 1500 && songPosition > 0) {
            songPosition--;
            songCurrentTimeMillisec = 0;
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
            ButtonController.setImageButtonPlayImage();
            songCurrentTimeMillisec = mediaPlayer.getCurrentPosition();
            orinNotification.foregroundNotificationUpdate(mediaPlayer);
        }
    }

    public void stopSong() {
        if (mediaPlayer.isPlaying()) {
            unregisterReceiver(becomingNoisyReceiver);
            mediaPlayer.reset();
            songCurrentTimeMillisec = 0;
            ButtonController.setImageButtonPlayImage();
            orinNotification.foregroundNotificationUpdate(mediaPlayer);
        }
    }

    public void choosePlaybackByPlaybackMode() {
        switch (MusicServiceHelper.playbackMode) {
            case MusicServiceHelper.REPEAT_ALL:
                nextSong();
                break;
            case MusicServiceHelper.REPEAT_ONE:
                playSong();
                break;
            case MusicServiceHelper.SHUFFLE:
                setSong(random.nextInt(songArrayList.size()));
                playSong();
                break;
        }
    }

    public void setSong(int songIndex) {
        songPosition = songIndex;
        songCurrentTimeMillisec = 0;
    }

    public boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void volumeLower() {
        mediaPlayer.setVolume(0.01f, 0.01f);
    }

    public void volumeDefault() {
        mediaPlayer.setVolume(1, 1);
    }

    public void setContext(Context ctx) {
        context = ctx;
    }

}
