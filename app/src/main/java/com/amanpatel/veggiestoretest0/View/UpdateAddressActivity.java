package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amanpatel.veggiestoretest0.R;

public class UpdateAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
