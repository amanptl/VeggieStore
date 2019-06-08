package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Models.ResponseCode;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.ProfileViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityEditProfileBinding;
import com.amanpatel.veggiestoretest0.databinding.ActivityPasswordChangeBinding;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;

public class PasswordChangeActivity extends AppCompatActivity {
    ActivityPasswordChangeBinding binding;
    ProfileViewModel profileviewmodel;
    SharedPrefs sharedPrefs;
    String valFlag = "";
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_change);
        profileviewmodel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding.setProfileviewmodel(profileviewmodel);
        binding.setLifecycleOwner(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefs = new SharedPrefs(this);

        binding.fabChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.oldPass.getText()) || TextUtils.isEmpty(binding.newPass.getText()) || TextUtils.isEmpty(binding.conPass.getText()))
                    toastMaker("Fill in data");
                else {
                    profileviewmodel.onChangeClicked(sharedPrefs.getString(PasswordChangeActivity.this, CID));
                }
            }
        });

        profileviewmodel.getValFlag().observe(this, new Observer<ValidationFlag>() {
            @Override
            public void onChanged(ValidationFlag validationFlag) {
                if (validationFlag != null) {
                    valFlag = validationFlag.getFlag();
                    switch (valFlag) {
                        case "oldPasswordInvalid":
                            binding.oldPass.setError("Invalid Password. Password must have at least 5 characters, 1 Alphabet, 1 Number and 1 Special Character");
                            break;
                        case "newPasswordInvalid":
                            binding.newPass.setError("Invalid Password. Password must have at least 5 characters, 1 Alphabet, 1 Number and 1 Special Character");
                            break;
                        case "passNotMatch":
                            binding.conPass.setError("Password doesn't match");
                            break;
                        case "completeAllDetails":
                            toastMaker("Incorrect data");
                            break;
                    }
                    valFlag = "";
                }
            }
        });

        profileviewmodel.getCode().observe(this, new Observer<ResponseCode>() {
            @Override
            public void onChanged(ResponseCode responseCode) {
                code = responseCode.getCode();
                if (code == 200) {
                    toastMaker("Password Changed");
                    finish();
                }
                else
                    toastMaker("Current password does not match");
            }
        });
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
