package com.amanpatel.veggiestoretest0;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ResponseCode;
import com.amanpatel.veggiestoretest0.Models.User;
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

public class LoginViewModel extends ViewModel {
    private String email = "";
    private String password = "";
    private MutableLiveData<ValidationFlag> valFlag = new MutableLiveData<>();
    private MutableLiveData<ResponseCode> code = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<String> toast = new MutableLiveData<>();
    private MutableLiveData<Integer> mergeFlag = new MutableLiveData<>();
    public ObservableField<String> logInBtn = new ObservableField<>("Log In");

    public void setEmail(@NonNull CharSequence s) {
        email = s.toString();
    }

    public void setPassword(@NonNull CharSequence s) {
        password = s.toString();
    }

    public LiveData<ResponseCode> getCode() {
        return code;
    }

    private boolean validate() {
        if (email.equals("") || password.equals(""))
            toast.setValue("Enter all details.");
        else
            return true;
        return false;
    }

    public void onLogInClicked() {
        if (validate()) {
            login();
        }
    }

    public LiveData<ValidationFlag> getValFlag() {
        return valFlag;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getToast() {
        return toast;
    }


    private void login() {
        ApiInterface loginRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = loginRetro.login(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("false")) {
                        toast.setValue("Account suspended.");
                    } else {
                        logInBtn.set("Please wait...");
                        toast.setValue("Welcome!");
                        user.setValue(response.body());
                    }
                }
                else
                    toast.setValue("Invalid email id or password.");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                toast.setValue("No internet. Try again.");
            }
        });
    }

    public void requestPassword(String email){
        toast.setValue("Please wait...");
        ApiInterface loginRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<String> call = loginRetro.getPassword(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200){
                    toast.setValue(response.body());
                } else {
                    toast.setValue("Server error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                toast.setValue("No internet. Try again.");
            }
        });
    }

    public LiveData<Integer> mergeCart(String id, List<Cart> cartList){
        merge(id, cartList);
        return mergeFlag;
    }

    private void merge(String id, List<Cart> cartList) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = productRetro.mergeCart(id, cartList);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int s = response.code();
                if(response.code() == 200) {
                   mergeFlag.setValue(response.code());
                }
                else
                    toast.setValue(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                toast.setValue("Operation failed. Check connection.");
            }
        });
    }

}
