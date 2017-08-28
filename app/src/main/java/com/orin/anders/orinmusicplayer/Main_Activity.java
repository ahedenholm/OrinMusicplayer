package com.orin.anders.orinmusicplayer;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.View;

import com.orin.anders.orinmusicplayer.MusicService.MusicBinder;

public class Main_Activity extends AppCompatActivity {

    private ThemeController themeController;
    private ArrayList<Song> songList;
    private LinearLayout linearLayout;
    private MusicService musicService;
    private Animation animation = new Animation();
    private ListView songListView;
    private Intent playIntent;
    private String theme;
    private boolean musicBound = false;
    private static final String TAG = "Debug Message";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Main_ActivityHelper.mainActivityContext = this;
        ButtonController.imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        ButtonController.imageButtonOpen = (ImageButton) findViewById(R.id.imageButtonOpen);
        ButtonController.imageButtonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        ButtonController.imageButtonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        ButtonController.imageButtonPrev = (ImageButton) findViewById(R.id.imageButtonPrev);
        ButtonController.imageButtonStop = (ImageButton) findViewById(R.id.imageButtonStop);

        songListView = (ListView) findViewById(R.id.song_list);
        songList = new ArrayList<Song>();
        linearLayout = (LinearLayout) findViewById(R.id.main_layout);
        themeController = new ThemeController();

        themeController.sharedPreferencesTheme = getSharedPreferences("THEME_PREFS", MODE_PRIVATE);
        themeController.sharedPreferencesThemeEditor = themeController.sharedPreferencesTheme.edit();
        themeController.setTheme(themeController.sharedPreferencesTheme
                .getInt("savedTheme", 0), linearLayout);

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
        Log.d(TAG, "onCreate()");
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


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
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
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        themeController.saveVisualTheme();
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    public void songPicked(View view) {
        //TODO listSelector doesnt seem to work properly
        /*
        ((TextView) view.findViewById(R.id.song_artist)).setTextColor(Color.GRAY);
        ((TextView) view.findViewById(R.id.song_title)).setTextColor(Color.GRAY);
        ((TextView) view.findViewById(R.id.song_length)).setTextColor(Color.GRAY);
        */

        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        Log.d(TAG, "songPicked() value:" + view.getTag().toString());
        musicService.playSong();
    }

    public void pressedOpen() {
        ButtonController.imageButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.animationFadeListView(songListView);
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
        ButtonController.imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ThemeController.currentTheme != null &&
                        ThemeController.currentTheme == ThemeController.THEME_PURPLE)
                {
                    themeController.setThemeMarine(linearLayout);
                    Log.d(TAG, ThemeController.currentTheme.toString());
                } else {
                    themeController.setThemePurple(linearLayout);
                    Log.d(TAG, ThemeController.currentTheme.toString());
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
