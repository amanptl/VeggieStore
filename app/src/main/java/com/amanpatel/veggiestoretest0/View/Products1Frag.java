package com.amanpatel.veggiestoretest0.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.ProductsAdapter;
import com.amanpatel.veggiestoretest0.Interface.CustomProductListener;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.ProductsViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.DealerID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class Products1Frag extends Fragment {
    private ProductsViewModel productsViewModel;
    SharedPrefs sharedPrefs;
    private List<ProductsWithPrice> productsWithPricesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsAdapter mAdapter;
    private ProgressBar pBar;
    private Context c;
    private RelativeLayout emptyState;
    View view;
    private Cart cartObj;
    private String pageTitle = "Not";
    private String cat1ID;
    private String cat2ID;
    int s;
    private List<Cart> cartItems = new ArrayList<>();
    private View v;
    private ProgressDialog progressDialog;
    private String loggedStatus = "";
    private int adapterFlag = 1;
    private boolean allowRefresh = false;

    public Products1Frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageTitle = getArguments().getString("pageTitle");
            cat1ID = getArguments().getString("cat1ID");
            cat2ID = getArguments().getString("cat2ID");
        }
        sharedPrefs = new SharedPrefs(getContext());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_products1, container, false);
        view = v;
        c = v.getContext();
        sharedPrefs = new SharedPrefs(c);
        emptyState = v.findViewById(R.id.empty_sate);
        pBar = v.findViewById(R.id.progressBar1);
        recyclerView = v.findViewById(R.id.recycler_view_products1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        loggedStatus = sharedPrefs.getString(getContext(), isLoggedIn);
        loadData();

        productsViewModel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                toastMaker(s);
                Snackbar.make(v, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData();
                            }
                        }).setActionTextColor(ContextCompat.getColor(c, R.color.snackbarActionColor)).show();
            }
        });

        productsViewModel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });
        return v;
    }

    private void loadData() {
        productsViewModel.getProducts(sharedPrefs.getString(c, AreaID), cat2ID).observe(this, new Observer<List<ProductsWithPrice>>() {
            @Override
            public void onChanged(@Nullable List<ProductsWithPrice> products) {
                if (products != null && products.size() != 0) {
                    sharedPrefs.saveString(getContext(), DealerID, products.get(0).getProduct().getDealerid());
                    productsWithPricesList.clear();
                    productsWithPricesList.addAll(products);
                    loadCart();
                } else {
                    pBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    emptyState.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void loadCart() {
        if (loggedStatus.equals("true")) {
            cartItems = new ArrayList<>();
            cartItems.clear();
            productsViewModel.getCart(sharedPrefs.getString(c, CID), sharedPrefs.getString(c, AreaID)).observe(Products1Frag.this, new Observer<List<Cart>>() {
                @Override
                public void onChanged(List<Cart> carts) {
                    if (carts.size() != 0) {
                        cartItems.addAll(carts);
                        setAdapter(productsWithPricesList);
                    } else {
                        cartItems = new ArrayList<>();
                        setAdapter(productsWithPricesList);
                    }
                }
            });
        } else {
            cartItems.clear();
            cartItems.addAll(CartActivity.guestCart);
            setAdapter(productsWithPricesList);
        }
    }

    private void setAdapter(List<ProductsWithPrice> pList) {
        if (adapterFlag == 1) {
            adapterFlag = 0;
            mAdapter = new ProductsAdapter(c, pList, cartItems, new CustomProductListener() {
                @Override
                public void onItemClick(View v, int position) {
                    ProductsWithPrice item = productsWithPricesList.get(position);
                    String productID = item.getProduct().getId();
                    String productName = item.getProduct().getTitle();
                    String quantity = "0";
                    Intent intent = new Intent(getActivity(), ProductDetails.class);
                    intent.putExtra("ProductName", productName);
                    intent.putExtra("ProductID", productID);
                    intent.putExtra("Cat2ID", cat2ID);
                    intent.putExtra("quantity", quantity);
                    startActivity(intent);
                }

                @Override
                public Cart onAddClick(Cart c) {
                    progressDialog.show();
                    cartObj = c;
                    cartObj.setCustid(sharedPrefs.getString(getContext(), CID));
                    cartObj.setAreaid(sharedPrefs.getString(getContext(), AreaID));
                    int quantity = Integer.valueOf(cartObj.getQuantity());
                    if (quantity < 25) {
                        cartObj.setQuantity(productsViewModel.addQuantity(cartObj.getQuantity()));
                        if (loggedStatus.equals("true")) {
                            if (cartObj.getId().equals("-1")) {
                                productsViewModel.addItemToCart(cartObj).observe(Products1Frag.this, new Observer<Cart>() {
                                    @Override
                                    public void onChanged(Cart cart) {
                                        if (cartObj.getId().equals("-1")) {

                                            mAdapter.setCartID(cart.getId(), cart.getProid(), cart.getPriceid());
                                            ((Products1) getActivity()).setupBadge();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            } else {
                                productsViewModel.editCartItem(cartObj).observe(Products1Frag.this, new Observer<Cart>() {
                                    @Override
                                    public void onChanged(Cart cart) {
                                        if (cartObj.getQuantity().equals(cart.getQuantity())) {
                                            mAdapter.setCartID(cart.getId(), cart.getProid(), cart.getPriceid());
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        } else {
                            if (cartObj.getQuantity().equals("1")) {
                                CartActivity.guestCart.add(cartObj);
                                mAdapter.setCartID(cartObj.getId(), cartObj.getProid(), cartObj.getPriceid());
                                ((Products1) getActivity()).setupBadge();
                                progressDialog.dismiss();
                            } else {
                                for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                                    if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && cartObj.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                        CartActivity.guestCart.set(i, cartObj);
                                        mAdapter.setCartID(cartObj.getId(), cartObj.getProid(), cartObj.getPriceid());
                                        progressDialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        toastMaker("Maximum quantity per item reached.");
                        progressDialog.dismiss();
                    }
                    return cartObj;
                }

                @Override
                public Cart onSubClick(Cart c) {
                    progressDialog.show();
                    cartObj = c;
                    cartObj.setCustid(sharedPrefs.getString(getContext(), CID));
                    cartObj.setAreaid(sharedPrefs.getString(getContext(), AreaID));
                    cartObj.setQuantity(productsViewModel.subQuantity(cartObj.getQuantity()));

                    if (Integer.valueOf(cartObj.getQuantity()) > 0) {
                        if (loggedStatus.equals("true")) {
                            productsViewModel.editCartItem(cartObj).observe(Products1Frag.this, new Observer<Cart>() {
                                @Override
                                public void onChanged(Cart cart) {
                                    if (cartObj.getQuantity().equals(cart.getQuantity())) {
                                        mAdapter.setCartID(cart.getId(), cart.getProid(), cart.getPriceid());
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                                if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && cartObj.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                    CartActivity.guestCart.set(i, cartObj);
                                    mAdapter.setCartID(cartObj.getId(), cartObj.getProid(), cartObj.getPriceid());
                                    progressDialog.dismiss();
                                }
                            }
                        }

                    } else {
                        if (loggedStatus.equals("true")) {
                            productsViewModel.deleteCartItem(cartObj.getId()).observe(Products1Frag.this, new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer integer) {
                                    cartObj.setQuantity("0");
                                    ((Products1) getActivity()).setupBadge();
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                                if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && cartObj.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                    cartObj.setQuantity("0");
                                    CartActivity.guestCart.remove(i);
                                    ((Products1) getActivity()).setupBadge();
                                    break;
                                }
                            }
                            progressDialog.dismiss();
                        }
                    }

                    return cartObj;
                }
            });
            recyclerView.setAdapter(mAdapter);
            allowRefresh = true;
            pBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Products1) getActivity()).setupBadge();
        cartObj = new Cart("", "", "", "", "", "", "", "");
        adapterFlag = 1;
        loadData();
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}

