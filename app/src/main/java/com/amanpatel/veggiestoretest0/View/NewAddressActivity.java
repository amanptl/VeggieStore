package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.AddressChangeViewModel;
import com.amanpatel.veggiestoretest0.Models.NewAddress;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityNewAddressBinding;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CEmail;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CName;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CPhone;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.City;

public class NewAddressActivity extends AppCompatActivity {

    public ActivityNewAddressBinding binding;
    public AddressChangeViewModel addresschangeviewmodel;
    public NewAddress newAddress;
    SharedPrefs sharedPrefs;
    String valFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_address);
        addresschangeviewmodel = ViewModelProviders.of(this).get(AddressChangeViewModel.class);
        binding.setAddresschangeviewmodel(addresschangeviewmodel);
        binding.setLifecycleOwner(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefs = new SharedPrefs(this);

        binding.nameInput.setText(sharedPrefs.getString(this, CName));
        binding.emailInput.setText(sharedPrefs.getString(this, CEmail));
        binding.phoneInput.setText(sharedPrefs.getString(this, CPhone));
        binding.areaInput.setText(sharedPrefs.getString(this, Area));
        binding.cityInput.setText(sharedPrefs.getString(this, City));
        addresschangeviewmodel.setName(sharedPrefs.getString(this, CName));
        addresschangeviewmodel.setEmail(sharedPrefs.getString(this, CEmail));
        addresschangeviewmodel.setPhone(sharedPrefs.getString(this, CPhone));

        addresschangeviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        addresschangeviewmodel.getValFlag().observe(this, new Observer<ValidationFlag>() {
            @Override
            public void onChanged(ValidationFlag validationFlag) {
                if (validationFlag != null) {
                    valFlag = validationFlag.getFlag();
                    switch (valFlag) {
                        case "nameInvalid":
                            binding.nameInput.setError("Invalid Name");
                            break;
                        case "emailInvalid":
                            binding.emailInput.setError("Invalid E-mail");
                            break;
                        case "phoneInvalid":
                            binding.phoneInput.setError("Invalid Phone Number");
                            break;
                        case "addressInvalid":
                            binding.addressInput.setError("Invalid Address Format");
                            break;
                        case "landmarkInvalid":
                            binding.landmarkInput.setError("Invalid Landmark Format");
                            break;
                        case "pinInvalid":
                            binding.pinCodeInput.setError("Invalid Pin-Code");
                            break;
                    }
                    valFlag = "";
                }
            }
        });

        binding.newAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.addressInput.getText()) || TextUtils.isEmpty(binding.landmarkInput.getText()) || TextUtils.isEmpty(binding.pinCodeInput.getText()))
                    toastMaker("Fill in all the details.");
                else {
                    addresschangeviewmodel.onNewBtnClicked(sharedPrefs.getString(NewAddressActivity.this, CID), sharedPrefs.getString(NewAddressActivity.this, Area), sharedPrefs.getString(NewAddressActivity.this, City));
                    if (getIntent().hasExtra("from")) {
                        if(getIntent().getExtras().getString("from").equals("cart")){
                            Intent intent = new Intent(NewAddressActivity.this, CheckoutActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        startMainActivity();
                        finish();
                    }
                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(NewAddressActivity.this, MainActivity.class);
        intent.putExtra("from", "Address");
        startActivity(intent);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
