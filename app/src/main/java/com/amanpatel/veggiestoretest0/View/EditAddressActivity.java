package com.amanpatel.veggiestoretest0.View;

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
import com.amanpatel.veggiestoretest0.databinding.ActivityEditAddressBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.City;

public class EditAddressActivity extends AppCompatActivity {
    public ActivityEditAddressBinding binding;
    public AddressChangeViewModel addresschangeviewmodel;
    public NewAddress newAddress;
    SharedPrefs sharedPrefs;
    String valFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_address);
        addresschangeviewmodel = ViewModelProviders.of(this).get(AddressChangeViewModel.class);
        binding.setAddresschangeviewmodel(addresschangeviewmodel);
        binding.setLifecycleOwner(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefs = new SharedPrefs(this);

        final String aid = getIntent().getExtras().getString("aid");
        String name = getIntent().getExtras().getString("name");
        String email = getIntent().getExtras().getString("email");
        String phone = getIntent().getExtras().getString("phone");
        final String address = getIntent().getExtras().getString("address");
        String landmark = getIntent().getExtras().getString("landmark");
        String pin = getIntent().getExtras().getString("pin");
        String area = getIntent().getExtras().getString("area");
        String city = getIntent().getExtras().getString("city");

        binding.nameInput.setText(name);
        binding.emailInput.setText(email);
        binding.phoneInput.setText(phone);
        binding.addressInput.setText(address);
        binding.landmarkInput.setText(landmark);
        binding.pinCodeInput.setText(pin);
        binding.areaInput.setText(area);
        binding.cityInput.setText(city);

        addresschangeviewmodel.setName(name);
        addresschangeviewmodel.setEmail(email);
        addresschangeviewmodel.setPhone(phone);
        addresschangeviewmodel.setAddress(address);
        addresschangeviewmodel.setLandmark(landmark);
        addresschangeviewmodel.setPincode(pin);
        addresschangeviewmodel.setArea(area);
        addresschangeviewmodel.setCity(city);

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

        binding.editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.nameInput.getText()) ||
                        TextUtils.isEmpty(binding.emailInput.getText()) ||
                        TextUtils.isEmpty(binding.phoneInput.getText()) ||
                        TextUtils.isEmpty(binding.addressInput.getText()) ||
                        TextUtils.isEmpty(binding.landmarkInput.getText()) ||
                        TextUtils.isEmpty(binding.pinCodeInput.getText()))
                    toastMaker("Fill in all the details.");
                else {
                    addresschangeviewmodel.onEditBtnClicked(sharedPrefs.getString(EditAddressActivity.this, CID), aid, sharedPrefs.getString(EditAddressActivity.this, Area), sharedPrefs.getString(EditAddressActivity.this, City));
                    startMainActivity();
                    finish();
                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(EditAddressActivity.this, MainActivity.class);
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
