package com.glamour.faith.Model;

public class Notification {

    public Notification() {
    }

    String userid, name, profileImage,description, postImage,date,time,church;
    public Notification(String postImage,String userid, String name, String profileImage, String description, String date, String time) {
        this.userid = userid;
        this.name = name;
        this.profileImage = profileImage;
        this.description = description;
        this.postImage = postImage;
        this.date = date;
        this.time = time;


    }

    public String getUserid() {
        return userid;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
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

}
