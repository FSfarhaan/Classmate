package com.example.finalproject.models;

public class MarksModel {
    String title, m4Marks, m4Out, mpMarks, mpOut, osMarks, osOut, pyMarks, pyOut, dbMarks, dbOut;

    public MarksModel(){}

    public MarksModel(String title, String m4Marks, String mpMarks, String osMarks, String pyMarks, String dbMarks) {
        this.title = title;
        this.m4Marks = m4Marks;
        this.mpMarks = mpMarks;
        this.osMarks = osMarks;
        this.pyMarks = pyMarks;
        this.dbMarks = dbMarks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getM4Marks() {
        return m4Marks;
    }

    public void setM4Marks(String m4Marks) {
        this.m4Marks = m4Marks;
    }

    public String getM4Out() {
        return m4Out;
    }

    public void setM4Out(String m4Out) {
        this.m4Out = m4Out;
    }

    public String getMpMarks() {
        return mpMarks;
    }

    public void setMpMarks(String mpMarks) {
        this.mpMarks = mpMarks;
    }

    public String getMpOut() {
        return mpOut;
    }

    public void setMpOut(String mpOut) {
        this.mpOut = mpOut;
    }

    public String getOsMarks() {
        return osMarks;
    }

    public void setOsMarks(String osMarks) {
        this.osMarks = osMarks;
    }

    public String getOsOut() {
        return osOut;
    }

    public void setOsOut(String osOut) {
        this.osOut = osOut;
    }

    public String getPyMarks() {
        return pyMarks;
    }

    public void setPyMarks(String pyMarks) {
        this.pyMarks = pyMarks;
    }

    public String getPyOut() {
        return pyOut;
    }

    public void setPyOut(String pyOut) {
        this.pyOut = pyOut;
    }

    public String getDbMarks() {
        return dbMarks;
    }

    public void setDbMarks(String dbMarks) {
        this.dbMarks = dbMarks;
    }

    public String getDbOut() {
        return dbOut;
    }

    public void setDbOut(String dbOut) {
        this.dbOut = dbOut;
    }
}
