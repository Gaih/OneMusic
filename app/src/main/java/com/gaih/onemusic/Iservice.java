package com.gaih.onemusic;

import android.graphics.Bitmap;

/**
 * Created by gaih on 2016/7/27.
 */

public interface Iservice {
    public void callPlayMusic(String uri,String name, String singer, Bitmap bitmap,long album);
    public void callPauseMusic();
    public void callReplayMusic();
    public void callSeekToPosition(int position);



    }
