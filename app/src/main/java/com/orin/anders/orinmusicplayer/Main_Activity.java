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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.Toast;

import com.orin.anders.orinmusicplayer.MusicService.MusicBinder;

public class Main_Activity extends AppCompatActivity {

    private Animation animation = new Animation();
    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicSrv;
    private LayoutThemeController layoutThemeController;
    private Intent playIntent;
    private boolean musicBound = false;
    LinearLayout linearLayout;

    private ImageButton imageButtonOpen;
    private ImageButton imageButtonPlay;
    private ImageButton imageButtonMenu;
    private ImageButton imageButtonNext;
    private ImageButton imageButtonPrev;
    private ImageButton imageButtonStop;

    private static final String TAG = "";
	
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songView = (ListView) findViewById(R.id.song_list);
        songList = new ArrayList<Song>();
        layoutThemeController = new LayoutThemeController();
        imageButtonOpen = (ImageButton) findViewById(R.id.imageButtonOpen);
        imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        imageButtonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        imageButtonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        imageButtonPrev = (ImageButton) findViewById(R.id.imageButtonPrev);
        imageButtonStop = (ImageButton) findViewById(R.id.imageButtonStop);
        linearLayout = (LinearLayout)findViewById(R.id.main_layout);

        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        activateSongList();
        play();
        menu();
		pressedNext();
		pressedPrev();
        pressedStop();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder musicBinder = (MusicBinder) service;
            musicSrv = musicBinder.getService();
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }
	
	public void songPicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        Log.i(TAG, "songPicked()" + view.getTag().toString());
        musicSrv.playSong();
        imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
    }

    public void activateSongList() {
        imageButtonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.animationFade(songView);
            }
        });
    }

    public void play() {
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicSrv.getIsPlaying()) {
                    musicSrv.playSong();
                    imageButtonPlay.setBackgroundResource(R.drawable.button_pause);
                }
                else {
                    musicSrv.pauseSong();
                    imageButtonPlay.setBackgroundResource(R.drawable.button_play);
                }
            }
        });
    }
	
    public void menu(){
        imageButtonMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                layoutThemeController.setThemeRandom(linearLayout);
            }
        });
    }
	
	public void pressedNext(){
		imageButtonNext.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				musicSrv.nextSong();
			}
		});
	}	
	public void pressedPrev(){
		imageButtonPrev.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				musicSrv.prevSong();
			}
		});
	}
	
	public void pressedStop(){
		imageButtonStop.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				musicSrv.stopSong();
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
