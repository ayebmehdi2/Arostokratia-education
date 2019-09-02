package com.mehdi.blankactivity.DATAS;

public class NOTE {

    private String detail, desk;
    private int time, imageDesk;


    public NOTE(String detail, String desk, int imageDesk, int time) {

        this.detail = detail;
        this.desk = desk;
        this.imageDesk = imageDesk;
        this.time = time;
    }

    public NOTE() {
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
