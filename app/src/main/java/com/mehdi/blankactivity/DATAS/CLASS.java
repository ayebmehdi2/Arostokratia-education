package com.mehdi.blankactivity.DATAS;

public class CLASS {

    private String uid , name;
    private int time;

    public CLASS(String uid, String name, int time) {
        this.uid = uid;
        this.name = name;
        this.time = time;
    }

    public CLASS() {
    }

    public int getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }
}


