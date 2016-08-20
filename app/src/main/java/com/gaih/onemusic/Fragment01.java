package com.gaih.onemusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by gaih on 2016/8/20.
 */

public class Fragment01 extends Fragment {

    private View view;
    private ListView lv;
    private static SeekBar sbar;
    private Myconn myconn;
    private Button stop;
    private Iservice iservice;
    private ArrayList<Music> musicList = new ArrayList<Music>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab01,container,false);
        lv = (ListView) view.findViewById(R.id.lv);
        initView();
        return view;
    }
    private void initView() {

        Intent intent = new Intent(this.getContext(), MusicService.class);
        getActivity().startService(intent);
        myconn = new Myconn();
        getActivity().bindService(intent, myconn, BIND_AUTO_CREATE);

        ScanMusic scanMusic = new ScanMusic();
        scanMusic.scanMusic(this.getContext(), musicList);
        MusicAdapter adapter = new MusicAdapter(this.getContext(), R.layout.music_item, musicList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Music music = musicList.get(position);
                iservice.callPlayMusic(position, musicList);
            }
        });
        sbar = (SeekBar) getActivity().findViewById(R.id.seekBar1);
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
                Log.d("aaaa"+seekBar.getProgress(),"aaaa");
            }
        });

        stop =(Button) getActivity().findViewById(R.id.palying);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iservice.callPauseMusic();
            }
        });
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
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            sbar.setMax(duration);
            sbar.setProgress(currentPosition);

        }
    };

}
