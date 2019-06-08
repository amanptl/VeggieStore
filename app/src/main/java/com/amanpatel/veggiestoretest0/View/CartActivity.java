package com.amanpatel.veggiestoretest0.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.CartAdapter;
import com.amanpatel.veggiestoretest0.Interface.CustomCartListListener;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.ProductsViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityCartBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    ProductsViewModel productsviewmodel;
    SharedPrefs sharedPrefs;
    Toolbar toolbar;
    RecyclerView recyclerView;
    CartAdapter mAdapter;
    String cId = "";
    String areaName = "";
    List<Cart> cartItems = new ArrayList<>();
    List<ProductsWithPrice> productsWithPricesList = new ArrayList<>();
    private ProgressDialog progressDialog;
    public static List<Cart> guestCart = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        productsviewmodel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        binding.setProductsviewmodel(productsviewmodel);
        binding.setLifecycleOwner(this);
        toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);
        sharedPrefs = new SharedPrefs(this);
        setTitle("Cart");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        productsviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        productsviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(binding.getRoot(), R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getCart();
                            }
                        }).setActionTextColor(ContextCompat.getColor(CartActivity.this, R.color.snackbarActionColor)).show();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_cart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItems.size() != 0) {
                    if (sharedPrefs.getString(CartActivity.this, isLoggedIn).equals("true")) {
                        productsviewmodel.getAddresses(sharedPrefs.getString(CartActivity.this, CID), areaName).observe(CartActivity.this, new Observer<List<AddressBook>>() {
                            @Override
                            public void onChanged(List<AddressBook> addressBooks) {
                                if (addressBooks.size() != 0) {
                                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                                    startActivity(intent);
                                } else {
                                    toastMaker("Add an address first.");
                                    Intent intent = new Intent(CartActivity.this, NewAddressActivity.class);
                                    intent.putExtra("from", "cart");
                                    startActivity(intent);
                                }
                            }
                        });

                    } else {
                        Intent intent = new Intent(CartActivity.this, LogInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("from", "cart");
                        startActivity(intent);
                    }
                }
            }
        });

        binding.emptyStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cId = sharedPrefs.getString(this, CID);
        areaName = sharedPrefs.getString(this, Area);

        setmAdapter();
        getCart();

        getSetTotal();

    }

    private void getCart() {
        if (sharedPrefs.getString(this, isLoggedIn).equals("true")) {
            productsviewmodel.getCart(sharedPrefs.getString(this, CID), sharedPrefs.getString(this, AreaID)).observe(this, new Observer<List<Cart>>() {
                @Override
                public void onChanged(List<Cart> cartList) {
                    if (cartList != null && cartList.size() != 0) {
                        cartItems.clear();
                        cartItems.addAll(cartList);
                        binding.totalCount.setText(String.format(getResources().getString(R.string.cart_count), cartList.size()));
                        loadProductsWithPrices();
                        binding.progressBar1.setVisibility(View.INVISIBLE);
                        binding.recyclerViewCart.setVisibility(View.VISIBLE);
                        binding.totalCard.setVisibility(View.VISIBLE);
                        binding.emptyState.setVisibility(View.INVISIBLE);
                    } else {
                        binding.progressBar1.setVisibility(View.INVISIBLE);
                        binding.recyclerViewCart.setVisibility(View.INVISIBLE);
                        binding.totalCard.setVisibility(View.INVISIBLE);
                        binding.emptyState.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            if (guestCart != null && guestCart.size() != 0) {
                cartItems.clear();
                cartItems.addAll(guestCart);
                binding.totalCount.setText(String.format(getResources().getString(R.string.cart_count), guestCart.size()));
                loadProductsWithPrices();
                binding.progressBar1.setVisibility(View.INVISIBLE);
                binding.recyclerViewCart.setVisibility(View.VISIBLE);
                binding.totalCard.setVisibility(View.VISIBLE);
                binding.emptyState.setVisibility(View.INVISIBLE);
            } else {
                binding.progressBar1.setVisibility(View.INVISIBLE);
                binding.recyclerViewCart.setVisibility(View.INVISIBLE);
                binding.totalCard.setVisibility(View.INVISIBLE);
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getSetTotal() {
        if (sharedPrefs.getString(this, isLoggedIn).equals("true")) {
            productsviewmodel.getCartTotal(sharedPrefs.getString(this, CID), sharedPrefs.getString(this, AreaID)).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null)
                        binding.totalPrice.setText(String.format(getResources().getString(R.string.total_rupees), s));
                }
            });
        } else {
            productsviewmodel.getGCartTotal(CartActivity.guestCart).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null)
                        binding.totalPrice.setText(String.format(getResources().getString(R.string.total_rupees), s));
                }
            });

        }
    }

    private void loadProductsWithPrices() {
        productsWithPricesList.clear();
        for (int i = 0; i < cartItems.size(); i++) {
            productsviewmodel.getSingleProduct(cartItems.get(i).getProid()).observe(CartActivity.this, new Observer<ProductsWithPrice>() {
                @Override
                public void onChanged(ProductsWithPrice productsWithPrice) {
                    productsWithPricesList.add(productsWithPrice);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void setmAdapter() {
        mAdapter = new CartAdapter(CartActivity.this, cartItems, productsWithPricesList, new CustomCartListListener() {

            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onAddClick(final int position, Cart c) {
                progressDialog.show();
                int cQuantity = Integer.valueOf(c.getQuantity());
                if (cQuantity < 25) {
                    String quantity = productsviewmodel.addQuantity(c.getQuantity());
                    c.setQuantity(quantity);
                    if (sharedPrefs.getString(CartActivity.this, isLoggedIn).equals("true")) {
                        productsviewmodel.editCartItem(c).observe(CartActivity.this, new Observer<Cart>() {
                            @Override
                            public void onChanged(Cart cart) {
                                mAdapter.notifyDataSetChanged();
                                mAdapter.notifyItemChanged(position);
                                getSetTotal();
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                            if (c.getProid().equals(CartActivity.guestCart.get(i).getProid()) && c.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                CartActivity.guestCart.set(i, c);
                                cartItems.set(i, c);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.notifyItemChanged(position);
                                getSetTotal();
                                progressDialog.dismiss();
                                break;
                            }
                        }
                    }
                } else {
                    toastMaker("Maximum quantity per item reached.");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onSubClick(final int position, final Cart c) {
                progressDialog.show();
                String quantity = productsviewmodel.subQuantity(c.getQuantity());
                c.setQuantity(quantity);
                if (Integer.valueOf(c.getQuantity()) > 0) {
                    if (sharedPrefs.getString(CartActivity.this, isLoggedIn).equals("true")) {
                        productsviewmodel.editCartItem(c).observe(CartActivity.this, new Observer<Cart>() {
                            @Override
                            public void onChanged(Cart cart) {
                                mAdapter.notifyDataSetChanged();
                                mAdapter.notifyItemChanged(position);
                                getSetTotal();
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                            if (c.getProid().equals(CartActivity.guestCart.get(i).getProid()) && c.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                CartActivity.guestCart.set(i, c);
                                cartItems.set(i, c);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.notifyItemChanged(position);
                                getSetTotal();
                                progressDialog.dismiss();
                                break;
                            }
                        }
                    }
                } else {
                    if (sharedPrefs.getString(CartActivity.this, isLoggedIn).equals("true")) {
                        productsviewmodel.deleteCartItem(c.getId()).observe(CartActivity.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                for (int i = 0; i < cartItems.size(); i++) {
                                    if (cartItems.get(i).getProid().equals(c.getProid()) && cartItems.get(i).getPriceid().equals(c.getPriceid())) {
                                        cartItems.remove(i);
                                        binding.totalCount.setText(String.format(getResources().getString(R.string.cart_count), cartItems.size()));
                                        mAdapter.notifyItemRemoved(position);
                                        getSetTotal();
                                        progressDialog.dismiss();
                                        break;
                                    }
                                }
                            }
                        });
                    } else {
                        for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                            if (c.getProid().equals(CartActivity.guestCart.get(i).getProid()) && c.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                CartActivity.guestCart.remove(i);
                                cartItems.remove(i);
                                binding.totalCount.setText(String.format(getResources().getString(R.string.cart_count), guestCart.size()));
                                mAdapter.notifyItemRemoved(position);
                                getSetTotal();
                                progressDialog.dismiss();
                                break;
                            }
                        }
                    }

                    if (cartItems.size() == 0) {
                        binding.recyclerViewCart.setVisibility(View.INVISIBLE);
                        binding.totalCard.setVisibility(View.INVISIBLE);
                        binding.emptyState.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onDeleteClick(View v, final int position, final String id, final String proid, final String priceid) {
                progressDialog.show();
                if (sharedPrefs.getString(CartActivity.this, isLoggedIn).equals("true")) {
                    productsviewmodel.deleteCartItem(id).observe(CartActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            for (int i = 0; i < cartItems.size(); i++) {
                                if (cartItems.get(i).getId().equals(id)) {
                                    cartItems.remove(i);
                                    mAdapter.notifyItemRemoved(position);
                                    progressDialog.dismiss();
                                    break;
                                }
                            }
                            getSetTotal();
                            binding.totalCount.setText(String.format(getResources().getString(R.string.cart_count), cartItems.size()));
                        }
                    });
                }
                else{
                    for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                        if (proid.equals(CartActivity.guestCart.get(i).getProid()) && priceid.equals(CartActivity.guestCart.get(i).getPriceid())) {
                            CartActivity.guestCart.remove(i);
                            cartItems.remove(i);
                            mAdapter.notifyItemRemoved(position);
                            progressDialog.dismiss();
                            break;
                        }
                    }
                    getSetTotal();
                    binding.totalCount.setText(String.format(getResources().getString(R.string.cart_count), guestCart.size()));
                }
                if (cartItems.size() == 0) {
                    binding.recyclerViewCart.setVisibility(View.INVISIBLE);
                    binding.totalCard.setVisibility(View.INVISIBLE);
                    binding.emptyState.setVisibility(View.VISIBLE);
                }

            }
        });
        recyclerView.setAdapter(mAdapter);



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
