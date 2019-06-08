package com.amanpatel.veggiestoretest0.Models;


public class Areas {
    public String id;
    public String cityid;
    public String Name;
    public boolean visible;
    public String shippingcharge;
    public String minimumcharge;

    public Areas(String id, String cityid, String name, boolean visible, String shippingcharge, String minimumcharge) {
        this.id = id;
        this.cityid = cityid;
        this.Name = name;
        this.visible = visible;
        this.shippingcharge = shippingcharge;
        this.minimumcharge = minimumcharge;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getShippingcharge() {
        return shippingcharge;
    }

    public void setShippingcharge(String shippingcharge) {
        this.shippingcharge = shippingcharge;
    }

    public String getMinimumcharge() {
        return minimumcharge;
    }

    public void setMinimumcharge(String minimumcharge) {
        this.minimumcharge = minimumcharge;
    }
}
