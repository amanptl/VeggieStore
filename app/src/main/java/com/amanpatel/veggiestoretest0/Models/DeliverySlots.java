package com.amanpatel.veggiestoretest0.Models;

public class DeliverySlots {
    public String id;
    public String title;
    public String timeslot;
    public String active;
    public String totalorders;
    public String isactive;
    public String Dealerid;

    public DeliverySlots(String id, String title, String timeslot, String active, String totalorders, String isactive, String dealerid) {
        this.id = id;
        this.title = title;
        this.timeslot = timeslot;
        this.active = active;
        this.totalorders = totalorders;
        this.isactive = isactive;
        this.Dealerid = dealerid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTotalorders() {
        return totalorders;
    }

    public void setTotalorders(String totalorders) {
        this.totalorders = totalorders;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getDealerid() {
        return Dealerid;
    }

    public void setDealerid(String dealerid) {
        this.Dealerid = dealerid;
    }
}
