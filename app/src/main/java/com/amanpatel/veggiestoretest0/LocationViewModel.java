package com.amanpatel.veggiestoretest0;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import android.util.Log;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.AdminBanner;
import com.amanpatel.veggiestoretest0.Models.Areas;
import com.amanpatel.veggiestoretest0.Models.City;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<String> internetToast = new MutableLiveData<>();
    private MutableLiveData<String> operationToast = new MutableLiveData<>();
    private MutableLiveData<List<Areas>> areaArray = new MutableLiveData<>();
    private MutableLiveData<List<City>> cityArray = new MutableLiveData<>();
    private MutableLiveData<List<AdminBanner>> banner = new MutableLiveData<>();

    public LiveData<String> getInternetToast() {
        return internetToast;
    }

    public LiveData<String> getOperationToast() {
        return operationToast;
    }

    public LiveData<List<City>> getCity() {
        loadCity();
        return cityArray;
    }

    private void loadCity() {
        final ApiInterface city = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<City>> call = city.getCity();

        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(@NonNull Call<List<City>> call, @NonNull Response<List<City>> response) {
                if(response.code() == 200 && response.body()!=null) {
                    if(response.body().size()!=0) {
                        cityArray.setValue(response.body());
                    }
                    else
                        operationToast.setValue("No data found.");
                }
                else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<City>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect. Check your connection.");
            }
        });
    }

    public LiveData<List<Areas>> getAreas(String id){
        loadAreas(id);
        return areaArray;
    }

    private void loadAreas(String id) {
        final ApiInterface area = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Areas>> call = area.getAreas(id);

        call.enqueue(new Callback<List<Areas>>() {
            @Override
            public void onResponse(@NonNull Call<List<Areas>> call, @NonNull Response<List<Areas>> response) {
                if(response.code() == 200 && response.body()!=null) {
                    if(response.body().size()!=0) {
                        areaArray.setValue(response.body());
                    }
                    else
                        operationToast.setValue("No data found.");
                }
                else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<Areas>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect. Check your connection.");
            }
        });
    }

    public LiveData<List<AdminBanner>> getBanner() {
        loadBanner();
        return banner;
    }

    private void loadBanner() {
        final ApiInterface bannerRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<AdminBanner>> call = bannerRetro.getAdminBanner();

        call.enqueue(new Callback<List<AdminBanner>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdminBanner>> call, @NonNull Response<List<AdminBanner>> response) {
                if(response.code() == 200 && response.body()!=null) {
                    if(response.body().size()!=0) {
                        banner.setValue(response.body());
                    }
                    else
                        operationToast.setValue("No data found.");
                }
                else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<AdminBanner>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect. Check your connection.");
            }
        });
    }

    public int validateLocation(String areaValue) {
        if (areaValue.equals("null"))
            return 1;
        else
            return 0;
    }

    public int validateCity(String cityValue) {
        if (cityValue.equals("null"))
            return 1;
        else
            return 0;
    }

}





