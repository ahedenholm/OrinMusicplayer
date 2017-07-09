package com.orin.anders.orinmusicplayer;


public class Song {

    private long id;
    private String title;
    private String artist;
    private String length;


    public Song(long songID, String songTitle, String songArtist, String songLength){
        this.id = songID;
        this.title = songTitle;
        this.artist = songArtist;
        this.length = songLength;

    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getLength() {
        return length;
    }
}
