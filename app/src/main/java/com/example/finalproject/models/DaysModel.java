package com.example.finalproject.models;

public class DaysModel {
    String timing, subject, professor;

    public DaysModel(String timing, String subject, String professor) {
        this.timing = timing;
        this.subject = subject;
        this.professor = professor;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
