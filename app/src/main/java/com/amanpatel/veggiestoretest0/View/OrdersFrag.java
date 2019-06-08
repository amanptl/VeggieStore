package com.amanpatel.veggiestoretest0.View;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.AddressAdapter;
import com.amanpatel.veggiestoretest0.Adapters.OrdersAdapter;
import com.amanpatel.veggiestoretest0.Interface.CustomAddressListener;
import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.Interface.CustomOrderClickListener;
import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.OrderHistory;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class OrdersFrag extends Fragment {
    public ActivityMainBinding binding;
    private MarketViewModel mainactivityviewmodel;
    private SharedPrefs sharedPrefs;
    private ProgressBar progressBar;
    View v;
    private Context c;
    private RecyclerView recyclerView;
    private OrdersAdapter mAdapter;
    private List<OrderHistory> orderList = new ArrayList<>();
    private TextView emptyState;
    private MaterialButton emptyStateButton;
    private String cid;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Orders");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_orders, container, false);
        c = v.getContext();
        sharedPrefs = new SharedPrefs(c);
        cid = sharedPrefs.getString(c, CID);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        progressBar = v.findViewById(R.id.progressBar1);
        emptyState = v.findViewById(R.id.empty_state_orders);
        emptyStateButton = v.findViewById(R.id.empty_state_orders_button);
        recyclerView = v.findViewById(R.id.recycler_view_orders);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainactivityviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                swipeRefreshLayout.setRefreshing(false);
                toastMaker(s);
            }
        });

        mAdapter = new OrdersAdapter(c, orderList, new CustomOrderClickListener() {
            @Override
            public void onItemClick(View v, int id, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("orderNum", orderList.get(position).getObjordermaster().getOrdernumber());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        if(sharedPrefs.getString(getContext(), isLoggedIn).equals("true")) {
            loadData();
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            emptyState.setVisibility(View.VISIBLE);
            emptyStateButton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    private void loadData(){
        mainactivityviewmodel.getOrders(cid).observe(this, new Observer<List<OrderHistory>>() {
            @Override
            public void onChanged(List<OrderHistory> orderHistories) {
                orderList.clear();
                if (orderHistories != null) {
                    orderList.addAll(orderHistories);
                    if (orderList.size() != 0) {
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.INVISIBLE);
                        emptyStateButton.setVisibility(View.INVISIBLE);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyState.setVisibility(View.VISIBLE);
                        emptyStateButton.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
