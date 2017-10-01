package com.orin.anders.orinmusicplayer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.orin.anders.orinmusicplayer.R;
import com.orin.anders.orinmusicplayer.adapters.ArtistViewAdapter;
import com.orin.anders.orinmusicplayer.controllers.ThemeController;

import java.util.ArrayList;
import java.util.List;

public class Artist_Activity extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;
    private ThemeController themeController;
    private RecyclerView recyclerView;
    private ViewGroup viewGroupArtistLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        viewGroupArtistLayout = (ViewGroup) findViewById(R.id.artist_layout);
        themeController = new ThemeController(this);
        themeController.setTheme(ThemeController.currentTheme, viewGroupArtistLayout);

        recyclerView = (RecyclerView) findViewById(R.id.artist_recyclerview);
        // specify that grid will consist of 2 columns
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        // provide our CustomSpanSizeLookup which determines how many spans each item in grid will occupy
        gridLayoutManager.setSpanSizeLookup(new CustomSpanSizeLookup());
        recyclerView.setLayoutManager(gridLayoutManager);

        // this is fake list of images
        List<Integer> imageResList = getMockedImageList();
        ArtistViewAdapter adapter = new ArtistViewAdapter(imageResList);
        recyclerView.setAdapter(adapter);
    }

    private List<Integer> getMockedImageList() {
        List<Integer> imageResList = new ArrayList<>();

        imageResList.add(R.drawable.bg_marine);
        imageResList.add(R.drawable.bg_greenfield);
        imageResList.add(R.drawable.bg_skyblue);
        imageResList.add(R.drawable.bg_marine);
        imageResList.add(R.drawable.bg_skyblue);
        imageResList.add(R.drawable.bg_purple);
        imageResList.add(R.drawable.bg_greenfield);
        imageResList.add(R.drawable.bg_marine);
        imageResList.add(R.drawable.bg_skyblue);
        imageResList.add(R.drawable.bg_purple);
        imageResList.add(R.drawable.bg_greenfield);
        return imageResList;
    }

    private List<Integer> getArtistList(){
        List<Integer> artistImage = new ArrayList<>();

        return artistImage;
    }

    private static class CustomSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        @Override
        public int getSpanSize(int i) {
            return 1;
        }
    }
}
