package com.amanpatel.veggiestoretest0.Models;

public class Cart {
    private String id;
    private String custid;
    private String Proid;
    private String priceid;
    private String areaid;
    private String Quantity;
    private String isspecial;
    private String instruction;


    public Cart(String id, String custid, String proid, String priceid, String areaid, String quantity, String isspecial, String instruction) {
        this.id = id;
        this.custid = custid;
        Proid = proid;
        this.priceid = priceid;
        this.areaid = areaid;
        Quantity = quantity;
        this.isspecial = isspecial;
        this.instruction = instruction;
    }

    public String getId() {
        return id;
    }

    public String getCustid() {
        return custid;
    }

    public String getProid() {
        return Proid;
    }

    public String getPriceid() {
        return priceid;
    }

    public String getAreaid() {
        return areaid;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getIsspecial() {
        return isspecial;
    }

    public String getInstruction() {
        return instruction;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public void setProid(String Proid) {
        this.Proid = Proid;
    }

    public void setPriceid(String priceid) {
        this.priceid = priceid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public void setIsspecial(String isspecial) {
        this.isspecial = isspecial;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
