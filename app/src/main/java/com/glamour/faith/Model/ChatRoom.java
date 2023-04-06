package com.glamour.faith.Model;

public class ChatRoom {
    public String name,profileImage,useriD,chatID,date,time,church,topic,whoisInvited,designation,maritalstatus;

    public ChatRoom() {
    }

    public ChatRoom(String maritalstatus,String name, String profileImage, String useriD, String chatID, String date, String time, String church, String topic, String whoisInvited, String designation) {
        this.name = name;
        this.profileImage = profileImage;
        this.useriD = useriD;
        this.chatID = chatID;
        this.date = date;
        this.time = time;
        this.church = church;
        this.topic = topic;
        this.whoisInvited = whoisInvited;
        this.designation = designation;
        this.maritalstatus=maritalstatus;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
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

    public String getUseriD() {
        return useriD;
    }

    public void setUseriD(String useriD) {
        this.useriD = useriD;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
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


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getWhoisInvited() {
        return whoisInvited;
    }

    public void setWhoisInvited(String whoisInvited) {
        this.whoisInvited = whoisInvited;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
