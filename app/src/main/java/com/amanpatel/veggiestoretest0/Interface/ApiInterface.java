package com.amanpatel.veggiestoretest0.Interface;

import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.AdminBanner;
import com.amanpatel.veggiestoretest0.Models.Areas;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.City;
import com.amanpatel.veggiestoretest0.Models.DeliverySlots;
import com.amanpatel.veggiestoretest0.Models.Franchise;
import com.amanpatel.veggiestoretest0.Models.Holiday;
import com.amanpatel.veggiestoretest0.Models.NewAddress;
import com.amanpatel.veggiestoretest0.Models.Notifications;
import com.amanpatel.veggiestoretest0.Models.OrderHistory;
import com.amanpatel.veggiestoretest0.Models.OrderMaster;
import com.amanpatel.veggiestoretest0.Models.ProductCategory1;
import com.amanpatel.veggiestoretest0.Models.ProductCategory2;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Models.User;
import com.amanpatel.veggiestoretest0.Models.UserEdit;
import com.amanpatel.veggiestoretest0.Models.UserRegister;
import com.amanpatel.veggiestoretest0.Models.Voucher;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("homebanners/")
    Call<List<AdminBanner>> getAdminBanner();

    @GET("City/")
    Call<List<City>> getCity();

    @GET("Area/")
    Call<List<Areas>> getAreas(@Query("cityid") String cityid);

    @GET("AreaDetail/")
    Call<Areas> getArea(@Query("areaid") String areaid);

    @GET("productcategory1/")
    Call<List<ProductCategory1>> getProductCategory1();

    @GET("productcategory2/")
    Call<List<ProductCategory2>> getProductCategory2(@Query("category1id") String category1id);

    @POST("customers/")
    Call<ResponseBody> register(@Body UserRegister data);

    @GET("CustomerLogin/")
    Call<User> login(@Query("Email") String email, @Query("Pass") String pass);

    @GET("addressbooks/")
    Call<List<AddressBook>> getAddress(@Query("customerid") String customerid, @Query("areaname") String areaname);

    @POST("addressbooks/")
    Call<AddressBook> addAddress(@Body NewAddress data);

    @PUT("addressbooks/")
    Call<AddressBook> editAddress(@Body NewAddress data);

    @DELETE("addressbooks/")
    Call<AddressBook> deleteAddress(@Query("customerid") String cid,@Query("areaid") String aid);

    @PUT("DefaultAddress/")
    Call<AddressBook> makeDefault(@Query("addressid") String addressid,@Query("customerid") String customerid, @Query("areaname") String areaname);

    @GET("customers/")
    Call<User> getUser(@Query("customerid") String customerid);

    @POST("customers/")
    Call<User> editUser(@Body UserEdit data);

    @PUT("CustomerPasswordReset/")
    Call<User> changePassword(@Query("customerid") String customerid, @Query("oldpass") String oldpass, @Query("newpass") String newpass);

    @GET("DealerBanner/")
    Call<List<AdminBanner>> getDealerBanners(@Query("areaid") String areaid);

    @GET("DealerProducts/")
    Call<List<ProductsWithPrice>> getProducts(@Query("areaid") String areaid, @Query("category2id") String category2id);

    @GET("SingleProduct/")
    Call<ProductsWithPrice> getProduct(@Query("productid") String productid);

    @POST("Carts/")
    Call<Cart> addToCart(@Body Cart cart);

    @GET("Carts/")
    Call<List<Cart>> getCart(@Query("customerid") String customerid, @Query("areaid") String areaid);

    @PUT("Carts/")
    Call<Cart> editCartItem(@Query("cartid") String cartid, @Body Cart cart);

    @DELETE("Carts/")
    Call<Cart> deleteCartItem(@Query("cartid") String cartid);

    @GET("CartTotal/")
    Call<Double> getCartTotal(@Query("customerid") String customerid, @Query("areaid") String areaid);

    @POST("GuestCartTotal/")
    Call<Double> getGCartTotal(@Body List<Cart> cartlist);

    @GET("DeliverySlot/")
    Call<List<DeliverySlots>> getDeliverySlots(@Query("areaid") String areaid);

    @GET("vouchers/")
    Call<Voucher> getVoucher(@Query("areaid") String areaid, @Query("vouchercode") String vouchercode);

    @POST("OrderMaster/")
    Call<OrderMaster> postOrderMaster(@Body OrderMaster orderMaster, @Query("areaid") String areaid);

    @GET("OrderHistory/")
    Call<List<OrderHistory>> getOrderHistory(@Query("customerid") String customerid);

    @GET("SingleOrdermasterItems/")
    Call<OrderHistory> getOrderItems(@Query("OrderMasterID") String OrderMasterID);

    @GET("Search/")
    Call<List<ProductsWithPrice>> searchProducts(@Query("search") String search, @Query("areaid") String areaid);

    @GET("Notification/")
    Call<List<Notifications>> getNotifications(@Query("areaid") String areaid);

    @POST("enquiries/")
    Call<ResponseBody> getFranchise(@Body Franchise enquiry);

    @GET("Holidays/")
    Call<List<Holiday>> getHolidays(@Query("areaid") String areaid);

    @POST("Review/{id}")
    Call<ResponseBody> submitReview(@Path("id") String id, @Query("rating") String rating, @Query("review") String review);

    @GET("CustomerStatus/")
    Call<Boolean> getVerification(@Query("customerid") String customerid);

    @GET("ForgotPassword/")
    Call<String> getPassword(@Query("Email") String email);

    @POST("MergeCart/")
    Call<ResponseBody> mergeCart(@Query("customerid") String customerid, @Body List<Cart> cartlist);

    @GET("Wallet/")
    Call<Double> getWallet(@Query("customerid") String customerid);

    @POST("ReturnRequest/")
    Call<ResponseBody> returnOrder(@Query("OrdermasterID") String OrdermasterID);
}
