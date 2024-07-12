package com.example.finalproject.models;

public class StudentModel {
    private String fullName, studentId, division, rollNo, contactNo, password, additionalDetails;

    public StudentModel() {}

    public StudentModel(String fullName, String studentId, String division, String rollNo, String contactNo, String password) {
        this.fullName = fullName;
        this.studentId = studentId;
        this.division = division;
        this.rollNo = rollNo;
        this.contactNo = contactNo;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getPhoneNo() {
        return contactNo;
    }

    public void setPhoneNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
