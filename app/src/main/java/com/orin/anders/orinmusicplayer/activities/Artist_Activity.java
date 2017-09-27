package com.orin.anders.orinmusicplayer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orin.anders.orinmusicplayer.R;
import com.orin.anders.orinmusicplayer.adapters.ArtistViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Artist_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        recyclerView = (RecyclerView) findViewById(R.id.artist_recyclerview);
        // specify that grid will consist of 2 columns
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        // provide our CustomSpanSizeLookup which determines how many spans each item in grid will occupy
        gridLayoutManager.setSpanSizeLookup(new CustomSpanSizeLookup());
        // provide our GridLayoutManager to the view
        recyclerView.setLayoutManager(gridLayoutManager);
        // this is fake list of images
        List<Integer> imageResList = getMockedImageList();
        // finally, provide adapter to the recycler view
        ArtistViewAdapter adapter = new ArtistViewAdapter(imageResList);
        recyclerView.setAdapter(adapter);
    }

    private List<Integer> getMockedImageList() {
        // fake images list, you'd need to upload your own image resources
        List<Integer> imageResList = new ArrayList<Integer>();

        imageResList.add(R.drawable.orin_notification_image);
        imageResList.add(R.drawable.bg_greenfield);
        imageResList.add(R.drawable.bg_marine);
        return imageResList;
    }


    private static class CustomSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        @Override
        public int getSpanSize(int i) {
            if(i == 0 || i == 1) {
                // grid items on positions 0 and 1 will occupy 2 spans of the grid
                return 2;
            } else {
                // the rest of the items will behave normally and occupy only 1 span
                return 1;
            }
        }
    }
}
