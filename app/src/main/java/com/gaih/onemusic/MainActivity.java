package com.gaih.onemusic;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private Button palying;
    private Iservice iservice;
    private Myconn myconn;
    private static SeekBar sbar;
    private ListView lv;
    private String uri;
    private List<Music> musicList = new ArrayList<Music>();


    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            Log.d("duration", "" + duration);
            Log.d("currentPosition", "" + currentPosition);

            sbar.setMax(duration);
            sbar.setProgress(currentPosition);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScanMusic scanMusic = new ScanMusic();
        scanMusic.scanMusic(MainActivity.this,musicList);

        palying = (Button)findViewById(R.id.palying);
        lv = (ListView) findViewById(R.id.lv);
        MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.music_item, musicList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Music music = musicList.get(position);
                uri = music.getUri();
                iservice.callPlayMusic(uri);
            }
        });


        sbar = (SeekBar) findViewById(R.id.seekBar1);
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        myconn = new Myconn();
        bindService(intent, myconn, BIND_AUTO_CREATE);

        sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iservice.callSeekToPosition(seekBar.getProgress());
            }
        });

    }


    public void click1(View view) {
        if (uri != null) {
            iservice.callPlayMusic(uri);
        } else {
            Snackbar snackbar = Snackbar.make(view, "没有音乐文件", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    public void click2(View view) {
        iservice.callPauseMusic();
        if (palying.getText().equals("暂停")){
            palying.setText("播放");
        }else if (palying.getText().equals("播放")){
            palying.setText("暂停");

        }
    }

    public void click3(View view) {
        iservice.callReplayMusic();
    }


    @Override
    protected void onDestroy() {
        unbindService(myconn);
        super.onDestroy();
    }

    private class Myconn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iservice = (Iservice) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
