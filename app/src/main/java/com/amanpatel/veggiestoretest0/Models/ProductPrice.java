package com.amanpatel.veggiestoretest0.Models;

public class ProductPrice {
    private String id;
    private String Weight;
    private String Description;
    private String APrice;
    private String DPrice;
    private String productid;
    private String Unit;

    public ProductPrice(String id, String weight, String description, String APrice, String DPrice, String productid, String unit) {
        this.id = id;
        this.Weight = weight;
        this.Description = description;
        this.APrice = APrice;
        this.DPrice = DPrice;
        this.productid = productid;
        this.Unit = unit;
    }

    public String getId() {
        return id;
    }

    public String getWeight() {
        return Weight;
    }

    public String getDescription() {
        return Description;
    }

    public String getAPrice() {
        return APrice;
    }

    public String getDPrice() {
        return DPrice;
    }

    public String getProductid() {
        return productid;
    }

    public String getUnit() {
        return Unit;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setAPrice(String APrice) {
        this.APrice = APrice;
    }

    public void setDPrice(String DPrice) {
        this.DPrice = DPrice;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }
}
