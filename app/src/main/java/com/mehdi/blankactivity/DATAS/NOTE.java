package com.mehdi.blankactivity.DATAS;

public class NOTE {

    private String childName, detail, desk;
    private int time, imageDesk;


    public NOTE(String childName, String detail, String desk, int imageDesk, int time) {
        this.childName = childName;
        this.detail = detail;
        this.desk = desk;
        this.imageDesk = imageDesk;
        this.time = time;
    }

    public NOTE() {
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public void setImageDesk(int imageDesk) {
        this.imageDesk = imageDesk;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public String getDetail() {
        return detail;
    }

    public String getDesk() {
        return desk;
    }

    public int getImageDesk() {
        return imageDesk;
    }

    public int getTime() {
        return time;
    }
}
