package com.orin.anders.orinmusicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orin.anders.orinmusicplayer.R;
import com.orin.anders.orinmusicplayer.Song;
import com.orin.anders.orinmusicplayer.TimeConverter;

import java.util.ArrayList;

/*
SongAdapter class should inflate a linearlayout with a songlist onto the activity_main layout
 */
public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songArrayList;
    private LayoutInflater layoutInflater;

    public SongAdapter(Context c, ArrayList<Song> theSongs) {
        songArrayList = theSongs;
        layoutInflater = LayoutInflater.from(c);
    }

    public SongAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return songArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout) layoutInflater.inflate(R.layout.song, parent, false);

        //get title and artist views
        TextView songView = (TextView) songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
        TextView lengthView = (TextView) songLay.findViewById(R.id.song_length);

        //get song using position
        Song currSong = songArrayList.get(position);

        //TODO doesn't gray out onClick, but when list has been scrolled out of view and into view again
        /*
        if (currSong == MusicServiceHelper.selectedSong){
            songView.setTextColor(Color.GRAY);
            artistView.setTextColor(Color.GRAY);
            lengthView.setTextColor(Color.GRAY);
        }
        */

        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        lengthView.setText(TimeConverter.convertDuration(Long.parseLong(currSong.getLength())));

        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
}
