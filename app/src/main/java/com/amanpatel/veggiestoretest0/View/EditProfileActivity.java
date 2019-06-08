package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.ProfileViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityEditProfileBinding;
import com.amanpatel.veggiestoretest0.databinding.ActivityProfileBinding;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CName;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CPhone;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    ProfileViewModel profileviewmodel;
    SharedPrefs sharedPrefs;
    private String cName;
    private String cPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefs = new SharedPrefs(this);

        binding.fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileviewmodel.editUser(sharedPrefs.getString(EditProfileActivity.this, CID),binding.nameInput.getText().toString(),binding.phoneInput.getText().toString());
                sharedPrefs.saveString(EditProfileActivity.this, CName, binding.nameInput.getText().toString());
                sharedPrefs.saveString(EditProfileActivity.this, CPhone, binding.phoneInput.getText().toString());
                Toast.makeText(EditProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void bind(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        profileviewmodel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding.setProfileviewmodel(profileviewmodel);
        binding.setLifecycleOwner(this);
        cName = getIntent().getExtras().getString("Name");
        cPhone = getIntent().getExtras().getString("Phone");
        binding.nameInput.setText(cName);
        binding.phoneInput.setText(cPhone);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_password) {
            Intent intent = new Intent(EditProfileActivity.this, PasswordChangeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
