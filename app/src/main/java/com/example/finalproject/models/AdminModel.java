package com.example.finalproject.models;

public class AdminModel{
    private String fullName, adminId, contactNo, password;

    public AdminModel(String fullName, String adminId, String contactNo, String password) {
        this.fullName = fullName;
        this.adminId = adminId;
        this.contactNo = contactNo;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
