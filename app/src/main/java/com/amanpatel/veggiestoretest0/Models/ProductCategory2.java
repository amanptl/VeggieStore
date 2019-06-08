package com.amanpatel.veggiestoretest0.Models;

public class ProductCategory2 {
    public String id;
    public String cat1id;
    public String name;
    public String visible;
    public String dealerid;

    public ProductCategory2(String id, String cat1id, String name, String visible, String dealerid) {
        this.id = id;
        this.cat1id = cat1id;
        this.name = name;
        this.visible = visible;
        this.dealerid = dealerid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat1id() {
        return cat1id;
    }

    public void setCat1id(String cat1id) {
        this.cat1id = cat1id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getDealerid() {
        return dealerid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }
}
