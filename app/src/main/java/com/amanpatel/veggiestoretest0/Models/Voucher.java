package com.amanpatel.veggiestoretest0.Models;

public class Voucher {
    private String id;
    private String Code;
    private String Type;
    private String VoucherAmount;
    private String MinimumAmount;
    private String PublishDate;
    private String ExpiryDate;
    private String Towhom;
    private String AdminNote;
    private String Sent;
    private String SenttoAll;
    private String areaid;

    public Voucher(String id, String code, String type, String voucherAmount, String minimumAmount, String publishDate, String expiryDate, String towhom, String adminNote, String sent, String senttoAll, String areaid) {
        this.id = id;
        this.Code = code;
        this.Type = type;
        this.VoucherAmount = voucherAmount;
        this.MinimumAmount = minimumAmount;
        this.PublishDate = publishDate;
        this.ExpiryDate = expiryDate;
        this.Towhom = towhom;
        this.AdminNote = adminNote;
        this.Sent = sent;
        this.SenttoAll = senttoAll;
        this.areaid = areaid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getVoucherAmount() {
        return VoucherAmount;
    }

    public void setVoucherAmount(String voucherAmount) {
        this.VoucherAmount = voucherAmount;
    }

    public String getMinimumAmount() {
        return MinimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.MinimumAmount = minimumAmount;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        this.PublishDate = publishDate;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.ExpiryDate = expiryDate;
    }

    public String getTowhom() {
        return Towhom;
    }

    public void setTowhom(String towhom) {
        this.Towhom = towhom;
    }

    public String getAdminNote() {
        return AdminNote;
    }

    public void setAdminNote(String adminNote) {
        this.AdminNote = adminNote;
    }

    public String getSent() {
        return Sent;
    }

    public void setSent(String sent) {
        this.Sent = sent;
    }

    public String getSenttoAll() {
        return SenttoAll;
    }

    public void setSenttoAll(String senttoAll) {
        this.SenttoAll = senttoAll;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }
}
