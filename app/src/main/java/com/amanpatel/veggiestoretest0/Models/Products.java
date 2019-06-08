package com.amanpatel.veggiestoretest0.Models;

import java.util.List;

public class Products {
    private String id;
    private String Title;
    private String HindiTitle;
    private String cat1id;
    private String cat2id;
    private String DESCabout;
    private String DESCbenifits;
    private String DESChowtouse;
    private String UOM;
    private String visible;
    private String featured;
    private String sortorder;
    private String cityid;
    private String dealerid;
    private String type;
    private String Image;
    private List<ProductPrice> productPrice;

    public Products(String id, String title, String hindiTitle, String cat1id, String cat2id, String DESCabout, String DESCbenifits, String DESChowtouse, String UOM, String visible, String featured, String sortorder, String cityid, String dealerid, String type, String image) {
        this.id = id;
        this.Title = title;
        this.HindiTitle = hindiTitle;
        this.cat1id = cat1id;
        this.cat2id = cat2id;
        this.DESCabout = DESCabout;
        this.DESCbenifits = DESCbenifits;
        this.DESChowtouse = DESChowtouse;
        this.UOM = UOM;
        this.visible = visible;
        this.featured = featured;
        this.sortorder = sortorder;
        this.cityid = cityid;
        this.dealerid = dealerid;
        this.type = type;
        this.Image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getHindiTitle() {
        return HindiTitle;
    }

    public String getCat1id() {
        return cat1id;
    }

    public String getCat2id() {
        return cat2id;
    }

    public String getDESCabout() {
        return DESCabout;
    }

    public String getDESCbenifits() {
        return DESCbenifits;
    }

    public String getDESChowtouse() {
        return DESChowtouse;
    }

    public String getUOM() {
        return UOM;
    }

    public String getVisible() {
        return visible;
    }

    public String getFeatured() {
        return featured;
    }

    public String getSortorder() {
        return sortorder;
    }

    public String getCityid() {
        return cityid;
    }

    public String getDealerid() {
        return dealerid;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return Image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setHindiTitle(String HindiTitle) {
        this.HindiTitle = HindiTitle;
    }

    public void setCat1id(String cat1id) {
        this.cat1id = cat1id;
    }

    public void setCat2id(String cat2id) {
        this.cat2id = cat2id;
    }

    public void setDESCabout(String DESCabout) {
        this.DESCabout = DESCabout;
    }

    public void setDESCbenifits(String DESCbenifits) {
        this.DESCbenifits = DESCbenifits;
    }

    public void setDESChowtouse(String DESChowtouse) {
        this.DESChowtouse = DESChowtouse;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
