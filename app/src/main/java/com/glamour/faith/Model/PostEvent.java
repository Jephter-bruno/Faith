package com.glamour.faith.Model;


public class PostEvent {
    private String username;
    private String church;
    private String profile;
    private String venue;
    private String date;
    private String time;
    private String event;

    private int position;

    public PostEvent() {
    }

    public PostEvent(int position){
        this.position = position;
    }
    public PostEvent(String username, String profile , String venue, String date, String time, String church, String event) {

        this.username = username;
        this.profile = profile;
        this.church = church;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.event = event;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
