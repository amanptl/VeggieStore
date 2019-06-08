package com.amanpatel.veggiestoretest0.Models;

import java.util.List;

public class OrderHistory {
    private OrderMaster objordermaster;
    private List<OrderItems> objorderitem;

    public OrderHistory(OrderMaster objordermaster, List<OrderItems> objorderitem) {
        this.objordermaster = objordermaster;
        this.objorderitem = objorderitem;
    }

    public OrderMaster getObjordermaster() {
        return objordermaster;
    }

    public void setObjordermaster(OrderMaster objordermaster) {
        this.objordermaster = objordermaster;
    }

    public List<OrderItems> getObjorderitem() {
        return objorderitem;
    }

    public void setObjorderitem(List<OrderItems> objorderitem) {
        this.objorderitem = objorderitem;
    }
}
