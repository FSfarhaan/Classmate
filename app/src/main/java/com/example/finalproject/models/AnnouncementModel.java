package com.example.finalproject.models;

import android.net.Uri;

import com.example.finalproject.R;

public class AnnouncementModel {
    String title, name, time, content, imgUrl, senderImg;

    public AnnouncementModel(){}

    public AnnouncementModel(String title, String name, String time, String content, String senderImg) {
        this.title = title;
        this.name = name;
        this.time = time;
        this.content = content;
        this.senderImg = senderImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderImg() {
        return senderImg;
    }

    public void setSenderImg(String senderImg) {
        this.senderImg = senderImg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
