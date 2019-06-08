package com.amanpatel.veggiestoretest0.View;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductPrice;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.ProductsViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityProductDetailsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class ProductDetails extends AppCompatActivity {
    ProductsViewModel productsviewmodel;
    ActivityProductDetailsBinding binding;
    Toolbar toolbar;
    TextView marketPriceText;
    ArrayAdapter<String> dataAdapter;
    SharedPrefs sharedPrefs;
    String title;
    String cat2ID;
    AlertDialog.Builder priceDialog;
    public ArrayList<String> weightsString = new ArrayList<>();
    private List<ProductPrice> productPrice = new ArrayList<>();
    private List<String> priceString = new ArrayList<>();
    ArrayAdapter<String> pDataAdapter;
    Cart cartObj;
    Cart c;
    String cID;
    String aID;
    String productID;
    boolean inCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        productsviewmodel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        binding.setProductsviewmodel(productsviewmodel);
        binding.setLifecycleOwner(this);
        cat2ID = Objects.requireNonNull(getIntent().getExtras()).getString("Cat2ID");
        title = Objects.requireNonNull(getIntent().getExtras()).getString("ProductName");
        productID = Objects.requireNonNull(getIntent().getExtras()).getString("ProductID");
        toolbar = findViewById(R.id.toolbar_product_details);
        setSupportActionBar(toolbar);
        setTitle(title);
        sharedPrefs = new SharedPrefs(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cID = sharedPrefs.getString(this, CID);
        cartObj = new Cart("-1", sharedPrefs.getString(this, CID), productID, "", sharedPrefs.getString(this, AreaID), "0", "0", "");
        binding.quantity.setText(cartObj.getQuantity());

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
                                loadProduct();
                            }
                        }).setActionTextColor(ContextCompat.getColor(ProductDetails.this, R.color.snackbarActionColor)).show();
            }
        });

        priceDialog = new AlertDialog.Builder(this);
        pDataAdapter = new ArrayAdapter<>(ProductDetails.this,
                android.R.layout.simple_spinner_dropdown_item,
                priceString);
        priceDialog.setAdapter(pDataAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.weightBtn.setText(priceString.get(which));
                binding.marketPrice.setText(String.format(getResources().getString(R.string.rupees), productPrice.get(which).getAPrice()));
                binding.appPrice.setText(String.format(getResources().getString(R.string.rupees), productPrice.get(which).getDPrice()));
                float a = Float.valueOf(productPrice.get(which).getAPrice());
                float d = Float.valueOf(productPrice.get(which).getDPrice());
                binding.savePrice.setText(String.format(getResources().getString(R.string.rupees), String.valueOf(a - d)));
                cartObj.setPriceid(productPrice.get(which).getId());
                getCart(productPrice.get(which).getId());
            }
        });

        priceDialog.setTitle(Html.fromHtml("<font color='#000000'>Select Weight</font>", 0));
        priceDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        marketPriceText = findViewById(R.id.market_price);
        marketPriceText.setPaintFlags(marketPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        sharedPrefs = new SharedPrefs(this);

        loadProduct();

        binding.weightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceDialog.show();
            }
        });

        binding.addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(cartObj.getQuantity()) < 25) {
                    String quantity = productsviewmodel.addQuantity(cartObj.getQuantity());
                    cartObj.setQuantity(quantity);
                    binding.quantity.setText(cartObj.getQuantity());
                } else {
                    toastMaker("Maximum quantity reached.");
                }
            }
        });

        binding.minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = productsviewmodel.subQuantity(cartObj.getQuantity());
                cartObj.setQuantity(quantity);
                binding.quantity.setText(cartObj.getQuantity());
                if(quantity.equals("0")){
                    if (sharedPrefs.getString(ProductDetails.this, isLoggedIn).equals("true")) {
                        productsviewmodel.deleteCartItem(cartObj.getId()).observe(ProductDetails.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == 200) {
                                    finish();
                                }
                            }
                        });
                    }
                    else{
                        for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                            if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && cartObj.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                                CartActivity.guestCart.remove(i);
                                finish();
                            }
                        }
                    }
                }
            }
        });


        binding.specialInstruction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cartObj.setInstruction(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.toolbarLayoutProductDetails.setExpanded(false, true);
                binding.scrollView.smoothScrollTo(0, binding.scrollView.getBottom());
            }
        });


        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefs.getString(ProductDetails.this, isLoggedIn).equals("true")) {
                    productsviewmodel.getCart(sharedPrefs.getString(ProductDetails.this, CID), sharedPrefs.getString(ProductDetails.this, AreaID)).observe(ProductDetails.this, new Observer<List<Cart>>() {
                        @Override
                        public void onChanged(List<Cart> cartList) {
                            if (cartList != null) {
                                for (int i = 0; i < cartList.size(); i++) {
                                    if (cartObj.getProid().equals(cartList.get(i).getProid()) && cartObj.getPriceid().equals(cartList.get(i).getPriceid())) {
                                        inCart = true;
                                        break;
                                    }
                                }
                            } else {
                                add(cartObj);
                            }
                        }
                    });
                    if (inCart) {
                        edit(cartObj);
                    } else {
                        add(cartObj);
                    }
                } else {
                    boolean inGuestCart = false;
                    for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                        if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && cartObj.getPriceid().equals(CartActivity.guestCart.get(i).getPriceid())) {
                            inGuestCart = true;
                            if (Integer.valueOf(cartObj.getQuantity()) > 0) {
                                CartActivity.guestCart.set(i, cartObj);
                                binding.quantity.setText(cartObj.getQuantity());
                                binding.specialInstruction.setText(cartObj.getInstruction());
                                finish();
//                            } else {
//                                CartActivity.guestCart.remove(i);
//                                finish();
                            }
                            break;
                        }
                    }
                    if (!inGuestCart) {
                        if (Integer.valueOf(cartObj.getQuantity()) > 0) {
                            CartActivity.guestCart.add(cartObj);
                            binding.quantity.setText(cartObj.getQuantity());
                            binding.specialInstruction.setText(cartObj.getInstruction());
                            finish();
                        } else {
                            Toast.makeText(ProductDetails.this, "Please select the quantity", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void loadProduct() {

        productsviewmodel.getSingleProduct(productID).observe(this, new Observer<ProductsWithPrice>() {
            @Override
            public void onChanged(ProductsWithPrice productsWithPrice) {
                title = productsWithPrice.getProduct().getTitle();
                setTitle(title);
                binding.proHindiName.setText(productsWithPrice.getProduct().getHindiTitle());
                GlideApp.with(ProductDetails.this).load(productsWithPrice.getProduct().getImage()).centerCrop().into(binding.htabHeader);
                productPrice = productsWithPrice.getPrices();
                binding.marketPrice.setText(String.format(getResources().getString(R.string.rupees), productsWithPrice.getPrices().get(0).getAPrice()));
                binding.appPrice.setText(String.format(getResources().getString(R.string.rupees), productsWithPrice.getPrices().get(0).getDPrice()));
                float a = Float.valueOf(productsWithPrice.getPrices().get(0).getAPrice());
                float d = Float.valueOf(productsWithPrice.getPrices().get(0).getDPrice());
                binding.savePrice.setText(String.format(getResources().getString(R.string.rupees), String.valueOf(a - d)));
                priceString = new ArrayList<>();
                for (int i = 0; i < productsWithPrice.getPrices().size(); i++) {
                    priceString.add(String.format(getResources().getString(R.string.available_price), productsWithPrice.getPrices().get(i).getDPrice(), productsWithPrice.getPrices().get(i).getWeight(), productsWithPrice.getPrices().get(i).getUnit()));
                }
                binding.weightBtn.setText(priceString.get(0));
                cartObj.setPriceid(productPrice.get(0).getId());
                getCart(productPrice.get(0).getId());
                pDataAdapter.addAll(priceString);
                pDataAdapter.notifyDataSetChanged();
                String about = productsWithPrice.getProduct().getDESCabout();
                String benefits = productsWithPrice.getProduct().getDESCbenifits();
                String htu = productsWithPrice.getProduct().getDESChowtouse();
                if (about == null || about.equals("")) {
                    binding.aboutText.setText("Not available");
                } else {
                    binding.aboutText.setText(about);
                }
                if (benefits == null || benefits.equals("")) {
                    binding.benefitsText.setText("Not available");
                } else {
                    binding.benefitsText.setText(benefits);
                }
                if (htu == null || htu.equals("")) {
                    binding.howToUseText.setText("Not available");
                } else {
                    binding.howToUseText.setText(htu);
                }

            }

        });
    }

    private void getCart(final String priceId) {
        if (sharedPrefs.getString(ProductDetails.this, isLoggedIn).equals("true")) {
            productsviewmodel.getCart(sharedPrefs.getString(ProductDetails.this, CID), sharedPrefs.getString(ProductDetails.this, AreaID)).observe(this, new Observer<List<Cart>>() {
                @Override
                public void onChanged(List<Cart> cartList) {
                    if (cartList != null) {
                        for (int i = 0; i < cartList.size(); i++) {
                            if (cartObj.getProid().equals(cartList.get(i).getProid()) && priceId.equals(cartList.get(i).getPriceid())) {
                                cartObj.setId(cartList.get(i).getId());
                                cartObj.setQuantity(cartList.get(i).getQuantity());
                                cartObj.setInstruction(cartList.get(i).getInstruction());
                                binding.quantity.setText(cartObj.getQuantity());
                                binding.specialInstruction.setText(cartObj.getInstruction());
                                break;
                            } else {
                                cartObj.setId("-1");
                                cartObj.setQuantity("0");
                                cartObj.setInstruction("");
                                binding.quantity.setText(cartObj.getQuantity());
                                binding.specialInstruction.setText(cartObj.getInstruction());
                            }
                        }
                    }
                }
            });
        } else {
            for (int i = 0; i < CartActivity.guestCart.size(); i++) {
                if (cartObj.getProid().equals(CartActivity.guestCart.get(i).getProid()) && priceId.equals(CartActivity.guestCart.get(i).getPriceid())) {
                    cartObj.setId(CartActivity.guestCart.get(i).getId());
                    cartObj.setQuantity(CartActivity.guestCart.get(i).getQuantity());
                    cartObj.setInstruction(CartActivity.guestCart.get(i).getInstruction());
                    binding.quantity.setText(cartObj.getQuantity());
                    binding.specialInstruction.setText(cartObj.getInstruction());
                    break;
                } else {
                    cartObj.setId("-1");
                    cartObj.setQuantity("0");
                    cartObj.setInstruction("");
                    binding.quantity.setText(cartObj.getQuantity());
                    binding.specialInstruction.setText(cartObj.getInstruction());
                }
            }
        }
    }

    private void add(Cart cartObject) {
        if (Integer.valueOf(cartObject.getQuantity()) > 0) {
            productsviewmodel.addItemToCart(cartObject).observe(ProductDetails.this, new Observer<Cart>() {
                @Override
                public void onChanged(Cart cart) {
                    cartObj = cart;
                    binding.quantity.setText(cartObj.getQuantity());
                    binding.specialInstruction.setText(cartObj.getInstruction());
                    finish();
                }
            });
        } else {
            Toast.makeText(ProductDetails.this, "Please select the quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private void edit(Cart cartObject) {
        if (Integer.valueOf(cartObject.getQuantity()) > 0) {
            productsviewmodel.editCartItem(cartObject).observe(ProductDetails.this, new Observer<Cart>() {
                @Override
                public void onChanged(Cart cart) {
                    cartObj = cart;
                    binding.quantity.setText(cartObj.getQuantity());
                    binding.specialInstruction.setText(cartObj.getInstruction());
                    finish();
                }
            });
        } else {
            productsviewmodel.deleteCartItem(cartObject.getId()).observe(ProductDetails.this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    finish();
                }
            });
        }
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
