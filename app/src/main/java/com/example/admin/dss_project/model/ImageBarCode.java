package com.example.admin.dss_project.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImageBarCode implements Serializable {

    private String id = null;

    private String data = null;

    private String dataImage = null;

    private String dateTime = null;

    private Bitmap bitmap = null;

    private boolean mcheck = false;

    public ImageBarCode(String id, String data, String dataImage, String dateTime) {
        this.id = id;
        this.data = data;
        this.dataImage = dataImage;
        this.dateTime = dateTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isMcheck() {
        return mcheck;
    }

    public void setMcheck(boolean mcheck) {
        this.mcheck = mcheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
