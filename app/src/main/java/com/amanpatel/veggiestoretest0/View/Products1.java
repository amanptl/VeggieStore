
package com.amanpatel.veggiestoretest0.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductCategory2;
import com.amanpatel.veggiestoretest0.Models.Products;
import com.amanpatel.veggiestoretest0.ProductsViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityProducts1Binding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class Products1 extends AppCompatActivity {
    public ActivityProducts1Binding binding;
    Toolbar toolbar;
    SharedPrefs sharedPrefs;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public String Title;
    public String Cat1id;
    ProductsViewModel productsViewModel;
    private List<ProductCategory2> proCat2 = new ArrayList<>();
    public List<String> cat2List = new ArrayList<>();
    Bundle pageTitleBundle;
    TextView textCartItemCount;
    int mCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products1);
        Title = Objects.requireNonNull(getIntent().getExtras()).getString("Title");
        Cat1id = Objects.requireNonNull(getIntent().getExtras()).getString("Category1ID");
        toolbar = binding.toolbartest;
        setSupportActionBar(toolbar);
        setTitle(Title);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        viewPager = binding.viewpager;
        sharedPrefs = new SharedPrefs(this);
        loadData();

        productsViewModel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                toastMaker(s);
                Snackbar.make(binding.coordinatorProducts1, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData();
                            }
                        }).setActionTextColor(ContextCompat.getColor(Products1.this, R.color.snackbarActionColor)).show();
            }
        });

    }


    private void loadData() {

        productsViewModel.getCategory2(Cat1id).observe(this, new Observer<List<ProductCategory2>>() {
            @Override
            public void onChanged(List<ProductCategory2> productCategory2s) {
                proCat2 = productCategory2s;
                cat2List.clear();
                if (productCategory2s != null && productCategory2s.size() != 0) {
                    for (int i = 0; i < productCategory2s.size(); i++) {
                        if (productCategory2s.get(i).getVisible().equals("true"))
                            cat2List.add(productCategory2s.get(i).getName());
                    }
                }
                viewPager.setOffscreenPageLimit(cat2List.size());
                setupViewPager(viewPager);
                tabLayout = binding.tabs;
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < cat2List.size(); i++) {
            pageTitleBundle = new Bundle();
            pageTitleBundle.putString("pageTitle", cat2List.get(i));
            pageTitleBundle.putString("cat1ID", Cat1id);
            pageTitleBundle.putString("cat2ID", proCat2.get(i).getId());
            Products1Frag frag = new Products1Frag();
            frag.setArguments(pageTitleBundle);
            adapter.addFragment(frag, cat2List.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.searchP) {
            Intent intent = new Intent(Products1.this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_cart) {
            Intent intent = new Intent(Products1.this, CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBadge() {
        if (sharedPrefs.getString(Products1.this, isLoggedIn).equals("true")) {
            productsViewModel.getCart(sharedPrefs.getString(Products1.this, CID), sharedPrefs.getString(Products1.this, AreaID)).observe(this, new Observer<List<Cart>>() {
                @Override
                public void onChanged(List<Cart> cartList) {
                    mCartItemCount = cartList.size();
                    if (textCartItemCount != null) {
                        if (mCartItemCount == 0) {
                            if (textCartItemCount.getVisibility() != View.GONE) {
                                textCartItemCount.setVisibility(View.GONE);
                            }
                        } else {
                            textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        } else {
            mCartItemCount = CartActivity.guestCart.size();
            if (textCartItemCount != null) {
                if (mCartItemCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}