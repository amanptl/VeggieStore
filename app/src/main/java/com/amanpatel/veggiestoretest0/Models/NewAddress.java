package com.amanpatel.veggiestoretest0.Models;

public class NewAddress {
    private String id;
    private String Name;
    private String Address;
    private String Landmark;
    private String City;
    private String State;
    private String District;
    private String PostCode;
    private String Email;
    private String Phone;
    private String addeddate;
    private String addedip;
    private String modifydate;
    private String modifyip;
    private String isreg;
    private String Cid;
    private String mobiledefault;
    private String MobileTitle;

    public NewAddress(String name, String address, String landmark, String city, String district, String postCode, String email, String phone, String cid, String mobiledefault) {
        this.id = "0";
        this.Name = name;
        this.Address = address;
        this.Landmark = landmark;
        this.City = city;
        this.State = "";
        this.District = district;
        this.PostCode = postCode;
        this.Email = email;
        this.Phone = phone;
        this.addeddate = "";
        this.addedip = "";
        this.modifydate = "";
        this.modifyip = "";
        this.isreg = "";
        this.Cid = cid;
        this.mobiledefault = mobiledefault;
        this.MobileTitle = "";
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

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        this.District = district;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        this.PostCode = postCode;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
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

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getModifyip() {
        return modifyip;
    }

    public void setModifyip(String modifyip) {
        this.modifyip = modifyip;
    }

    public String getIsreg() {
        return isreg;
    }

    public void setIsreg(String isreg) {
        this.isreg = isreg;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        this.Cid = cid;
    }

    public String getMobiledefault() {
        return mobiledefault;
    }

    public void setMobiledefault(String mobiledefault) {
        this.mobiledefault = mobiledefault;
    }

    public String getMobileTitle() {
        return MobileTitle;
    }

    public void setMobileTitle(String mobileTitle) {
        this.MobileTitle = mobileTitle;
    }
}
