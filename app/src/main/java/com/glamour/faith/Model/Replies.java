package com.glamour.faith.Model;

public class Replies {
    String uid,reply,date, dates,time, username,profile,church;

    public Replies() {
    }

    public Replies(String uid, String reply, String date, String dates, String time, String username, String profile, String church) {
        this.uid = uid;
        this.reply = reply;
        this.date = date;
        this.dates = dates;
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

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
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
