package com.gaih.onemusic;

import android.graphics.Bitmap;

/**
 * Created by gaih on 2016/8/4.
 */
public class Music {
    private String name;
    private String singer;
    private Bitmap bitmap;
    private String uri;

    public Music(String name,String singer,String uri,Bitmap bitmap){
        this.name = name;
        this.singer = singer;
        this.bitmap = bitmap;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getUri() {
        return uri;
    }
}
