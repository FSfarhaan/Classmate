package com.example.finalproject.models;

import com.example.finalproject.R;

public class showAvailableGroupsModel {
    public int image;
    public String groupName, groupText, groupId, adminName;

    public showAvailableGroupsModel() {

    }
    public showAvailableGroupsModel(int image, String groupName, String groupText) {
        this.image = (image > 0) ? image : R.drawable.avatar_img;
        this.groupName = groupName;
        this.groupText = groupText;
    }

    public showAvailableGroupsModel(String groupName, String groupId, String adminName) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.adminName = adminName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}