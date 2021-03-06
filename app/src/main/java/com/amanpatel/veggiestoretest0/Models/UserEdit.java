package com.amanpatel.veggiestoretest0.Models;

public class UserEdit {
    public String id;
    public String Name;
    public String Address;
    public String Landmark;
    public String City;
    public String State;
    public String PostCode;
    public String Country;
    public String Email;
    public String Password;
    public String Phone;
    public String Status;
    public String Addeddate;
    public String Addedip;
    public String Modifydate;
    public String Modifyip;
    public String numConfirmed;
    public String areaid;
    public String deviceid;


    public UserEdit(String id, String name, String phone) {
        this.id = id;
        this.Name = name;
        this.Address = "";
        this.Landmark = "";
        this.City = "";
        this.State = "";
        this.PostCode = "";
        this.Country = "";
        this.Email = "";
        this.Password = "";
        this.Phone = phone;
        this.Status = "true";
        this.Addeddate = "";
        this.Addedip = "";
        this.Modifydate = "";
        this.Modifyip = "";
        this.numConfirmed = "true";
        this.areaid = "0";
        this.deviceid = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        this.Landmark = landmark;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        this.PostCode = postCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        this.Country = country;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getAddeddate() {
        return Addeddate;
    }

    public void setAddeddate(String addeddate) {
        this.Addeddate = addeddate;
    }

    public String getAddedip() {
        return Addedip;
    }

    public void setAddedip(String addedip) {
        this.Addedip = addedip;
    }

    public String getModifydate() {
        return Modifydate;
    }

    public void setModifydate(String modifydate) {
        this.Modifydate = modifydate;
    }

    public String getModifyip() {
        return Modifyip;
    }

    public void setModifyip(String modifyip) {
        this.Modifyip = modifyip;
    }

    public String getNumConfirmed() {
        return numConfirmed;
    }

    public void setNumConfirmed(String numConfirmed) {
        this.numConfirmed = numConfirmed;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
