package com.gaih.onemusic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                        Fragment01.handler.sendMessage(msg);
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
                .setSmallIcon(R.drawable.icon);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    mBuilder.setProgress(duration, currentPosition, false);
                    mNotificationManager.notify(1, mBuilder.build());
                    Log.d(currentPosition + "", "" + duration);
                    while (currentPosition == duration) {
                        mBuilder.setProgress(0, 0, false);
                    }
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


    public void playMusic(int position, final ArrayList<Music> muList) {
        if (position==muList.size()){
            position=0;
        }
        player = new MediaPlayer();
        player.reset();
        try {
            player.setDataSource(muList.get(position).getUri());
            player.prepare();
            player.start();
            updateSeekBar();
            notification(muList.get(position).getName(), muList.get(position).getSinger(), muList.get(position).getBitmap());
            final int finalPosition = position;
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playMusic(finalPosition + 1, muList);
                    Log.d("1111", "方法执行");

                }
            });

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
        public void callPlayMusic(final int position, final ArrayList<Music> mList) {
            playMusic(position, mList);
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
