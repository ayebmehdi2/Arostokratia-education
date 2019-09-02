package com.mehdi.blankactivity.DATAS;

public class MESSAGE {

    private String uid, image, name, message;
    private int time;

    public MESSAGE(String uid, String image, String name, String message, int time) {
        this.uid = uid;
        this.image = image;
        this.time = time;
        this.name = name;
        this.message = message;
    }

    public MESSAGE() {
    }

    public String getUid() {
        return uid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
