package com.amanpatel.veggiestoretest0.Models;

public class OrderMaster {
    private String id;
    private String Name;
    private String Email;
    private String Address;
    private String City;
    private String State;
    private String District;
    private String Postcode;
    private String Landmark;
    private String Phone;
    private String BName;
    private String BEmail;
    private String BAddress;
    private String BCity;
    private String BState;
    private String BPostCode;
    private String BLandmark;
    private String BPhone;
    private String uid;
    private String Subtotal;
    private String Grandtotal;
    private String Delivery;
    private String discount;
    private String Orderdate;
    private String Orderip;
    private String Total;
    private String OrderStatus;
    private String GiftCodeType;
    private String ordernumber;
    private String unread;
    private String Admincomments;
    private String AutoProcessDone;
    private String ModifyDate;
    private String ModifyIp;
    private String DiscountDESC;
    private String FromWallet;
    private String Express;
    private String DeliveryOn;
    private String paymentmode;
    private String Ordertime;
    private String dealerid;
    private String israting;
    private String rating;
    private String review;


    public OrderMaster(String name, String email, String address, String city, String state, String district, String postcode, String landmark, String phone, String uid, String subtotal, String grandtotal, String delivery, String discount, String orderDate,String giftCodeType, String discountDESC, String fromWallet, String deliveryOn, String paymentmode, String dealerid) {
        this.id = "-1";
        this.Name = name;
        this.Email = email;
        this.Address = address;
        this.City = city;
        this.State = state;
        this.District = district;
        this.Postcode = postcode;
        this.Landmark = landmark;
        this.Phone = phone;
        this.BName = name;
        this.BEmail = email;
        this.BAddress = address;
        this.BCity = city;
        this.BState = state;
        this.BPostCode = postcode;
        this.BLandmark = landmark;
        this.BPhone = phone;
        this.uid = uid;
        this.Subtotal = subtotal;
        this.Grandtotal = grandtotal;
        this.Delivery = delivery;
        this.discount = discount;
        this.Orderdate = orderDate;
        this.Orderip = "";
        this.Total = grandtotal;
        this.OrderStatus = "0";
        this.GiftCodeType = giftCodeType;
        this.ordernumber = "";
        this.unread = "";
        this.Admincomments = "";
        this.AutoProcessDone = "";
        this.ModifyDate = "";
        this.ModifyIp = "";
        this.DiscountDESC = discountDESC;
        this.FromWallet = fromWallet;
        this.Express = "";
        this.DeliveryOn = deliveryOn;
        this.paymentmode = paymentmode;
        this.Ordertime = "";
        this.dealerid = dealerid;
        this.israting = "";
        this.rating = "";
        this.review = "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getDistrict() {
        return District;
    }

    public String getPostcode() {
        return Postcode;
    }

    public String getLandmark() {
        return Landmark;
    }

    public String getPhone() {
        return Phone;
    }

    public String getBName() {
        return BName;
    }

    public String getBEmail() {
        return BEmail;
    }

    public String getBAddress() {
        return BAddress;
    }

    public String getBCity() {
        return BCity;
    }

    public String getBState() {
        return BState;
    }

    public String getBPostCode() {
        return BPostCode;
    }

    public String getBLandmark() {
        return BLandmark;
    }

    public String getBPhone() {
        return BPhone;
    }

    public String getUid() {
        return uid;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public String getGrandtotal() {
        return Grandtotal;
    }

    public String getDelivery() {
        return Delivery;
    }

    public String getDiscount() {
        return discount;
    }

    public String getOrderdate() {
        return Orderdate;
    }

    public String getOrderip() {
        return Orderip;
    }

    public String getTotal() {
        return Total;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getGiftCodeType() {
        return GiftCodeType;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public String getUnread() {
        return unread;
    }

    public String getAdmincomments() {
        return Admincomments;
    }

    public String getAutoProcessDone() {
        return AutoProcessDone;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public String getModifyIp() {
        return ModifyIp;
    }

    public String getDiscountDESC() {
        return DiscountDESC;
    }

    public String getFromWallet() {
        return FromWallet;
    }

    public String getExpress() {
        return Express;
    }

    public String getDeliveryOn() {
        return DeliveryOn;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public String getOrdertime() {
        return Ordertime;
    }

    public String getDealerid() {
        return dealerid;
    }

    public String getIsrating() {
        return israting;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public void setState(String State) {
        this.State = State;
    }

    public void setDistrict(String District) {
        this.District = District;
    }

    public void setPostcode(String Postcode) {
        this.Postcode = Postcode;
    }

    public void setLandmark(String Landmark) {
        this.Landmark = Landmark;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public void setBName(String BName) {
        this.BName = BName;
    }

    public void setBEmail(String BEmail) {
        this.BEmail = BEmail;
    }

    public void setBAddress(String BAddress) {
        this.BAddress = BAddress;
    }

    public void setBCity(String BCity) {
        this.BCity = BCity;
    }

    public void setBState(String BState) {
        this.BState = BState;
    }

    public void setBPostCode(String BPostCode) {
        this.BPostCode = BPostCode;
    }

    public void setBLandmark(String BLandmark) {
        this.BLandmark = BLandmark;
    }

    public void setBPhone(String BPhone) {
        this.BPhone = BPhone;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSubtotal(String Subtotal) {
        this.Subtotal = Subtotal;
    }

    public void setGrandtotal(String Grandtotal) {
        this.Grandtotal = Grandtotal;
    }

    public void setDelivery(String Delivery) {
        this.Delivery = Delivery;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setOrderdate(String Orderdate) {
        this.Orderdate = Orderdate;
    }

    public void setOrderip(String Orderip) {
        this.Orderip = Orderip;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public void setGiftCodeType(String GiftCodeType) {
        this.GiftCodeType = GiftCodeType;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public void setAdmincomments(String Admincomments) {
        this.Admincomments = Admincomments;
    }

    public void setAutoProcessDone(String AutoProcessDone) {
        this.AutoProcessDone = AutoProcessDone;
    }

    public void setModifyDate(String ModifyDate) {
        this.ModifyDate = ModifyDate;
    }

    public void setModifyIp(String ModifyIp) {
        this.ModifyIp = ModifyIp;
    }

    public void setDiscountDESC(String DiscountDESC) {
        this.DiscountDESC = DiscountDESC;
    }

    public void setFromWallet(String FromWallet) {
        this.FromWallet = FromWallet;
    }

    public void setExpress(String Express) {
        this.Express = Express;
    }

    public void setDeliveryOn(String DeliveryOn) {
        this.DeliveryOn = DeliveryOn;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public void setOrdertime(String Ordertime) {
        this.Ordertime = Ordertime;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }

    public void setIsrating(String israting) {
        this.israting = israting;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
