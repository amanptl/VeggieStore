package com.amanpatel.veggiestoretest0.Models;

public class City {
    public String id;
    public String Name;
    public boolean visible;

    public City(String id, String name, boolean visible) {
        this.id = id;
        this.Name = name;
        this.visible = visible;
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
        Name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
