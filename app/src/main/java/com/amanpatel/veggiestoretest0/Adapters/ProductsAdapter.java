package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.Interface.CustomProductListener;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductPrice;
import com.amanpatel.veggiestoretest0.Models.Products;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<ProductsWithPrice> mProductWithPrice = new ArrayList<>();
    private Context mContext;
    private CustomProductListener listener;
    private ArrayAdapter<String> mAdapter;
    private List<String> priceList = new ArrayList<>();
    private List<Cart> cartList;


    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        final ProductsAdapter.ViewHolder mViewHolder = new ProductsAdapter.ViewHolder(mView);


        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        mViewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProductsWithPrice item = mProductWithPrice.get(mViewHolder.getAdapterPosition());
                if (cartList.size() != 0) {
                    for (int y = 0; y < cartList.size(); y++) {
                        if (item.getProduct().getId().equals(cartList.get(y).getProid())) {
                            if (item.getPrices().size() != 0) {
                                if (item.getPrices().get(i).getId().equals(cartList.get(y).getPriceid())) {
                                    mViewHolder.quantity.setText(cartList.get(y).getQuantity());
                                    break;
                                } else {
                                    mViewHolder.quantity.setText("0");
                                }
                            } else {
                                mViewHolder.quantity.setText("0");
                            }
                        } else {
                            mViewHolder.quantity.setText("0");
                        }
                    }
                } else {
                    mViewHolder.quantity.setText("0");
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mViewHolder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart item = checkInCart(mViewHolder.getAdapterPosition(), mViewHolder.spinner.getSelectedItemPosition());
                Cart cartObj = listener.onAddClick(item);


                if (cartList.size() != 0) {
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getProid().equals(cartObj.getProid()) && cartList.get(i).getPriceid().equals(cartObj.getPriceid())) {
                            cartList.get(i).setQuantity(cartObj.getQuantity());
                            break;
                        } else if (i == cartList.size() - 1) {
                            cartList.add(cartObj);
                        }
                    }
                } else {
                    cartList.add(cartObj);

                }
                mViewHolder.quantity.setText(cartObj.getQuantity());
            }
        });

        mViewHolder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart item = checkInCart(mViewHolder.getAdapterPosition(), mViewHolder.spinner.getSelectedItemPosition());
                Cart cartObj = listener.onSubClick(item);

                if (cartList.size() != 0) {
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getProid().equals(cartObj.getProid()) && cartList.get(i).getPriceid().equals(cartObj.getPriceid())) {
                            if (!cartObj.getQuantity().equals("0")) {
                                cartList.set(i, cartObj);
                                break;
                            } else {
                                cartList.remove(i);
                                break;
                            }
                        }
                    }
                }
                mViewHolder.quantity.setText(cartObj.getQuantity());
            }
        });


        return mViewHolder;
    }

    private Cart checkInCart(int position, int spinnerSelection) {
        ProductsWithPrice item = mProductWithPrice.get(position);
        Cart currentItem = new Cart("-1", "", item.getProduct().getId(), item.getPrices().get(spinnerSelection).getId(), "", "0", "", "");

        if (cartList.size() != 0) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getProid().equals(currentItem.getProid()) && cartList.get(i).getPriceid().equals(currentItem.getPriceid())) {
                    currentItem = cartList.get(i);
                    break;
                }
            }
        }
        return currentItem;
    }

    public void setCartID(String id, String proid, String priceid) {
        if (cartList.size() != 0) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getProid().equals(proid) && cartList.get(i).getPriceid().equals(priceid)) {
                    cartList.get(i).setId(id);
                    break;
                }
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {


        ProductsWithPrice item = mProductWithPrice.get(position);
        holder.proName.setText(item.getProduct().getTitle());
        holder.proNameHindi.setText(item.getProduct().getHindiTitle());
        GlideApp.with(mContext).load(item.getProduct().getImage()).centerCrop().into(holder.proUrl);
        holder.quantity.setText("  ");
        priceList = new ArrayList<>();
        for (int i = 0; i < item.getPrices().size(); i++) {
            priceList.add(String.format(mContext.getResources().getString(R.string.available_price), item.getPrices().get(i).getDPrice(), item.getPrices().get(i).getWeight(), item.getPrices().get(i).getUnit()));
        }
        mAdapter = new ArrayAdapter<>(mContext, R.layout.product_spinner_item, priceList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(mAdapter);

    }


    @Override
    public int getItemCount() {
        return mProductWithPrice.size();
    }


    public ProductsAdapter(Context mContext, List<ProductsWithPrice> productsWithPrices, List<Cart> cartList, CustomProductListener listener) {
        this.mContext = mContext;
        this.mProductWithPrice = productsWithPrices;
        this.listener = listener;
        this.cartList = cartList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView proName;
        TextView proNameHindi;
        TextView quantity;
        ImageView proUrl;
        ImageView removeIcon;
        ImageView addIcon;
        Spinner spinner;

        ViewHolder(View v) {
            super(v);
            proUrl = v.findViewById(R.id.product_img);
            proName = v.findViewById(R.id.product_name);
            proNameHindi = v.findViewById(R.id.product_name_hindi);
            removeIcon = v.findViewById(R.id.product_minus10);
            addIcon = v.findViewById(R.id.product_plus10);
            spinner = v.findViewById(R.id.product_weight_spiner);
            quantity = v.findViewById(R.id.quantity);
        }
    }
}