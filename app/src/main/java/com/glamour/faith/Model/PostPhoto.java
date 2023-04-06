package com.glamour.faith.Model;

public class PostPhoto {

    public PostPhoto() {
    }

    String designation,event,venue,day,eventType,type,postedChurch, postedname,postedProfile,postVideo, userid, name, profileImage,description, postImage,date,time,church,scriptureContent, scriptureBook,postmode,link,confidentiality;
    public PostPhoto(String designation,String event, String venue, String day, String eventType, String type, String postedChurch, String postedname, String postedProfile,String confidentiality,String postVideo,String link,String userid,String postmode, String name, String profileImage, String description, String postImage,String scriptureContent,String scriptureBook, String date, String time) {
        this.userid = userid;
        this.name = name;
        this.type = type;
        this.profileImage = profileImage;
        this.description = description;
        this.postImage = postImage;
        this.date = date;
        this.time = time;
        this.scriptureContent = scriptureContent;
        this.scriptureBook = scriptureBook;
        this.postmode = postmode;
        this.postVideo = postVideo;
        this.link = link;
        this.confidentiality = confidentiality;
        this.postedChurch = postedChurch;
        this.postedname = postedname;
        this.postedProfile = postedProfile;
        this.event = event;
        this.venue = venue;
        this.day = day;
        this.eventType = eventType;
        this.designation = designation;


    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostedChurch() {
        return postedChurch;
    }

    public void setPostedChurch(String postedChurch) {
        this.postedChurch = postedChurch;
    }

    public String getPostedname() {
        return postedname;
    }

    public void setPostedname(String postedname) {
        this.postedname = postedname;
    }

    public String getPostedProfile() {
        return postedProfile;
    }

    public void setPostedProfile(String postedProfile) {
        this.postedProfile = postedProfile;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPostmode() {
        return postmode;
    }

    public void setPostmode(String postmode) {
        this.postmode = postmode;
    }

    public String getScriptureContent() {
        return scriptureContent;
    }

    public void setScriptureContent(String scriptureContent) {
        this.scriptureContent = scriptureContent;
    }

    public String getScriptureBook() {
        return scriptureBook;
    }

    public void setScriptureBook(String scriptureBook) {
        this.scriptureBook = scriptureBook;
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
