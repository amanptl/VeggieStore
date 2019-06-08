package com.amanpatel.veggiestoretest0.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.DealerID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    public ProductsViewModel productsviewmodel;
    public static boolean noResult = true;
    private RecyclerView recyclerView;
    private ProductsAdapter mAdapter;
    private RelativeLayout emptyState;
    SharedPrefs sharedPrefs;
    private List<ProductsWithPrice> productsWithPricesList = new ArrayList<>();
    View view;
    Cart cartObj;
    int s;
    private ProgressDialog progressDialog;
    private List<Cart> cartItems = new ArrayList<>();
    private String loggedStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        productsviewmodel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setElevation(0);

        sharedPrefs = new SharedPrefs(SearchActivity.this);
        loggedStatus = sharedPrefs.getString(SearchActivity.this, isLoggedIn);
        emptyState = findViewById(R.id.empty_sate_search);
        recyclerView = findViewById(R.id.recycler_view_productsS);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.INVISIBLE);
        emptyState.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        productsviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                toastMaker(s);
                Snackbar.make(toolbar, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setActionTextColor(ContextCompat.getColor(SearchActivity.this, R.color.snackbarActionColor)).show();
            }
        });

        productsviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });
    }

    private void searchData(String searchString) {
        productsviewmodel.searchProducts(searchString, sharedPrefs.getString(SearchActivity.this, AreaID)).observe(this, new Observer<List<ProductsWithPrice>>() {
            @Override
            public void onChanged(@Nullable final List<ProductsWithPrice> products) {
                if (products != null && products.size() != 0) {
                    sharedPrefs.saveString(SearchActivity.this, DealerID, products.get(0).getProduct().getDealerid());
                    productsWithPricesList.clear();
                    cartItems.clear();
                    productsWithPricesList.addAll(products);
                    productsviewmodel.getCart(sharedPrefs.getString(SearchActivity.this, CID), sharedPrefs.getString(SearchActivity.this, AreaID)).observe(SearchActivity.this, new Observer<List<Cart>>() {
                        @Override
                        public void onChanged(List<Cart> carts) {
                            cartItems = carts;
                            setAdapter(productsWithPricesList, cartItems);
                        }
                    });
                } else {
                    toastMaker("Product not found.");
                    recyclerView.setVisibility(View.INVISIBLE);
                    emptyState.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setAdapter(List<ProductsWithPrice> pList, final List<Cart> mCart) {
        mAdapter = new ProductsAdapter(SearchActivity.this, pList, mCart, new CustomProductListener() {
            @Override
            public void onItemClick(View v, int position) {
                ProductsWithPrice item = productsWithPricesList.get(position);
                String productID = item.getProduct().getId();
                String productName = item.getProduct().getTitle();
                String cat2id = item.getProduct().getCat2id();
                String quantity = "0";
                Intent intent = new Intent(SearchActivity.this, ProductDetails.class);
                intent.putExtra("ProductName", productName);
                intent.putExtra("ProductID", productID);
                intent.putExtra("Cat2ID", cat2id);
                intent.putExtra("quantity", quantity);
                startActivity(intent);
            }

            @Override
            public Cart onAddClick(Cart c) {
                progressDialog.show();
                cartObj = c;
                cartObj.setCustid(sharedPrefs.getString(SearchActivity.this, CID));
                cartObj.setAreaid(sharedPrefs.getString(SearchActivity.this, AreaID));
                int quantity = Integer.valueOf(cartObj.getQuantity());
                if (quantity < 25) {
                    cartObj.setQuantity(productsviewmodel.addQuantity(cartObj.getQuantity()));
                    if (loggedStatus.equals("true")) {
                        if (cartObj.getId().equals("-1")) {
                            productsviewmodel.addItemToCart(cartObj).observe(SearchActivity.this, new Observer<Cart>() {
                                @Override
                                public void onChanged(Cart cart) {
                                    if (cartObj.getId().equals("-1")) {
                                        mAdapter.setCartID(cart.getId(), cart.getProid(), cart.getPriceid());
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            productsviewmodel.editCartItem(cartObj).observe(SearchActivity.this, new Observer<Cart>() {
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
                        if(cartObj.getQuantity().equals("1")){
                            CartActivity.guestCart.add(cartObj);
                            mAdapter.setCartID(cartObj.getId(), cartObj.getProid(), cartObj.getPriceid());
                            progressDialog.dismiss();
                        }
                        else{
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
                cartObj.setCustid(sharedPrefs.getString(SearchActivity.this, CID));
                cartObj.setAreaid(sharedPrefs.getString(SearchActivity.this, AreaID));
                cartObj.setQuantity(productsviewmodel.subQuantity(cartObj.getQuantity()));

                if (Integer.valueOf(cartObj.getQuantity()) > 0) {
                    if (loggedStatus.equals("true")) {
                        productsviewmodel.editCartItem(cartObj).observe(SearchActivity.this, new Observer<Cart>() {
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
                        productsviewmodel.deleteCartItem(cartObj.getId()).observe(SearchActivity.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                cartObj.setQuantity("0");
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                            if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && cartObj.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                cartObj.setQuantity("0");
                                CartActivity.guestCart.remove(i);
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
        recyclerView.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.searchS);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchItem.expandActionView();
        searchView.setQueryHint("Search the store");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cartItems.clear();
                productsWithPricesList.clear();
                setAdapter(productsWithPricesList, cartItems);
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.equals("")) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    emptyState.setVisibility(View.VISIBLE);
                }
                return false;
            }


        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
