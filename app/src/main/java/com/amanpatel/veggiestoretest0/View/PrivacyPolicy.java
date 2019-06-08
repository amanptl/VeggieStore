package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.amanpatel.veggiestoretest0.R;

public class PrivacyPolicy extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        webView = findViewById(R.id.ppa_webview);
        webView.loadUrl("http://dotcompreview.com/Veggiestore/privacypolicy");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
