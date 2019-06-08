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
import com.amanpatel.veggiestoretest0.Interface.CustomAddressListener;
import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class AddressFrag extends Fragment {
    public ActivityMainBinding binding;
    private MarketViewModel mainactivityviewmodel;
    private Context c;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private AddressAdapter mAdapter;
    private ProgressBar progressBar;
    private SharedPrefs sharedPrefs;
    private List<AddressBook> addressesList = new ArrayList<>();
    private String cid;
    private String currentArea;
    private TextView emptyState;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Addresses");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_address, container, false);
        c = v.getContext();
        sharedPrefs = new SharedPrefs(c);
        fab = v.findViewById(R.id.fabAFrag);
        progressBar = v.findViewById(R.id.progressBar1);
        emptyState = v.findViewById(R.id.empty_state_address);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefs.findKey(c, isLoggedIn)) {
                    if(addressesList.size()<3) {
                        Intent intent = new Intent(getActivity(), NewAddressActivity.class);
                        startActivity(intent);
                    }
                    else
                        toastMaker("Only 3 addresses are allowed.");
                } else
                    toastMaker("Log in to add a address");
            }
        });

        cid = sharedPrefs.getString(c, CID);
        currentArea = sharedPrefs.getString(c, Area);

        recyclerView = v.findViewById(R.id.recycler_view_address);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainactivityviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(fab, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData();
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


        mAdapter = new AddressAdapter(c, addressesList, new CustomAddressListener() {
            @Override
            public void onItemClick(View v, int position, String id) {
                mainactivityviewmodel.makeAddressDefault(id, sharedPrefs.getString(c, CID), sharedPrefs.getString(c, Area));
                for(int i = 0; i<addressesList.size(); i++){
                    if(i == position){
                        addressesList.get(i).setMobiledefault("true");
                    }
                    else
                        addressesList.get(i).setMobiledefault("false");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteClick(View v, int position, String id) {
                String cid = sharedPrefs.getString(c, CID);
                mainactivityviewmodel.deleteAddress(cid, id);
                addressesList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, addressesList.size());
            }

            @Override
            public void onEditClick(View v, int position, AddressBook address) {
                Intent intent = new Intent(getContext(), EditAddressActivity.class);
                intent.putExtra("aid",address.getId());
                intent.putExtra("name",address.getName());
                intent.putExtra("email",address.getEmail());
                intent.putExtra("phone",address.getPhone());
                intent.putExtra("address",address.getAddress());
                intent.putExtra("landmark",address.getLandmark());
                intent.putExtra("pin",address.getPostCode());
                intent.putExtra("area",address.getDistrict());
                intent.putExtra("city",address.getCity());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        if(sharedPrefs.getString(getContext(), isLoggedIn).equals("true")) {
            loadData();
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }


        return v;
    }

    private void loadData(){
        mainactivityviewmodel.getAddresses(cid, currentArea).observe(this, new Observer<List<AddressBook>>() {
            @Override
            public void onChanged(final List<AddressBook> addressBooks) {
                addressesList.clear();
                if (addressBooks != null) {
                    addressesList.addAll(addressBooks);
                    if (addressesList.size() != 0) {
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.INVISIBLE);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyState.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                    mAdapter.notifyDataSetChanged();

                    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeChanged(int positionStart, int itemCount) {
                            super.onItemRangeChanged(positionStart, itemCount);
                            if (itemCount == 0) {
                                progressBar.setVisibility(View.INVISIBLE);
                                emptyState.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
