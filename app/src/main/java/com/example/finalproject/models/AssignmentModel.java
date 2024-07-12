package com.example.finalproject.models;

public class AssignmentModel {
    public String assignmentName, assignmentDate, assignmentUrl;
    long timestamp;

    public AssignmentModel() {}

    public AssignmentModel(String assignmentName, String assignmentUrl) {
        this.assignmentName = assignmentName;
        this.assignmentUrl = assignmentUrl;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(String assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAssignmentUrl() {
        return assignmentUrl;
    }

    public void setAssignmentUrl(String assignmentUrl) {
        this.assignmentUrl = assignmentUrl;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
