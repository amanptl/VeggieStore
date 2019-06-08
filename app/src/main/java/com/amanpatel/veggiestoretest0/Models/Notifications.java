package com.amanpatel.veggiestoretest0.Models;

public class Notifications {
    private String id;
    private String Message;
    private String image;
    private String dealerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public Notifications(String id, String message, String image, String dealerId) {
        this.id = id;
        Message = message;
        this.image = image;
        this.dealerId = dealerId;
    }
}
