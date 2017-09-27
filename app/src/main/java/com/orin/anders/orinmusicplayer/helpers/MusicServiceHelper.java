package com.orin.anders.orinmusicplayer.helpers;

import com.orin.anders.orinmusicplayer.Song;

public class MusicServiceHelper {

    public static final int NOTIFICATION_ID = 2814;
    public static final int REPEAT_ALL = 0;
    public static final int REPEAT_ONE = 1;
    public static final int SHUFFLE = 2;
    public static int playbackMode;
    public static Song selectedSong;
    public static Song previousSong;

    public static void setPlaybackMode(int REPEAT_MODE){
        MusicServiceHelper.playbackMode = REPEAT_MODE;
    }

}
