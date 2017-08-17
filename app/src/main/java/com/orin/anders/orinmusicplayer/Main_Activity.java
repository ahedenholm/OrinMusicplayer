package com.orin.anders.orinmusicplayer;

import android.content.SharedPreferences;
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
import android.widget.AdapterView;
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

    private Animation animation = new Animation();
    private ArrayList<Song> songList;
    private ListView songListView;
    private MusicService musicService;
    private LayoutThemeController layoutThemeController;
    private Intent playIntent;
    private boolean musicBound = false;
    private LinearLayout linearLayout;
    private static final String TAG = "Debug Message";
    public static Context mainActivityContext;

    private ImageButton imageButtonOpen;
    //TODO implement as nonstatic
    //currently static so background resource can be changed from within MusicService class
    //with a static set method: setImageButtonPlayImage()
    private static ImageButton imageButtonPlay;
    private ImageButton imageButtonMenu;
    private ImageButton imageButtonNext;
    private ImageButton imageButtonPrev;
    private ImageButton imageButtonStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityContext = this;
        songListView = (ListView) findViewById(R.id.song_list);
        songList = new ArrayList<Song>();

        imageButtonOpen = (ImageButton) findViewById(R.id.imageButtonOpen);
        imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        imageButtonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        imageButtonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        imageButtonPrev = (ImageButton) findViewById(R.id.imageButtonPrev);
        imageButtonStop = (ImageButton) findViewById(R.id.imageButtonStop);

        //TODO implement sharedpreferences for saving background theme
        SharedPreferences theme_prefs = getSharedPreferences("THEME_PREFS", 1);

        linearLayout = (LinearLayout) findViewById(R.id.main_layout);
        layoutThemeController = new LayoutThemeController();

        //TODO set visual theme to sharedpreference data
        layoutThemeController.setThemeBG2(linearLayout);

        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
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
        setImageButtonPauseImage();
        setImageButtonPlayImage();
        pressedSongList();
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
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    public void pressedSongList() {
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                songListView.getChildAt(Integer.parseInt(view.getTag().toString())).;
                songPicked(songListView);
            }
        });
    }

    public void songPicked(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        Log.d(TAG, "songPicked() value:" + view.getTag().toString());
        musicService.playSong();
    }

    public void pressedOpen() {
        imageButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.animationFadeListView(songListView);
            }
        });
    }

    public void pressedPlay() {
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
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
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutThemeController.getThemeID() == 2)
                    layoutThemeController.setThemeBG4(linearLayout);
                else layoutThemeController.setThemeBG2(linearLayout);
            }
        });
    }

    public void pressedNext() {
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.nextSong();
            }
        });
    }

    public void pressedPrev() {
        imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.prevSong();
            }
        });
    }

    public void pressedStop() {
        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.stopSong();
            }
        });
    }

    public static void setImageButtonPauseImage() {
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }

    public static void setImageButtonPlayImage() {
        imageButtonPlay.setBackgroundResource(R.drawable.button_play);
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
