package com.mehdi.blankactivity.DATAS;

public class POST {

    private String uid, subject, desc, image, type;
    private int time, numComment, color;

    public POST(String uid, String subject, String desc, String image,
                int numComment, int time, String type, int color) {
        this.uid = uid;
        this.subject = subject;
        this.desc = desc;
        this.image = image;
        this.type = type;
        this.numComment = numComment;
        this.time = time;
        this.color = color;
    }


    public POST() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNumComment() {
        return numComment;
    }

    public int getTime() {
        return time;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public String getSubject() {
        return subject;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }
}
