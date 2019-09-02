package com.mehdi.blankactivity.DATAS;

public class CHILD {

    private String uid, name, photo, QRcode;

    public CHILD() {
    }

    public CHILD(String uid, String name, String photo, String QRcode) {
        this.name = name;
        this.photo = photo;
        this.uid = uid;
        this.QRcode = QRcode;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
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

