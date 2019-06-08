package com.amanpatel.veggiestoretest0.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.NotificationAdapter;
import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.Models.Notifications;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;

public class NotificationFrag extends Fragment {
    public ActivityMainBinding binding;
    private MarketViewModel mainactivityviewmodel;
    private Context c;
    private RecyclerView recyclerView;
    private NotificationAdapter mAdapter;
    private SharedPrefs sharedPrefs;
    private List<Notifications> notificationsList = new ArrayList<>();
    private String areaID;
    private TextView emptyState;
    private SwipeRefreshLayout swipeRefreshLayout;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Notifications");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notifications, container, false);
        c = v.getContext();
        sharedPrefs = new SharedPrefs(c);

        emptyState = v.findViewById(R.id.empty_state_address);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_notification);

        areaID = sharedPrefs.getString(c, AreaID);

        recyclerView = v.findViewById(R.id.recycler_view_notification);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainactivityviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(v.getRootView(), R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                loadData();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }).setActionTextColor(ContextCompat.getColor(c, R.color.snackbarActionColor)).show();
            }
        });

        mainactivityviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });


        mAdapter = new NotificationAdapter(c, notificationsList);

        recyclerView.setAdapter(mAdapter);

        loadData();


        return v;
    }

    private void loadData() {
        mainactivityviewmodel.getNotifications(areaID).observe(this, new Observer<List<Notifications>>() {
            @Override
            public void onChanged(List<Notifications> notifications) {
                if (notifications != null && notifications.size() != 0) {
                    notificationsList.clear();
                    notificationsList.addAll(notifications);
                    mAdapter.notifyDataSetChanged();
                    emptyState.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    emptyState.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
