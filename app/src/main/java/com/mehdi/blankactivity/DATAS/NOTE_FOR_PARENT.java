package com.mehdi.blankactivity.DATAS;

public class NOTE_FOR_PARENT {

    private String uid, kidName, kidImage, noteDetail, noteDesk;
    private int noteDeskImage, time;

    public NOTE_FOR_PARENT(String uid, String kidName, String kidImage,
                           String noteDetail, String noteDesk, int noteDeskImage, int time) {
        this.uid = uid;
        this.kidName = kidName;
        this.kidImage = kidImage;
        this.noteDesk = noteDesk;
        this.noteDeskImage = noteDeskImage;
        this.time = time;
        this.noteDetail = noteDetail;
    }

    public String getNoteDetail() {
        return noteDetail;
    }

    public void setNoteDetail(String noteDetail) {
        this.noteDetail = noteDetail;
    }

    public NOTE_FOR_PARENT() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public void setKidImage(String kidImage) {
        this.kidImage = kidImage;
    }

    public void setNoteDesk(String noteDesk) {
        this.noteDesk = noteDesk;
    }


    public void setNoteDeskImage(int noteDeskImage) {
        this.noteDeskImage = noteDeskImage;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getKidName() {
        return kidName;
    }

    public String getKidImage() {
        return kidImage;
    }

    public String getNoteDesk() {
        return noteDesk;
    }

    public int getNoteDeskImage() {
        return noteDeskImage;
    }

    public int getTime() {
        return time;
    }
}
