package com.glamour.faith.Model;

public class ChatRoomChats {
    String uid,chat,date,time,profile,username,church,dates,type,chatimage;

    public ChatRoomChats() {
    }

    public ChatRoomChats(String chatimage,String type,String dates,String uid, String chat, String date, String time, String profile, String username, String church) {
        this.uid = uid;
        this.chat = chat;
        this.date = date;
        this.time = time;
        this.profile = profile;
        this.username = username;
        this.church = church;
        this.dates = dates;
        this.type = type;
        this.chatimage = chatimage;
    }

    public String getChatimage() {
        return chatimage;
    }

    public void setChatimage(String chatimage) {
        this.chatimage = chatimage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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
}
