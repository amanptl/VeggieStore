package com.amanpatel.veggiestoretest0.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.LoginViewModel;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.User;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityLogInBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CEmail;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CName;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CPhone;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class LogInActivity extends AppCompatActivity {
    ActivityLogInBinding binding;
    LoginViewModel loginviewmodel;
    SharedPrefs sharedPrefs;
    String valFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        loginviewmodel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    sharedPrefs.saveString(LogInActivity.this, isLoggedIn, "true");
                    sharedPrefs.saveString(LogInActivity.this, CName, user.getName());
                    sharedPrefs.saveString(LogInActivity.this, CID, user.getId());
                    sharedPrefs.saveString(LogInActivity.this, CEmail, user.getEmail());
                    sharedPrefs.saveString(LogInActivity.this, CPhone, user.getPhone());
                    if (getIntent().hasExtra("from")) {
                        if (getIntent().getExtras().getString("from").equals("cart")) {
                            loginviewmodel.mergeCart(user.getId(), CartActivity.guestCart).observe(LogInActivity.this, new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer integer) {
                                    if (integer == 200) {
                                        CartActivity.guestCart.clear();
                                        Intent intent = new Intent(LogInActivity.this, CheckoutActivity.class);
                                        startActivity(intent);
                                    } else {
                                        toastMaker("Failed.");
                                    }
                                }
                            });
                        }
                    } else {
                        loginviewmodel.mergeCart(user.getId(), CartActivity.guestCart).observe(LogInActivity.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == 200) {
                                    CartActivity.guestCart.clear();
                                    startMarketActivity();
                                } else {
                                    toastMaker("Failed.");
                                }
                            }
                        });
                    }
                }
            }
        });

        loginviewmodel.getToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.emailInput.getText())) {
                    loginviewmodel.requestPassword(binding.emailInput.getText().toString());
                } else
                    toastMaker("Enter your email id.");
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void startMarketActivity() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void bind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);
        loginviewmodel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setLoginviewmodel(loginviewmodel);
        binding.setLifecycleOwner(this);
        sharedPrefs = new SharedPrefs(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
