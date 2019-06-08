package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.amanpatel.veggiestoretest0.R;

public class TermsAndConditions extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        webView = findViewById(R.id.tnc_webview);
        webView.loadUrl("http://dotcompreview.com/Veggiestore/termsandconditions");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
