package com.glamour.faith.Model;

public class Comments {
   public String uid;
    public String commment;
    public String date;
    public String time;
    public String username;
    public String profile;
    public String church;

    public Comments() {
    }

    public Comments(String uid, String commment, String date, String time, String username, String profile,String church) {
        this.uid = uid;
        this.commment = commment;
        this.date = date;
        this.time = time;
        this.username = username;
        this.profile = profile;
        this.church = church;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCommment() {
        return commment;
    }

    public void setCommment(String commment) {
        this.commment = commment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }
}
