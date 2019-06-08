package com.amanpatel.veggiestoretest0.Models;

public class ProductCategory1 {
    public String id;
    public String name;
    public boolean visible;
    public String image;

    public ProductCategory1(String id, String name, boolean visible, String image) {
        this.id = id;
        this.name = name;
        this.visible = visible;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
