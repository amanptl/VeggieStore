package com.amanpatel.veggiestoretest0;


import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.NewAddress;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressChangeViewModel extends ViewModel {
    private MutableLiveData<String> internetToast = new MutableLiveData<>();
    private MutableLiveData<String> operationToast = new MutableLiveData<>();
    private NewAddress newAddress = new NewAddress("", "", "", "", "", "", "", "", "", "");
    private MutableLiveData<ValidationFlag> valFlag = new MutableLiveData<>();
    private int flag = 1;

    public LiveData<String> getInternetToast() {
        return internetToast;
    }

    public LiveData<String> getOperationToast() {
        return operationToast;
    }

    public void setName(@NonNull CharSequence s) {
        String name = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (nameValidator(name)) {
            newAddress.setName(name);
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
            newAddress.setEmail(email);
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
                newAddress.setPhone(phone);
                flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("phoneInvalid"));
            flag = 1;
        }
    }

    public void setAddress(@NonNull CharSequence s) {
        String address = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (!address.equals("")) {
            newAddress.setAddress(address);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("addressInvalid"));
            flag = 1;
        }
    }

    public void setLandmark(@NonNull CharSequence s) {
        String landmark = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (!landmark.equals("")) {
            newAddress.setLandmark(landmark);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("landmarkInvalid"));
            flag = 1;
        }
    }

    public void setPincode(@NonNull CharSequence s) {
        String pin = s.toString();
        valFlag.setValue(new ValidationFlag(""));
        if (pinValidator(pin) && !pin.equals("")) {
            newAddress.setPostCode(pin);
            flag = 0;
        } else {
            valFlag.setValue(new ValidationFlag("pinInvalid"));
            flag = 1;
        }
    }

    public void setArea(@NonNull String area) {
        newAddress.setDistrict(area);
    }

    public void setCity(@NonNull String city) {
        newAddress.setCity(city);
    }

    private void setCID(@NonNull String cid) {
        newAddress.setCid(cid);
    }

    private void setMobileDef() {
        newAddress.setMobiledefault("false");
    }

    private boolean validate() {
        if (flag == 0)
            return true;
        else
            operationToast.setValue("Fill in correct details.");
        return false;
    }

    public void onNewBtnClicked(String cid, String area, String city) {
        if (validate()) {
            setArea(area);
            setCity(city);
            setCID(cid);
            setMobileDef();
            newAddress();
        }
    }

    public LiveData<ValidationFlag> getValFlag() {
        return valFlag;
    }

    private void newAddress() {
        final ApiInterface addrRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<AddressBook> call = addrRetro.addAddress(newAddress);
        call.enqueue(new Callback<AddressBook>() {
            @Override
            public void onResponse(@NonNull Call<AddressBook> call, @NonNull Response<AddressBook> response) {
                if (response.code() == 201)
                    operationToast.setValue("Address added.");
                else
                    operationToast.setValue("Operation failed. Please try again.");
            }

            @Override
            public void onFailure(@NonNull Call<AddressBook> call, @NonNull Throwable t) {
                operationToast.setValue("Operation failed. Check connection.");
            }
        });
    }

    public void onEditBtnClicked(String cid, String aid, String area, String city) {
        if (validate()) {
            newAddress.setId(aid);
            setArea(area);
            setCity(city);
            setCID(cid);
            setMobileDef();
            editAddress();
        }
    }

    private void editAddress() {
        final ApiInterface addrRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<AddressBook> call = addrRetro.editAddress(newAddress);
        call.enqueue(new Callback<AddressBook>() {
            @Override
            public void onResponse(@NonNull Call<AddressBook> call, @NonNull Response<AddressBook> response) {
                if (response.code() == 200)
                    operationToast.setValue("Address edited.");
                else
                    operationToast.setValue("Operation failed. Please try again.");
            }

            @Override
            public void onFailure(@NonNull Call<AddressBook> call, @NonNull Throwable t) {
                operationToast.setValue("Operation failed. Check connection.");
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


    private static boolean pinValidator(String pin) {
        String regx = "^[1-9][0-9]{5}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pin);
        return matcher.find();
    }
}
