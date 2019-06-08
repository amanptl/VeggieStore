package com.amanpatel.veggiestoretest0;

import android.util.Log;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.Areas;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.DeliverySlots;
import com.amanpatel.veggiestoretest0.Models.Holiday;
import com.amanpatel.veggiestoretest0.Models.OrderHistory;
import com.amanpatel.veggiestoretest0.Models.OrderItems;
import com.amanpatel.veggiestoretest0.Models.OrderMaster;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.Models.Voucher;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {

    private int flag = 1;
    private MutableLiveData<String> internetToast = new MutableLiveData<>();
    private MutableLiveData<String> operationToast = new MutableLiveData<>();
    private OrderMaster orderMaster = new OrderMaster("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    private MutableLiveData<ValidationFlag> valFlag = new MutableLiveData<>();
    private MutableLiveData<Areas> area = new MutableLiveData<>();
    private MutableLiveData<String> cartTotal = new MutableLiveData<>();
    private MutableLiveData<List<Cart>> mCart = new MutableLiveData<>();
    private MutableLiveData<ProductsWithPrice> cartProduct = new MutableLiveData<>();
    private MutableLiveData<List<AddressBook>> addresses = new MutableLiveData<>();
    private MutableLiveData<OrderHistory> orderItems = new MutableLiveData<>();
    private MutableLiveData<List<DeliverySlots>> deliverySlots = new MutableLiveData<>();
    private MutableLiveData<Voucher> voucher = new MutableLiveData<>();
    private MutableLiveData<String> wallet = new MutableLiveData<>();
    private MutableLiveData<Integer> orderFlag = new MutableLiveData<>();
    private MutableLiveData<List<Holiday>> holiday = new MutableLiveData<>();
    private MutableLiveData<Integer> reviewFlag = new MutableLiveData<>();
    private MutableLiveData<String> customerStatus = new MutableLiveData<>();
    private String verificationStatus = "";

    public LiveData<String> getInternetToast() {
        return internetToast;
    }

    public LiveData<String> getOperationToast() {
        return operationToast;
    }

    public void setName(@NonNull CharSequence s) {
        String nameInput = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (nameValidator(nameInput)) {
            orderMaster.setName(nameInput);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("nameInvalid"));
            flag = 1;
        }
    }

    public void setEmail(@NonNull CharSequence s) {
        orderMaster.setEmail(s.toString());
    }

    public void setAddress(AddressBook address) {
        orderMaster.setAddress(address.getAddress());
        orderMaster.setCity(address.getCity());
        orderMaster.setState(address.getState());
        orderMaster.setDistrict(address.getDistrict());
        orderMaster.setPostcode(address.getPostCode());
        orderMaster.setLandmark(address.getLandmark());
        orderMaster.setPhone(address.getPhone());

    }

    public void setUidDid(String uid, String did) {
        orderMaster.setUid(uid);
        orderMaster.setDealerid(did);
    }

    public void setDiscountInfo(String codeType, String desc) {
        orderMaster.setGiftCodeType(codeType);
        orderMaster.setDiscountDESC(desc);
    }

    public void setDeliveryDetails(String date, String paymentMode, String orderDate) {
        orderMaster.setDeliveryOn(date);
        orderMaster.setPaymentmode(paymentMode);
        orderMaster.setOrderdate(orderDate);
    }

    public void setAmounts(String subTotal, String discount, String delivery, String fromWallet, String grandTotal) {
        orderMaster.setSubtotal(subTotal);
        orderMaster.setDiscount(discount);
        orderMaster.setDelivery(delivery);
        orderMaster.setFromWallet(fromWallet);
        orderMaster.setGrandtotal(grandTotal);
        orderMaster.setTotal(grandTotal);
    }


    private static boolean nameValidator(String name) {
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    public LiveData<ValidationFlag> getValFlag() {
        return valFlag;
    }


    public LiveData<List<Cart>> getCart(String custid, String areaid) {
        loadCart(custid, areaid);
        return mCart;
    }

    private void loadCart(String custid, String areaid) {
        final ApiInterface cartRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Cart>> call = cartRetro.getCart(custid, areaid);

        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                if (response.code() == 200) {
                    mCart.setValue(response.body());
                } else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    public LiveData<ProductsWithPrice> getSingleProduct(String pid) {
        loadSingleProduct(pid);
        return cartProduct;
    }

    private void loadSingleProduct(String pid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductsWithPrice> call = productRetro.getProduct(pid);

        call.enqueue(new Callback<ProductsWithPrice>() {
            @Override
            public void onResponse(Call<ProductsWithPrice> call, Response<ProductsWithPrice> response) {
                if (response.code() == 200)
                    cartProduct.setValue(response.body());
                else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<ProductsWithPrice> call, Throwable throwable) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<Areas> getAreaDetails(String areaid) {
        loadAreaDetails(areaid);
        return area;
    }

    private void loadAreaDetails(final String areaid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Areas> call = productRetro.getArea(areaid);

        call.enqueue(new Callback<Areas>() {
            @Override
            public void onResponse(Call<Areas> call, Response<Areas> response) {
                if (response.code() == 200) {
                    area.setValue(response.body());
                } else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<Areas> call, Throwable throwable) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<String> getCartTotal(String cusotmerid, String areaid) {
        getTotal(cusotmerid, areaid);
        return cartTotal;
    }

    private void getTotal(String cusotmerid, String areaid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Double> call = productRetro.getCartTotal(cusotmerid, areaid);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.code() == 200) {
                    cartTotal.setValue(response.body().toString());
                    orderMaster.setSubtotal(cartTotal.getValue());
                } else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable throwable) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<String> getWalletAmount(String cusotmerid) {
        getWallet(cusotmerid);
        return wallet;
    }

    private void getWallet(String cusotmerid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Double> call = productRetro.getWallet(cusotmerid);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.code() == 200) {
                    wallet.setValue(response.body().toString());
                } else {
                    operationToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable throwable) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }


    //---------------------------------------------Verification------------------------------------------

    public LiveData<String> getVerification(String id){
        getCustomerStatus(id);
        return customerStatus;
    }

    private void getCustomerStatus(String id){
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Boolean> call = productRetro.getVerification(id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200){
                    String s = response.body().toString();
                    customerStatus.setValue(response.body().toString());
                } else {
                    operationToast.setValue("Server error");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                internetToast.setValue("Cannot connect to server. Check connection.");
            }
        });
    }

    //--------------------------------------------Address-------------------------------------------

    public LiveData<List<AddressBook>> getAddresses(String cid, String areaname) {
        loadAddresses(cid, areaname);
        return addresses;
    }

    private void loadAddresses(final String cid, String areaname) {
        final ApiInterface addressRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<AddressBook>> call = addressRetro.getAddress(cid, areaname);
        call.enqueue(new Callback<List<AddressBook>>() {
            @Override
            public void onResponse(@NonNull Call<List<AddressBook>> call, @NonNull Response<List<AddressBook>> response) {
                if (response.code() == 200) {
                    addresses.setValue(response.body());
                } else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AddressBook>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<List<DeliverySlots>> getDeliverySlots(String areaid) {
        loadDeliverySlots(areaid);
        return deliverySlots;
    }

    private void loadDeliverySlots(String areaid) {
        final ApiInterface addressRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<DeliverySlots>> call = addressRetro.getDeliverySlots(areaid);

        call.enqueue(new Callback<List<DeliverySlots>>() {
            @Override
            public void onResponse(@NonNull Call<List<DeliverySlots>> call, @NonNull Response<List<DeliverySlots>> response) {
                if (response.code() == 200) {
                    deliverySlots.setValue(response.body());
                } else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DeliverySlots>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<Voucher> getVoucher(String areaid, String vouchercode) {
        voucher = new MutableLiveData<>();
        loadVoucher(areaid, vouchercode);
        return voucher;
    }

    private void loadVoucher(String areaid, String vouchercode) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Voucher> call = productRetro.getVoucher(areaid, vouchercode);

        call.enqueue(new Callback<Voucher>() {
            @Override
            public void onResponse(Call<Voucher> call, Response<Voucher> response) {
                if (response.code() == 200)
                    voucher.setValue(response.body());
                else if (response.code() == 404) {
                    operationToast.setValue("Code not found.");
                } else {
                    operationToast.setValue("Server error");
                }
            }

            @Override
            public void onFailure(Call<Voucher> call, Throwable throwable) {
                internetToast.setValue("Cannot connect to server. Check connection.");
            }
        });
    }

    public LiveData<Integer> validate(String areaid) {
        if (flag == 0) {
            placeOrderMaster(areaid);
        } else
            operationToast.setValue("Enter correct details.");
        return orderFlag;
    }

    private void placeOrderMaster(String areaid) {
        if (!orderMaster.getDeliveryOn().equals(""))
            sendOrderMaster(areaid);
    }

    private void sendOrderMaster(String areaid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<OrderMaster> call = productRetro.postOrderMaster(orderMaster, areaid);

        call.enqueue(new Callback<OrderMaster>() {
            @Override
            public void onResponse(Call<OrderMaster> call, Response<OrderMaster> response) {
                    if (response.code() == 201) {
                        operationToast.setValue("Order placed.");
                        orderFlag.setValue(response.code());
                    } else
                        operationToast.setValue("Please try again.");
            }

            @Override
            public void onFailure(Call<OrderMaster> call, Throwable throwable) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<OrderHistory> getOrderItems(String id) {
        loadOrderItems(id);
        return orderItems;
    }

    private void loadOrderItems(final String id) {
        final ApiInterface orderRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<OrderHistory> call = orderRetro.getOrderItems(id);

        call.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                if (response.code() == 200)
                    orderItems.setValue(response.body());
                else {
                    internetToast.setValue("Server error.");
                    operationToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<OrderHistory> call, Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    public LiveData<List<Holiday>> getHolidays(String areaid) {
        loadHolidays(areaid);
        return holiday;
    }

    private void loadHolidays(String areaid) {
        final ApiInterface cartRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Holiday>> call = cartRetro.getHolidays(areaid);

        call.enqueue(new Callback<List<Holiday>>() {
            @Override
            public void onResponse(@NonNull Call<List<Holiday>> call, @NonNull Response<List<Holiday>> response) {
                if (response.code() == 200) {
                    holiday.setValue(response.body());
                } else {
                    operationToast.setValue("Server error.");
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Holiday>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    //----------------------------------------Review----------------------------------------------------------------------

    public LiveData<Integer> setReview(String orderid, String rating, String review) {
        submitReview(orderid, rating, review);
        return reviewFlag;
    }

    private void submitReview(String orderid, String rating, String review) {
        final ApiInterface orderRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = orderRetro.submitReview(orderid, rating, review);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    reviewFlag.setValue(response.code());
                    operationToast.setValue("Thanks for your feedback.");
                } else {
                    internetToast.setValue("Server error.");
                    operationToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }

    //------------------------------------------------Return Order------------------------------------------

    public LiveData<Integer> returnOrder(String orderid) {
        submitReturn(orderid);
        return reviewFlag;
    }

    private void submitReturn(String orderid) {
        final ApiInterface orderRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = orderRetro.returnOrder(orderid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    reviewFlag.setValue(response.code());
                    operationToast.setValue("Your request has been submitted.");
                } else {
                    internetToast.setValue("Server error.");
                    operationToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }



}
