package com.amanpatel.veggiestoretest0.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.ImageSliderAdapter;
import com.amanpatel.veggiestoretest0.LocationViewModel;
import com.amanpatel.veggiestoretest0.Models.AdminBanner;
import com.amanpatel.veggiestoretest0.Models.Areas;
import com.amanpatel.veggiestoretest0.Models.City;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityLocationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaMinAmt;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaShipping;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.City;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CityId;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class LocationActivity extends AppCompatActivity {

    public String areaValue = "null";
    public String cityValue = "null";

    private SharedPrefs sharedPrefs;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public LocationViewModel locationviewmodel;
    ActivityLocationBinding binding;
    public ArrayList<String> cityString = new ArrayList<>();
    public ArrayList<String> areaString = new ArrayList<>();
    ArrayAdapter<String> cDataAdapter;
    ArrayAdapter<String> aDataAdapter;
    private Boolean exit = false;
    private String cid;
    AlertDialog.Builder cityDialog;
    AlertDialog.Builder areaDialog;
    private List<City> tCity;
    private int cityFlag = 1;
    private int previousFlag = 1;
    private ViewPager mPager;
    private ArrayList<String> imagesUrl = new ArrayList<>();
    private List<Areas> tAreas = new ArrayList<>();
    private String previousCity = "";
    private String previousArea = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        sharedPrefs = new SharedPrefs(this);

        locationviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        locationviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(binding.getRoot(), R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadCity();
                                loadBanner();
                            }
                        }).setActionTextColor(ContextCompat.getColor(LocationActivity.this, R.color.snackbarActionColor)).show();
            }
        });


        previousCity =  sharedPrefs.getString(LocationActivity.this, City);
        previousArea = sharedPrefs.getString(LocationActivity.this, Area);

        if(!previousCity.equals("") && !previousArea.equals("")) {
            cityValue = previousCity;
            areaValue = previousArea;
            cityFlag = 0;
            previousFlag = 1;
            loadArea(sharedPrefs.getString(LocationActivity.this, CityId));
            binding.cityText.setText(previousCity);
            binding.areaText.setText(previousArea);
        }

        cityDialog = new AlertDialog.Builder(this);
        cDataAdapter = new ArrayAdapter<>(LocationActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                cityString);
        cityDialog.setAdapter(cDataAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cityValue = cDataAdapter.getItem(which);
                sharedPrefs.saveString(LocationActivity.this, City, cityValue);
                binding.cityText.setText(cityValue);
                for (int i = 0; i < tCity.size(); i++) {
                    if (tCity.get(i).getName().equals(cityValue))
                        cid = tCity.get(i).getId();
                }
                cityFlag = locationviewmodel.validateCity(cityValue);
                sharedPrefs.saveString(LocationActivity.this, CityId, cid);
                loadArea(cid);
            }
        });
        cityDialog.setTitle(Html.fromHtml("<font color='#000000'><b>" + getResources().getString(R.string.city) + "</b></font>", 0));
        cityDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        areaDialog = new AlertDialog.Builder(this);
        aDataAdapter = new ArrayAdapter<>(LocationActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                areaString);
        areaDialog.setAdapter(aDataAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                areaValue = aDataAdapter.getItem(which);
                sharedPrefs.saveString(LocationActivity.this, Area, areaValue);
                for (int i = 0; i < tAreas.size(); i++) {
                    if (tAreas.get(i).getName().equals(areaValue)) {
                        sharedPrefs.saveString(LocationActivity.this, AreaID, tAreas.get(i).getId());
                        sharedPrefs.saveString(LocationActivity.this, AreaShipping, tAreas.get(i).getShippingcharge());
                        sharedPrefs.saveString(LocationActivity.this, AreaMinAmt, tAreas.get(i).getMinimumcharge());
                    }
                }
                binding.areaText.setText(areaValue);
            }
        });

        if (sharedPrefs.findKey(this, isLoggedIn)) {
            binding.loginTextLayout.setVisibility(View.INVISIBLE);
            binding.registerTextLayout.setVisibility(View.INVISIBLE);
        } else {
            binding.loginTextLayout.setVisibility(View.VISIBLE);
            binding.registerTextLayout.setVisibility(View.VISIBLE);
        }

        loadCity();
        loadBanner();

        binding.citySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefs.removeKey(LocationActivity.this, Area);
                sharedPrefs.removeKey(LocationActivity.this, AreaID);
                cityDialog.show();
            }
        });

        binding.areaSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cityFlag == 0) {
                    if(areaString.size()!=0) {
                        areaDialog.setTitle(Html.fromHtml("<font color='#000000'><b>" + getResources().getString(R.string.area) + "</b></font>", 0));
                        areaDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        areaDialog.show();
                    }
                } else {
                    areaDialog.setTitle(Html.fromHtml("<font color='#000000'>Please select your city.</font>", 0));
                    areaDialog.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    areaDialog.show();
                }

            }
        });

        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefs.findKey(LocationActivity.this, Area))
                    startLogInActivity(view);
                else
                    makeLocationAlert();
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefs.findKey(LocationActivity.this, Area))
                    startSignUpActivity(view);
                else
                    makeLocationAlert();
            }
        });

    }

    private void loadCity(){
        locationviewmodel.getCity().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                if (cities != null) {
                    tCity = cities;
                    cityString = new ArrayList<>();
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).isVisible())
                            cityString.add(cities.get(i).getName());
                    }
                    cDataAdapter.clear();
                    cDataAdapter.addAll(cityString);
                    cDataAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void loadArea(String cityId) {
        areaString = new ArrayList<>();
        tAreas = new ArrayList<>();
        locationviewmodel.getAreas(cityId).observe(this, new Observer<List<Areas>>() {
            @Override
            public void onChanged(List<Areas> areas) {
                if (areas != null) {
                    aDataAdapter.clear();
                    tAreas = areas;
                    areaString.clear();
                    areaString = new ArrayList<>();
                    for (int i = 0; i < areas.size(); i++) {
                            areaString.add(areas.get(i).getName());
                    }
                    if(areaString.size()==0){
                        toastMaker("No areas found.");
                    }
                    aDataAdapter.addAll(areaString);
                    aDataAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void loadBanner() {
        locationviewmodel.getBanner().observe(this, new Observer<List<AdminBanner>>() {
            @Override
            public void onChanged(List<AdminBanner> adminBanners) {
                if (adminBanners != null) {
                    for (int i = 0; i < adminBanners.size(); i++)
                        imagesUrl.add(adminBanners.get(i).getLink());
                    binding.progressBarLocation.setVisibility(View.INVISIBLE);
                    binding.bannerLayout.setVisibility(View.VISIBLE);
                }
                mPager.setAdapter(new ImageSliderAdapter(LocationActivity.this, imagesUrl));
                if (mPager.getAdapter() != null) {
                    mPager.getAdapter().notifyDataSetChanged();
                }
            }
        });

        NUM_PAGES = imagesUrl.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                if (mPager.getAdapter() != null) {
                    mPager.getAdapter().notifyDataSetChanged();
                    mPager.setCurrentItem(currentPage++, true);
                }

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
                //handler.postDelayed(Update, 2000);
            }
        }, 4000, 4000);


    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finishAffinity();
        } else {
            Toast.makeText(this, "Press back again to exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    public void bind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        locationviewmodel = ViewModelProviders.of(this).get(LocationViewModel.class);
        binding.setLocationviewmodel(locationviewmodel);
        binding.setLifecycleOwner(this);
        mPager = binding.pager;
        mPager.addOnPageChangeListener(new LocationActivity.CircularViewPagerHandler(mPager));
        binding.progressBarLocation.setVisibility(View.VISIBLE);
        binding.bannerLayout.setVisibility(View.INVISIBLE);
    }

    public void startMainActivity(View v) {
        int flag = locationviewmodel.validateLocation(areaValue);
        if (flag == 0) {
            Intent intent = new Intent(LocationActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            makeLocationAlert();
        }
    }

    public void startSignUpActivity(View v) {
        Intent intent = new Intent(LocationActivity.this, SignUpActivity.class);
        startActivity(intent);

    }

    public void startLogInActivity(View v) {
        Intent intent = new Intent(LocationActivity.this, LogInActivity.class);
        startActivity(intent);

    }

    public void makeLocationAlert() {
        AlertDialog.Builder locationDialog = new AlertDialog.Builder(this);
        locationDialog.setMessage(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.no_location) + "</font>", 0));
        locationDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        locationDialog.show();
    }

    public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        private ViewPager mViewPager;
        private int mCurrentPosition;
        private int mScrollState;

        CircularViewPagerHandler(final ViewPager viewPager) {
            mViewPager = viewPager;

        }

        @Override
        public void onPageSelected(final int position) {
            mCurrentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            handleScrollState(state);
            mScrollState = state;
        }

        private void handleScrollState(final int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                setNextItemIfNeeded();
            }
        }

        private void setNextItemIfNeeded() {
            if (!isScrollStateSettling()) {
                handleSetNextItem();
            }
        }

        private boolean isScrollStateSettling() {
            return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
        }

        private void handleSetNextItem() {
            final int lastPosition = Objects.requireNonNull(mViewPager.getAdapter()).getCount() - 1;
            if (mCurrentPosition == 0) {
                mViewPager.setCurrentItem(lastPosition, false);
            } else if (mCurrentPosition == lastPosition) {
                mViewPager.setCurrentItem(0, false);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}