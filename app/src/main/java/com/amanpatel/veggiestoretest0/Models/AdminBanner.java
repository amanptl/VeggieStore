package com.amanpatel.veggiestoretest0.Models;

public class AdminBanner {
    public String id;
    public String image;
    public String link;

    public AdminBanner(String id, String image, String link) {
        this.id = id;
        this.image = image;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
