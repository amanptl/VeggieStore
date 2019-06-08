package com.amanpatel.veggiestoretest0;


import android.content.Intent;
import android.text.TextUtils;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.ResponseCode;
import com.amanpatel.veggiestoretest0.Models.UserRegister;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;

import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpViewModel extends ViewModel {
    private UserRegister user = new UserRegister("", "", "", "");
    private int flag = 1;
    private MutableLiveData<Integer> code = new MutableLiveData<>();
    private MutableLiveData<ValidationFlag> valFlag = new MutableLiveData<>();
    private MutableLiveData<String> toast = new MutableLiveData<>();
    public ObservableField<String> signUpBtn = new ObservableField<>("Sign Up");

    public LiveData<String> getToast() {
        return toast;
    }


    public void setName(@NonNull CharSequence s) {
        String name = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (nameValidator(name)) {
            user.setName(name);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("nameInvalid"));
            flag = 1;
        }
    }

    public void setEmail(@NonNull CharSequence s) {
        String email = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            user.setEmail(email);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("emailInvalid"));
            flag = 1;
        }
    }

    public void setPhone(@NonNull CharSequence s) {
        String phone = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (phoneValidator(phone)) {
                user.setPhone(phone);
                flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("phoneInvalid"));
            flag = 1;
        }
    }

    public void setPassword(@NonNull CharSequence s) {
        String password = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (password.length() > 5 && passwordValidator(password)) {
            user.setPassword(password);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("passwordInvalid"));
            flag = 1;
        }
    }

    public void matchPassword(@NonNull CharSequence s) {
        String conPass = s.toString();
        if (!user.getPassword().equals(conPass)) {
            valFlag.setValue(new ValidationFlag("passNotMatch"));
            flag = 1;
        } else
            flag = 0;
    }

    private boolean validate() {
        if (flag == 0)
            return true;
        else
            valFlag.setValue(new ValidationFlag("completeAllDetails"));
            return false;
    }

    public void onSignUpClicked() {
        if (validate()) {
            register();
        }
    }

    public LiveData<Integer> getCode() {
        return code;
    }

    public LiveData<ValidationFlag> getValFlag() {
        return valFlag;
    }


    private void register() {
        final ApiInterface signupRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = signupRetro.register(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    signUpBtn.set("Please wait...");
                    toast.setValue("Registered.");
                    code.setValue(response.code());
                }
                else if(response.code() == 400){
                    toast.setValue("Email id already exists.");
                    signUpBtn.set("Sign Up");
                }
                else{
                    signUpBtn.set("Sign Up");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                toast.setValue("No internet. Try again.");
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

    private static boolean passwordValidator(String password) {
        String regx = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
}
