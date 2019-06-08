package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Models.ResponseCode;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.SignUpViewModel;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivitySignUpBinding;
import com.google.android.material.tabs.TabLayout;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    SignUpViewModel signupviewmodel;
    SharedPrefs sharedPrefs;
    int code;
    String valFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        sharedPrefs = new SharedPrefs(this);

        signupviewmodel.getToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.nameInput.getText()) || TextUtils.isEmpty(binding.emailInput.getText()) || TextUtils.isEmpty(binding.phoneInput.getText()))
                    toastMaker("Fill in data");
                else {
                    signupviewmodel.onSignUpClicked();
                }
            }
        });

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        binding.tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, TermsAndConditions.class);
                startActivity(intent);
            }
        });

        signupviewmodel.getCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer responseCode) {
                if (responseCode == 200) {
                    startLoginActivity();
                }
            }
        });

        signupviewmodel.getValFlag().observe(this, new Observer<ValidationFlag>() {
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
                        case "passwordInvalid":
                            binding.passwordInput.setError("Invalid Password. Password must have at least 5 characters, 1 Alphabet, 1 Number and 1 Special Character");
                            break;
                        case "passNotMatch":
                            binding.confirmPasswordInput.setError("Password doesn't match");
                            break;
                        case "completeAllDetails":
                            toastMaker("Incorrect data");
                            break;
                    }
                    valFlag = "";
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void startLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    private void bind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signupviewmodel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.setSignupviewmodel(signupviewmodel);
        binding.setLifecycleOwner(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
