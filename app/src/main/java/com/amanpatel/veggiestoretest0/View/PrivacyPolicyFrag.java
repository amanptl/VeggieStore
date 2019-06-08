package com.amanpatel.veggiestoretest0.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class PrivacyPolicyFrag extends Fragment {
    private MarketViewModel mainactivityviewmodel;
    private Context c;
    View v;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Privacy Policy");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        c = v.getContext();
        webView = v.findViewById(R.id.pp_webview);
        webView.loadUrl("http://dotcompreview.com/Veggiestore/privacypolicy");
        return v;
    }
}
