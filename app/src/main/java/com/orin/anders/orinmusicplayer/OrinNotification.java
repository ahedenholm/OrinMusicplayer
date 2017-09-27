package com.orin.anders.orinmusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import com.orin.anders.orinmusicplayer.activities.Main_Activity;

public class OrinNotification {

    private Bitmap orinNotificationIcon;
    private Context context;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public OrinNotification(Context context) {
        this.context = context;
    }

    //status bar notification adjusting with playback changes
    private void setupPlaybackNotification(MediaPlayer mediaPlayer) {
        notificationBuilder = new NotificationCompat.Builder(context);

        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

		orinNotificationIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.orin_notification_image);
    
        notificationBuilder.setContentTitle(MusicServiceHelper.selectedSong.getArtist())
                .setContentText(MusicServiceHelper.selectedSong.getTitle())
                .setLargeIcon(orinNotificationIcon);

        if (mediaPlayer.isPlaying())
            notificationBuilder.setSmallIcon(android.R.drawable.ic_media_play);
        else notificationBuilder.setSmallIcon(android.R.drawable.ic_media_pause);

        Intent returnToAppIntent = new Intent(context, Main_Activity.class);
        returnToAppIntent.setAction(Intent.ACTION_MAIN);
        returnToAppIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, returnToAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
    }

    public Notification foregroundNotification(MediaPlayer mediaPlayer) {
        setupPlaybackNotification(mediaPlayer);
        return (notificationBuilder.build());
    }

    public void foregroundNotificationUpdate(MediaPlayer mediaPlayer) {
        setupPlaybackNotification(mediaPlayer);
        notificationManager.notify(
                MusicServiceHelper.NOTIFICATION_ID,
                notificationBuilder.build());
    }


}
