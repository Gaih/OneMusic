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
    private long album;

    public Music(String name,String singer,String uri,Bitmap bitmap,long album){
        this.name = name;
        this.singer = singer;
        this.bitmap = bitmap;
        this.uri = uri;
        this.album = album;
    }

    public long getAlbum() {
        return album;
    }

    public void setAlbum(long album) {
        this.album = album;
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
