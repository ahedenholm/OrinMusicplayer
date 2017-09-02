package com.orin.anders.orinmusicplayer;

public class MusicServiceHelper {

    public static final int NOTIFICATION_ID = 2814;
    public static final int REPEAT_ALL = 0;
    public static final int REPEAT_ONE = 1;
    public static final int SHUFFLE = 2;
    public static int repeatMode;
    public static Song selectedSong;
    public static Song previousSong;

    public static void setRepeatMode(int REPEAT_MODE){
        MusicServiceHelper.repeatMode = REPEAT_MODE;
    }

}
