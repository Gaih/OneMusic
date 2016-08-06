package com.gaih.onemusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }


    public void playMusic(String uri) {
        try {
            if (player != null) {
                player.release();
            }
            player = new MediaPlayer();
            player.setDataSource(uri);
            player.prepare();
            player.start();
            updateSeekBar();

        } catch (IllegalStateException e) {
            System.out.print(e.getMessage());
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    public void seekToPosition(int position) {
        player.seekTo(position);
    }

    private void updateSeekBar() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                duration = player.getDuration();
                currentPosition = player.getCurrentPosition();
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);
                msg.setData(bundle);
                MainActivity.handler.sendMessage(msg);
            }
        };
        timer.schedule(task, 300, 1000);

    }
    public void pauseMusic() {
        if (player.isPlaying()){
            player.pause();
        }else if (!player.isPlaying()){
            player.start();
        }
    }

    public void replayMusic() {
        player.start();
    }


    private class MyBinder extends Binder implements Iservice {
        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        @Override
        public void callPlayMusic(String uri) {
            playMusic(uri);
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
