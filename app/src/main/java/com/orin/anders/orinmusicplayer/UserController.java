package com.orin.anders.orinmusicplayer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class UserController extends AppCompatActivity {

    private ImageButton imageButtonOpen;
    private ImageButton imageButtonPlay;
    private MusicService musicSrv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        imageButtonOpen = (ImageButton) findViewById(R.id.imageButtonOpen);
        imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
    }

    public ImageButton getImageButtonPlay(){return imageButtonPlay;}
    public ImageButton getImageButtonOpen(){return imageButtonOpen;}
    public void setImageButtonToPlay(){imageButtonPlay.setBackgroundResource(R.drawable.button_play);}
    public void setImageButtonToPause(){imageButtonPlay.setBackgroundResource(R.drawable.button_pause);}



    public void play(final MusicService musicSrv) {
        this.getImageButtonPlay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicSrv.getIsPlaying()) {
                    musicSrv.playSong();
                    setImageButtonToPause();
                }
                else {
                    musicSrv.pauseSong();
                    setImageButtonToPlay();
                }
            }
        });
    }

}
