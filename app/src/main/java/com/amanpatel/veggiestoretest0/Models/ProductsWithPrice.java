package com.amanpatel.veggiestoretest0.Models;

import java.util.List;

public class ProductsWithPrice {
    private Products objpro;
    private List<ProductPrice> objprice;

    public List<ProductPrice> getPrices() {
        return objprice;
    }

    public void setPrices(List<ProductPrice> objprice) {
        this.objprice = objprice;
    }

    public ProductsWithPrice(Products objpro, List<ProductPrice> objprice) {
        this.objpro = objpro;
        this.objprice = objprice;
    }

    public Products getProduct() {
        return objpro;
    }

    public void setProduct(Products objpro) {
        this.objpro = objpro;
    }
}
