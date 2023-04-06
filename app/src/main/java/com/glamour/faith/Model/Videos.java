package com.glamour.faith.Model;

public class Videos {
    private String name;
    private String profileImage;
    private String description;
    private String postVideo;
    private String date;
    private String time;
    private String church;

    private int position;

    public Videos() {
    }
    public Videos(int position){
        this.position = position;
    }
    public Videos(String name, String profileImage , String description, String postVideo, String date, String time, String church){
        this.name = name;
        this.profileImage = profileImage;
        this.description = description;
        this.postVideo = postVideo;
        this.date = date;
        this.time = time;
        this.church = church;

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

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
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

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
