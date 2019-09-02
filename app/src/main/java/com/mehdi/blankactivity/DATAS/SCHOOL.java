package com.mehdi.blankactivity.DATAS;

public class SCHOOL {

    private String uid, name, pass, email, image;

    public SCHOOL(String uid, String name, String pass, String email, String  i) {
        this.uid = uid;
        this.name = name;
        this.pass = pass;
        this.email = email;
        image = i;
    }

    public SCHOOL() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
