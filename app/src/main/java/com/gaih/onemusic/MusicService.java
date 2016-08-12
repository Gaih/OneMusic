package com.gaih.onemusic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gaih on 2016/7/14.
 */

public class MusicService extends Service {
    private MediaPlayer player;
    private int currentPosition;
    private int duration;
    private android.support.v4.app.NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //startForeground(1, mBuilder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }


    public void seekToPosition(int position) {
        player.seekTo(position);
    }

    private void updateSeekBar() {
        if (player.isPlaying()) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        duration = player.getDuration();
                        currentPosition = player.getCurrentPosition();
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putInt("currentPosition", currentPosition);
                        msg.setData(bundle);
                        MainActivity.handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            timer.schedule(task, 300, 1000);
        }

    }


    private void notification(String name, String singer, Bitmap bitmap) {
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("" + name)
                .setContentText(name + "--" + singer)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.app);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mBuilder.setProgress(duration, currentPosition, false);
                mNotificationManager.notify(1, mBuilder.build());
                Log.d(currentPosition + "", "" + duration);
                while (currentPosition == duration) {
                    mBuilder.setProgress(0, 0, false);
                }
            }
        };
        timer.schedule(task, 0, 1000);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        1,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        startForeground(1, mBuilder.build());

    }


    public void playMusic(String uri, String name, String singer, Bitmap bitmap,long album) {
        try {
            if (player != null) {
                player.release();
            }
            player = new MediaPlayer();
            player.setDataSource(uri);
            player.prepare();
            player.start();
            updateSeekBar();
            notification(name, singer, bitmap);
        } catch (IllegalStateException e) {
            System.out.print(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pauseMusic() {

        if (player.isPlaying()) {
            player.pause();
        } else if (!player.isPlaying()) {
            player.start();
        }
    }

    public void replayMusic() {
        player.stop();
        stopForeground(true);
    }


    private class MyBinder extends Binder implements Iservice {
        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        @Override
        public void callPlayMusic(String uri, String name, String singer, Bitmap bitmap,long album) {
            playMusic(uri, name, singer, bitmap,album);
        }

        @Override
        public void callReplayMusic() {
            replayMusic();
        }

        @Override
        public void callSeekToPosition(int position) {
            seekToPosition(position);
        }


    }

}
