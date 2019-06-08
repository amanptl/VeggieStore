package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.LocationViewModel;
import com.amanpatel.veggiestoretest0.Models.User;
import com.amanpatel.veggiestoretest0.ProfileViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CEmail;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CName;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CPhone;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    ProfileViewModel profileviewmodel;
    FloatingActionButton fab;
    SharedPrefs sharedPrefs;
    private User userProfile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        sharedPrefs = new SharedPrefs(this);
        fab = findViewById(R.id.fab_edit_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userProfile!=null) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("Name", binding.profileName.getText());
                    intent.putExtra("Phone", binding.profilePhone.getText());
                    startActivity(intent);
                }
                else
                    Toast.makeText(ProfileActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
            }
        });

        profileviewmodel.getUser(sharedPrefs.getString(this,CID)).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userProfile = user;
                binding.profileName.setText(user.getName());
                binding.profileEmail.setText(user.getEmail());
                binding.profilePhone.setText(user.getPhone());
            }
        });
    }

    private void bind(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        profileviewmodel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding.setProfileviewmodel(profileviewmodel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.profileName.setText(sharedPrefs.getString(this, CName));
        binding.profileEmail.setText(sharedPrefs.getString(this, CEmail));
        binding.profilePhone.setText(sharedPrefs.getString(this, CPhone));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
