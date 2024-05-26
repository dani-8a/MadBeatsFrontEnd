package com.example.madbeatsfrontend.entity;

import java.util.List;

public class Feedback {
    private String idMessage;
    private List<String> subject;
    private String message;

    public Feedback(String idMessage, List<String> subject, String message) {
        super();
        this.idMessage = idMessage;
        this.subject = subject;
        this.message = message;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Feedback [idMessage=" + idMessage + ", subject=" + subject + ", message=" + message + "]";
    }
}
