package com.gaih.onemusic;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaih on 2016/7/27.
 */

public interface Iservice {
    public void callPlayMusic(int position, ArrayList<Music> mList);
    public void callPauseMusic();
    public void callReplayMusic();
    public void callSeekToPosition(int position);



    }
