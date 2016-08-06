package com.gaih.onemusic;

/**
 * Created by gaih on 2016/7/27.
 */

public interface Iservice {
    public void callPlayMusic(String uri);
    public void callPauseMusic();
    public void callReplayMusic();
    public void callSeekToPosition(int position);



    }
