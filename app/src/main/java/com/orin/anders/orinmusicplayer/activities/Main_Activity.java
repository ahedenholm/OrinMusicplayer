package com.orin.anders.orinmusicplayer.activities;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.orin.anders.orinmusicplayer.Animation;
import com.orin.anders.orinmusicplayer.MusicService;
import com.orin.anders.orinmusicplayer.MusicService.MusicBinder;
import com.orin.anders.orinmusicplayer.OrinNotification;
import com.orin.anders.orinmusicplayer.R;
import com.orin.anders.orinmusicplayer.Song;
import com.orin.anders.orinmusicplayer.adapters.SongAdapter;
import com.orin.anders.orinmusicplayer.controllers.ButtonController;
import com.orin.anders.orinmusicplayer.controllers.ThemeController;
import com.orin.anders.orinmusicplayer.helpers.Main_ActivityHelper;
import com.orin.anders.orinmusicplayer.helpers.MusicServiceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main_Activity extends AppCompatActivity {

    private OrinNotification orinNotification;
    private ThemeController themeController;
    private ArrayList<Song> songList;
    private RelativeLayout relativeLayout;
    private MusicService musicService;
    private Animation animation = new Animation();
    private ListView songListView;
    private Intent playIntent;
    private Context context;
    private boolean musicBound = false;
    private boolean songListEnabled;
    private static final String TAG = "Debug Message";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Main_ActivityHelper.activity = this;
        Main_ActivityHelper.context = this;
        context = this;

        ButtonController.imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        ButtonController.imageButtonOpen = (ImageButton) findViewById(R.id.imageButtonOpen);
        ButtonController.imageButtonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        ButtonController.imageButtonPrev = (ImageButton) findViewById(R.id.imageButtonPrev);
        ButtonController.imageButtonStop = (ImageButton) findViewById(R.id.imageButtonStop);
        ButtonController.imageButtonSwitchtheme = (ImageButton) findViewById(R.id.imageButtonSwitchtheme);
        ButtonController.imageButtonPlaybackMode = (ImageButton) findViewById(R.id.imageButtonPlaybackMode);

        orinNotification = new OrinNotification(this);
        songListView = (ListView) findViewById(R.id.song_list);
        songList = new ArrayList<>();
        relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        themeController = new ThemeController(this);

        themeController.setTheme(themeController.getSharedPreferencesTheme()
                .getInt("savedTheme", 0), relativeLayout);

        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getArtist().compareTo(b.getArtist());
            }
        });
        SongAdapter songAdt = new SongAdapter(this, songList);
        songListView.setAdapter(songAdt);
        pressedOpen();
        pressedPlay();
        pressedMenu();
        pressedNext();
        pressedPrev();
        pressedStop();
        pressedPlaybackMode();
        songListEnabled = false;
        ButtonController.imageButtonSwitchtheme.setEnabled(false);
        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        Main_ActivityHelper.activity = this;
        Main_ActivityHelper.context = this;
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        themeController.saveVisualTheme();
        Main_ActivityHelper.setActivityAndContextToNull();
        if (musicService != null && musicService.getIsPlaying())
            musicService.startForeground(MusicServiceHelper.NOTIFICATION_ID,
                    orinNotification.foregroundNotification(musicService.getMediaPlayer()));
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        Main_ActivityHelper.setActivityAndContextToNull();
        ButtonController.setImageButtonsToNull();
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                //TODO KEYCODE MEDIA PLAY not registering as intended, maybe use
                // broadcastreceiver ACTION_MEDIA instead
                Log.d(TAG, "KEYCODE MEDIA PLAY");
                if (!musicService.getIsPlaying()) {
                    musicService.playSong();
                } else {
                    musicService.pauseSong();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                Log.d(TAG, "KEYCODE MEDIA PREVIOUS");
                musicService.prevSong();
                return true;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                Log.d(TAG, "KEYCODE MEDIA NEXT");
                musicService.nextSong();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder musicBinder = (MusicBinder) service;
            musicService = musicBinder.getService();
            musicService.setList(songList);
            musicBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    public void songPicked(View view) {
        //TODO not working as intended, song doesnt gray out if next if pressed and shuffle is off
        /*
        ((TextView) view.findViewById(R.id.song_artist)).setTextColor(Color.GRAY);
        ((TextView) view.findViewById(R.id.song_title)).setTextColor(Color.GRAY);
        ((TextView) view.findViewById(R.id.song_length)).setTextColor(Color.GRAY);
        */
        if (songListEnabled) {
            musicService.setSong(Integer.parseInt(view.getTag().toString()));
            musicService.playSong();
        }
    }

    public void pressedOpen() {
        ButtonController.imageButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent openArtistIntent = new Intent(context, Artist_Activity.class);
                startActivity(openArtistIntent);
                */
                //Opens a songlist within the same activity
                animation.fadeView(songListView);
                animation.fadeView(ButtonController.imageButtonSwitchtheme);
                songListEnabled = !songListEnabled;

            }
        });
    }

    public void pressedPlay() {
        ButtonController.imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicService.getIsPlaying()) {
                    musicService.playSong();
                } else {
                    musicService.pauseSong();
                }
            }
        });
    }

    public void pressedMenu() {
        ButtonController.imageButtonSwitchtheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (ThemeController.currentTheme) {
                    case ThemeController.THEME_PURPLE:
                        themeController.setThemeGreenfield(relativeLayout);
                        break;
                    case ThemeController.THEME_GREENFIELD:
                        themeController.setThemeSkyblue(relativeLayout);
                        break;
                    case ThemeController.THEME_SKYBLUE:
                        themeController.setThemeMarine(relativeLayout);
                        break;
                    case ThemeController.THEME_MARINE:
                        themeController.setThemePurple(relativeLayout);
                        break;
                    default:
                        themeController.setThemeMarine(relativeLayout);
                }
            }
        });
    }

    public void pressedNext() {
        ButtonController.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.nextSong();
            }
        });
    }

    public void pressedPrev() {
        ButtonController.imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.prevSong();
            }
        });
    }

    public void pressedStop() {
        ButtonController.imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.stopSong();
            }
        });
    }

    public void pressedPlaybackMode() {
        ButtonController.imageButtonPlaybackMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (MusicServiceHelper.playbackMode) {
                    case MusicServiceHelper.REPEAT_ALL:
                        MusicServiceHelper.setPlaybackMode(MusicServiceHelper.REPEAT_ONE);
                        ButtonController.setImageButtonRepeatOneImage();
                        break;
                    case MusicServiceHelper.REPEAT_ONE:
                        MusicServiceHelper.setPlaybackMode(MusicServiceHelper.SHUFFLE);
                        ButtonController.setImageButtonShuffleImage();
                        break;
                    case MusicServiceHelper.SHUFFLE:
                        MusicServiceHelper.setPlaybackMode(MusicServiceHelper.REPEAT_ALL);
                        ButtonController.setImageButtonRepeatAllImage();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Cursor musicCursor = musicResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int lengthColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisLength = musicCursor.getString(lengthColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist, thisLength));
            }
            while (musicCursor.moveToNext());
        }
    }

}
