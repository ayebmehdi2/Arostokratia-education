package com.mehdi.blankactivity.DATAS;

import android.graphics.Bitmap;

public class CHILDcode {


    private String uid, name, photo;

    private Bitmap QRcode;

    public CHILDcode() {
    }

    public CHILDcode(String uid, String name, String photo, Bitmap QRcode) {
        this.name = name;
        this.photo = photo;
        this.uid = uid;
        this.QRcode = QRcode;
    }

    public Bitmap getQRcode() {
        return QRcode;
    }

    public void setQRcode(Bitmap QRcode) {
        this.QRcode = QRcode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

}
