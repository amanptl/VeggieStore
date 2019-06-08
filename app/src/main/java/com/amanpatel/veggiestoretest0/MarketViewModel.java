package com.amanpatel.veggiestoretest0;

import android.text.TextUtils;
import android.util.Log;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.AdminBanner;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.Franchise;
import com.amanpatel.veggiestoretest0.Models.Notifications;
import com.amanpatel.veggiestoretest0.Models.OrderHistory;
import com.amanpatel.veggiestoretest0.Models.ProductCategory1;
import com.amanpatel.veggiestoretest0.Models.UserRegister;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketViewModel extends ViewModel {
    public ObservableField<String> areaName = new ObservableField<>();
    public ObservableField<String> cName = new ObservableField<>("User");
    private MutableLiveData<String> operationToast = new MutableLiveData<>();
    private MutableLiveData<String> internetToast = new MutableLiveData<>();
    private MutableLiveData<List<AdminBanner>> banner = new MutableLiveData<>();
    private MutableLiveData<List<ProductCategory1>> category = new MutableLiveData<>();
    private MutableLiveData<List<Cart>> mCart = new MutableLiveData<>();
    private MutableLiveData<List<AddressBook>> addresses = new MutableLiveData<>();
    private MutableLiveData<List<OrderHistory>> orders = new MutableLiveData<>();
    private MutableLiveData<String> wallet = new MutableLiveData<>();
    private MutableLiveData<List<Notifications>> notifications = new MutableLiveData<>();
    private Franchise enquiry = new Franchise("", "", "", "", "", "", "");
    private MutableLiveData<Integer> enquiryFlag = new MutableLiveData<>();
    private int flag = 1;
    private MutableLiveData<ValidationFlag> valFlag = new MutableLiveData<>();

    public void setAreaName(String areaName) {
        this.areaName.set(areaName);
    }

    public void setCName(String cName) {
        this.cName.set(cName);
    }

    public LiveData<String> getInternetToast() {
        return internetToast;
    }

    public LiveData<String> getOperationToast() {
        return operationToast;
    }

    public LiveData<List<AdminBanner>> getBanner(String aid) {
        loadBanner(aid);
        return banner;
    }

    private void loadBanner(String aid) {
        final ApiInterface bannerRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<AdminBanner>> call = bannerRetro.getDealerBanners(aid);

        call.enqueue(new Callback<List<AdminBanner>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdminBanner>> call, @NonNull Response<List<AdminBanner>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().size() != 0) {
                        banner.setValue(response.body());
                    } else
                        operationToast.setValue("No data found.");
                } else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<AdminBanner>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch banners. Please check your internet.");
            }
        });
    }

    public LiveData<List<ProductCategory1>> getCategory1() {
        loadCategory();
        return category;
    }

    private void loadCategory() {
        final ApiInterface catRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<ProductCategory1>> call = catRetro.getProductCategory1();

        call.enqueue(new Callback<List<ProductCategory1>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductCategory1>> call, @NonNull Response<List<ProductCategory1>> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().size() != 0) {
                        category.setValue(response.body());
                    } else
                        operationToast.setValue("No data found.");
                } else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductCategory1>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch categories. Please check your internet.");
            }
        });
    }
    //------------------------------------Cart------------------------------------------------------

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
                if (response.code() == 200)
                    mCart.setValue(response.body());
                else
                    internetToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                internetToast.setValue("Failed to connect.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    //------------------------------------ADDRESS FRAG---------------------------------------------------------------------------------

    public LiveData<List<AddressBook>> getAddresses(String cid, String areaname) {
        loadAddresses(cid, areaname);
        return addresses;
    }

    private void loadAddresses(String cid, String areaname) {
        final ApiInterface addressRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<AddressBook>> call = addressRetro.getAddress(cid, areaname);

        call.enqueue(new Callback<List<AddressBook>>() {
            @Override
            public void onResponse(@NonNull Call<List<AddressBook>> call, @NonNull Response<List<AddressBook>> response) {
                if (response.code() == 200) {
                    addresses.setValue(response.body());
                } else
                    internetToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<AddressBook>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect. Check your connection.");
            }
        });
    }

    public void deleteAddress(String cid, String aid) {
        final ApiInterface addrRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<AddressBook> call = addrRetro.deleteAddress(cid, aid);
        call.enqueue(new Callback<AddressBook>() {
            @Override
            public void onResponse(@NonNull Call<AddressBook> call, @NonNull Response<AddressBook> response) {
                if (response.code() == 200)
                    operationToast.setValue("Address deleted.");
                else {
                    operationToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressBook> call, @NonNull Throwable t) {
                internetToast.setValue("Operation failed. Check connection.");
            }
        });
    }

    public void makeAddressDefault(String addressid, String customerid, String areaname) {
        final ApiInterface addrRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<AddressBook> call = addrRetro.makeDefault(addressid, customerid, areaname);

        call.enqueue(new Callback<AddressBook>() {
            @Override
            public void onResponse(@NonNull Call<AddressBook> call, @NonNull Response<AddressBook> response) {
                if (response.code() == 200) {
                    operationToast.setValue("Default address changed.");
                } else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<AddressBook> call, @NonNull Throwable t) {
                internetToast.setValue("Operation failed. Check connection.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    //---------------------------------------------Order History-----------------------------------------------------------

    public LiveData<List<OrderHistory>> getOrders(String customerid) {
        loadOrders(customerid);
        return orders;
    }

    private void loadOrders(String customerid) {
        final ApiInterface ordersRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<OrderHistory>> call = ordersRetro.getOrderHistory(customerid);

        call.enqueue(new Callback<List<OrderHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderHistory>> call, @NonNull Response<List<OrderHistory>> response) {
                if (response.code() == 200) {
                    orders.setValue(response.body());
                } else {
                    operationToast.setValue("Server error. Try to refresh.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderHistory>> call, @NonNull Throwable t) {
                operationToast.setValue("Cannot connect. Check your connection and refresh.");
            }
        });
    }

    //--------------------------------------------Notification-----------------------------------------------------------------

    public LiveData<List<Notifications>> getNotifications(String areaid) {
        loadNotifications(areaid);
        return notifications;
    }

    private void loadNotifications(String areaid) {
        final ApiInterface notiRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Notifications>> call = notiRetro.getNotifications(areaid);

        call.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(@NonNull Call<List<Notifications>> call, @NonNull Response<List<Notifications>> response) {
                if (response.code() == 200) {
                    notifications.setValue(response.body());
                } else {
                    operationToast.setValue("Server error. Try to refresh.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Notifications>> call, @NonNull Throwable t) {
                operationToast.setValue("Cannot connect. Check your connection and refresh.");
            }
        });
    }

    //-----------------------------------------------Wallet------------------------------------------------------------------
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

    //-----------------------------------------------Franchise----------------------------------------------------------------

    public LiveData<ValidationFlag> getValFlag() {
        return valFlag;
    }

    public void setFFName(@NonNull CharSequence s) {
        String fname = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (nameValidator(fname)) {
            enquiry.setFname(fname);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("ffNameInvalid"));
            flag = 1;
        }
    }

    public void setFLName(@NonNull CharSequence s) {
        String lname = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (nameValidator(lname)) {
            enquiry.setLname(lname);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("flNameInvalid"));
            flag = 1;
        }
    }

    public void setFPhone(@NonNull CharSequence s) {
        String phone = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (phoneValidator(phone)) {
            enquiry.setPhone(phone);
            flag = 0;

        } else {
            valFlag.setValue(new ValidationFlag("fphoneInvalid"));
            flag = 1;
        }
    }

    public void setFEmail(@NonNull CharSequence s) {
        String email = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            enquiry.setEmail(email);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("femailInvalid"));
            flag = 1;
        }
    }

    public void setFCity(@NonNull CharSequence s) {
        String city = s.toString();
        enquiry.setCity(city);
        flag = 0;
    }

    public void setFCountry(@NonNull CharSequence s) {
        String country = s.toString();
        enquiry.setCountry(country);
        flag = 0;
    }

    public void setFMessage(CharSequence s) {
        String message = s.toString();
        enquiry.setMessage(message);
        flag = 0;
    }

    private boolean validate() {
        if (flag == 0 &&
                !TextUtils.isEmpty(enquiry.getFname()) &&
                !TextUtils.isEmpty(enquiry.getLname()) &&
                !TextUtils.isEmpty(enquiry.getPhone()) &&
                !TextUtils.isEmpty(enquiry.getPhone()) &&
                !TextUtils.isEmpty(enquiry.getEmail()) &&
                !TextUtils.isEmpty(enquiry.getCity()) &&
                !TextUtils.isEmpty(enquiry.getCountry()))
            return true;
         else {
            operationToast.setValue("Complete all the details.");
            return false;
        }
    }

    public LiveData<Integer> onSubmitClick() {
        if (validate()) {
            submitEnquiry();
            return enquiryFlag;
        }
        else {
            enquiryFlag.setValue(0);
            return enquiryFlag;
        }
    }

    private void submitEnquiry() {
        ApiInterface enquiryRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = enquiryRetro.getFranchise(enquiry);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    operationToast.setValue("Enquiry submitted.");
                    enquiryFlag.setValue(response.code());
                } else {
                    operationToast.setValue("Try again please.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                internetToast.setValue("No internet. Try again.");
            }
        });

    }

    private static boolean nameValidator(String name) {
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    private static boolean phoneValidator(String phone) {
        String regx = "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }


}
