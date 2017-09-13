package com.orin.anders.orinmusicplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

    /*
    This class handles the various playback funtions.
    It also:
    - Initializes the MediaPlayer,
    - Handles loss and gain of AUDIOFOCUS.
    - Handles AUDIO_BECOMING_NOISY.
    - Handles notifications
     */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private BroadcastReceiver becomingNoisyReceiver;
    private ArrayList<Song> songArrayList;
    private ListView songList;
    private final IBinder musicBind = new MusicBinder();
    private Random random = new Random();
    private Context context;
    private Activity activity;
    private int songPosition;
    private int audioFocusResult;
    private int songCurrentTimeMillisec;
    private static final String TAG = "Debug Message";
    private Bitmap orinNotificationIcon;

    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        songCurrentTimeMillisec = 0;
        initMusicPlayer();
        songList = (ListView) Main_ActivityHelper.activity.findViewById(R.id.song_list);
        orinNotificationIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.orin_notification_image);
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

    //initialize listeners, audio focus, mediaplayer, set repeatMode
    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        MusicServiceHelper.setRepeatMode(MusicServiceHelper.REPEAT_ALL);
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
        foregroundNotificationUpdate();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //set songCurrentTimeMillisec to 0 so next song will actually start from the beginning
        songCurrentTimeMillisec = 0;
        choosePlaybackByRepeatMode();
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
            MusicServiceHelper.previousSong = MusicServiceHelper.selectedSong;
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
            songList.setSelection(songPosition);
            ButtonController.setImageButtonPauseImage();
        }
    }

    public void nextSong() {
        switch (MusicServiceHelper.repeatMode) {
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
                    Log.d(TAG, "nextSong() !songPosition < songArrayList.size, songPosition: " + String.valueOf(songPosition));
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
            foregroundNotificationUpdate();
        }
    }

    public void stopSong() {
        if (mediaPlayer.isPlaying()) {
            unregisterReceiver(becomingNoisyReceiver);
            mediaPlayer.reset();
            songCurrentTimeMillisec = 0;
            ButtonController.setImageButtonPlayImage();
        }
    }

    public void choosePlaybackByRepeatMode() {
        switch (MusicServiceHelper.repeatMode) {
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

    public void volumeLower() {
        mediaPlayer.setVolume(0.01f, 0.01f);
    }

    public void volumeDefault() {
        mediaPlayer.setVolume(1, 1);
    }

    public Notification foregroundNotification() {
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);

        nBuilder.setContentTitle("Orin Musicplayer")
                .setContentText(
                        MusicServiceHelper.selectedSong.getArtist() + " - " +
                                MusicServiceHelper.selectedSong.getTitle())
                .setLargeIcon(orinNotificationIcon);

        if (mediaPlayer.isPlaying())
            nBuilder.setSmallIcon(android.R.drawable.ic_media_play);
        else nBuilder.setSmallIcon(android.R.drawable.ic_media_pause);

        return (nBuilder.build());
    }

    public void foregroundNotificationUpdate() {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);

        nBuilder.setContentTitle("Orin Musicplayer")
                .setContentText(
                        MusicServiceHelper.selectedSong.getArtist() + " - " +
                                MusicServiceHelper.selectedSong.getTitle())
        .setLargeIcon(orinNotificationIcon);

        if (mediaPlayer.isPlaying())
            nBuilder.setSmallIcon(android.R.drawable.ic_media_play);
        else nBuilder.setSmallIcon(android.R.drawable.ic_media_pause);

        notificationManager.notify(
                MusicServiceHelper.NOTIFICATION_ID,
                nBuilder.build());
    }

}
