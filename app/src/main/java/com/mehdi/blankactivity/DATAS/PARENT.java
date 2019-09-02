package com.mehdi.blankactivity.DATAS;

public class PARENT {

    private String name, email, pass, uid, numChild, image;

    public PARENT(String name, String email, String pass, String uid, String numChild, String i) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.uid = uid;
        this.numChild = numChild;
        image = i;
    }

    public PARENT() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNumChild(String numChild) {
        this.numChild = numChild;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getUid() {
        return uid;
    }

    public String getNumChild() {
        return numChild;
    }
}
