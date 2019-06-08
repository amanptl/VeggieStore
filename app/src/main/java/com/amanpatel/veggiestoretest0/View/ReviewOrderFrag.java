package com.amanpatel.veggiestoretest0.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.CartAdapter;
import com.amanpatel.veggiestoretest0.Adapters.ReviewOrderAdapter;
import com.amanpatel.veggiestoretest0.Interface.CustomCartListListener;
import com.amanpatel.veggiestoretest0.Models.Areas;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.OrderViewModel;
import com.amanpatel.veggiestoretest0.ProductsViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityCartBinding;
import com.amanpatel.veggiestoretest0.databinding.FragmentReviewOrderBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.executor.TaskExecutor;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaMinAmt;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaShipping;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;

public class ReviewOrderFrag extends Fragment {
    private OrderViewModel orderViewModel;
    SharedPrefs sharedPrefs;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView shippingCharge;
    private TextView payableAmt;
    private ReviewOrderAdapter mAdapter;
    private List<Cart> cartItems = new ArrayList<>();
    private String shippingChrg;
    private String totalPayable = "";
    private String areaId;
    private AlertDialog.Builder alert;
    private List<ProductsWithPrice> productsWithPricesList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        super.onCreate(savedInstanceState);
        sharedPrefs = new SharedPrefs(getContext());
        alert = new AlertDialog.Builder(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_review_order, container, false);

        progressBar = v.findViewById(R.id.progressBar1);
        recyclerView = v.findViewById(R.id.recycler_view_review);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        shippingCharge = v.findViewById(R.id.shipping_charge);

        payableAmt = v.findViewById(R.id.payable_amt);

        areaId = sharedPrefs.getString(getContext(), AreaID);
        orderViewModel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(recyclerView, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getCartData();
                            }
                        }).setActionTextColor(ContextCompat.getColor(v.getContext(), R.color.snackbarActionColor)).show();
            }
        });

        orderViewModel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        setmAdapter();
        getCartData();
        return v;
    }

    private void getCartData() {
        orderViewModel.getCart(sharedPrefs.getString(getContext(), CID), sharedPrefs.getString(getContext(), AreaID)).observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> cartList) {
                if (cartList != null) {
                    cartItems.clear();
                    cartItems.addAll(cartList);
                    loadProductsWithPrices();
                }
            }
        });
    }

    private void loadProductsWithPrices() {
        productsWithPricesList.clear();
        for (int i = 0; i < cartItems.size(); i++) {
            orderViewModel.getSingleProduct(cartItems.get(i).getProid()).observe(ReviewOrderFrag.this, new Observer<ProductsWithPrice>() {
                @Override
                public void onChanged(ProductsWithPrice productsWithPrice) {
                    if (productsWithPrice != null) {
                        productsWithPricesList.add(productsWithPrice);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setmAdapter() {
        mAdapter = new ReviewOrderAdapter(getContext(), cartItems, productsWithPricesList);
        recyclerView.setAdapter(mAdapter);


        orderViewModel.getAreaDetails(areaId).observe(ReviewOrderFrag.this, new Observer<Areas>() {
            @Override
            public void onChanged(final Areas areas) {
                if (areas != null) {
                    orderViewModel.getCartTotal(sharedPrefs.getString(getContext(), CID), sharedPrefs.getString(getContext(), AreaID)).observe(ReviewOrderFrag.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s != null) {
                                totalPayable = s;
                                payableAmt.setText(totalPayable);
                                sharedPrefs.saveString(getContext(), AreaMinAmt, areas.getMinimumcharge());
                                sharedPrefs.saveString(getContext(), AreaShipping, areas.getShippingcharge());

                                if (Float.valueOf(totalPayable) >= Float.valueOf(areas.getMinimumcharge())) {
                                    shippingChrg = "0";
                                    shippingCharge.setText(String.format(getResources().getString(R.string.rupees), shippingChrg));
                                    payableAmt.setText(String.format(getResources().getString(R.string.rupees), totalPayable));
                                } else {
                                    shippingChrg = areas.getShippingcharge();
                                    shippingCharge.setText(String.format(getResources().getString(R.string.rupees), shippingChrg));
                                    totalPayable = String.valueOf(Float.valueOf(totalPayable) + Float.valueOf(shippingChrg));
                                    payableAmt.setText(String.format(getResources().getString(R.string.rupees), totalPayable));
                                    alert.setMessage(Html.fromHtml("<font color='#000000'>" + String.format(getResources().getString(R.string.checkout_alert), areas.getMinimumcharge()) + "</font>", 0));
                                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                }
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
