package com.amanpatel.veggiestoretest0.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;

public class WalletFrag extends Fragment {
    private MarketViewModel mainactivityviewmodel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView refreshButton;
    SharedPrefs sharedPrefs;
    TextView walletAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Wallet");
        sharedPrefs = new SharedPrefs(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        refreshButton = v.findViewById(R.id.refresh_button);
        walletAmount = v.findViewById(R.id.wallet_frag_amount);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        loadData();
        return v;
    }

    private void loadData(){
        mainactivityviewmodel.getWalletAmount(sharedPrefs.getString(getContext(), CID)).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                walletAmount.setText(String.format(getResources().getString(R.string.rupees), s));
            }
        });
    }
}
