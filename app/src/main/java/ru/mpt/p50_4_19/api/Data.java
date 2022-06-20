package ru.mpt.p50_4_19.api;

import android.graphics.Bitmap;

public class Data {
    public static Data inst = new Data();

    private String text;
    private Bitmap img;

    public void setData(String t, Bitmap i)
    {
        text = t;
        img = i;
    }

    public String getText()
    {
        return text;
    }

    public Bitmap getImg()
    {
        return img;
    }
}
