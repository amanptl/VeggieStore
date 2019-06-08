package com.amanpatel.veggiestoretest0.Models;

public class Franchise {
    private String id;
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String city;
    private String country;
    private String message;
    private String addeddate;
    private String addedip;
    private String unread;

    public Franchise(String fname, String lname, String phone, String email, String city, String country, String message) {
        this.id = "0";
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.country = country;
        this.message = message;
        this.addeddate = "";
        this.addedip = "";
        this.unread = "0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(String addeddate) {
        this.addeddate = addeddate;
    }

    public String getAddedip() {
        return addedip;
    }

    public void setAddedip(String addedip) {
        this.addedip = addedip;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }
}
