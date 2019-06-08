package com.amanpatel.veggiestoretest0.View;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.ProductsAdapter;
import com.amanpatel.veggiestoretest0.ProductsViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.databinding.ActivityProductsBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsActivity extends AppCompatActivity {

    public ActivityProductsBinding binding;
    public ProductsViewModel productsviewmodel;
    public String Title = null;
    Toolbar toolbar;

    private RecyclerView recyclerView;
    private ProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        productsviewmodel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        binding.setProductsactivityviewmodel(productsviewmodel);
        binding.setLifecycleOwner(this);

        Title = getIntent().getExtras().getString("Title");
        toolbar = (Toolbar) findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);
        setTitle(Title);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


//        productsviewmodel.getProducts().observe(this, new Observer<List<Products>>() {
//            @Override
//            public void onChanged(@Nullable List<Products> products) {
//                proList = products;
//                mAdapter = new ProductsAdapter(ProductsActivity.this, proList, new CustomItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        Toast.makeText(ProductsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                recyclerView.setAdapter(mAdapter);
//                binding.progressBar.setVisibility(View.GONE);
//                binding.recyclerViewProducts.setVisibility(View.VISIBLE);
//            }
//        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_products);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products, menu);
        MenuItem searchItem = menu.findItem(R.id.searchP);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search products...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mAdapter.getFilter().filter(newText);
                return false;
            }


        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.black));
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.black));
                return true;
            }
        });
        return true;
    }

    public void createToast(View v) {
        Toast.makeText(ProductsActivity.this, "Quantity", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}
