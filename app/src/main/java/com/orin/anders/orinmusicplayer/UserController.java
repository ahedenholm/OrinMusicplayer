package com.orin.anders.orinmusicplayer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class UserController extends AppCompatActivity {

    private ImageButton imageButtonOpen;
    private ImageButton imageButtonPlay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        imageButtonOpen = (ImageButton) findViewById(R.id.imageButtonOpen);
        imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
    }







    public ImageButton getImageButtonPlay() {
        return imageButtonPlay;
    }

    public ImageButton getImagetButtonOpen(){
        return imageButtonOpen;

    }
}
