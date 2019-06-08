package com.amanpatel.veggiestoretest0;

import android.util.Log;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.AdminBanner;
import com.amanpatel.veggiestoretest0.Models.ProductCategory1;
import com.amanpatel.veggiestoretest0.Models.ResponseCode;
import com.amanpatel.veggiestoretest0.Models.User;
import com.amanpatel.veggiestoretest0.Models.UserEdit;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;

import java.util.List;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<String> internetToast = new MutableLiveData<>();
    private MutableLiveData<String> operationToast = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<User> userPassword = new MutableLiveData<>();
    private UserEdit userEdited;
    private int flag = 1;
    private MutableLiveData<ResponseCode> code = new MutableLiveData<>();
    private MutableLiveData<ValidationFlag> valFlag = new MutableLiveData<>();
    private String oldpass;
    private String newpass;

    public LiveData<String> getInternetToast() {
        return internetToast;
    }

    public LiveData<String> getOperationToast() {
        return operationToast;
    }

    public LiveData<User> getUser(String cid) {
        loadUser(cid);
        return user;
    }



    private void loadUser(String cid) {
        final ApiInterface userRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = userRetro.getUser(cid);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200)
                    user.setValue(response.body());
                else
                    internetToast.setValue("Server error");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot fetch. Check connection.");
            }
        });
    }


    //---------------------------------------------------Edit--------------------------------------------------------------------
    public void setName(@NonNull CharSequence s) {
        String name = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (nameValidator(name)) {
            userEdited.setName(name);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("nameInvalid"));
            flag = 1;
        }
    }

    public void setPhone(@NonNull CharSequence s) {
        String phone = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (phoneValidator(phone)) {
            userEdited.setPhone(phone);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("phoneInvalid"));
            flag = 1;
        }
    }

    public void editUser(String cid, String name, String phone) {
        userEdited = new UserEdit(cid, name, phone);
        final ApiInterface userRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = userRetro.editUser(userEdited);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200)
                    operationToast.setValue("Information updated.");
                else
                    operationToast.setValue("Operation failed.");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                operationToast.setValue("Operation failed. Check your connection.");
                Log.d("onFailure", t.toString());
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


    //-----------------------------------------------Change Password-----------------------------------------------

    public void setOldPassword(@NonNull CharSequence s) {
        String password = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (password.length() > 5 && passwordValidator(password)) {
            oldpass = password;
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("oldPasswordInvalid"));
            flag = 1;
        }
    }

    public void setNewPassword(@NonNull CharSequence s) {
        String password = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (password.length() > 5 && passwordValidator(password)) {
            newpass = password;
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("newPasswordInvalid"));
            flag = 1;
        }
    }

    public void matchPassword(@NonNull CharSequence s) {
        String conPass = s.toString();
        if (!newpass.equals(conPass)) {
            valFlag.setValue(new ValidationFlag("passNotMatch"));
            flag = 1;
        } else {
            flag = 0;
        }
    }

    private boolean validate() {
        if (flag == 0)
            return true;
        else
            operationToast.setValue("Enter all the details.");
        return false;
    }

    public void onChangeClicked(String cid) {
        if (validate()) {
            changePassword(cid, oldpass, newpass);
        }
    }

    public LiveData<ResponseCode> getCode() {
        return code;
    }

    public LiveData<ValidationFlag> getValFlag() {
        return valFlag;
    }


    public void changePassword(final String id, final String oldpass, String newpass) {
        final ApiInterface userRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = userRetro.changePassword(id, oldpass, newpass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.code() == 200){
                operationToast.setValue("Password changed.");
                code.setValue(new ResponseCode(response.code()));}
                else
                    operationToast.setValue("Operation failed.");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                operationToast.setValue("Operation failed. Check your connection.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    private static boolean passwordValidator(String password) {
        String regx = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

}
