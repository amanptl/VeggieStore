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

import com.amanpatel.veggiestoretest0.Interface.CustomProductListener;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductPrice;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ProductsWithPrice> mProductWithPrice = new ArrayList<>();
    private Context mContext;
    private CustomProductListener listener;
    private ArrayAdapter<String> mAdapter;
    private List<String> priceList = new ArrayList<>();
    private List<ProductPrice> prices = new ArrayList<>();
    private List<Cart> cartList = new ArrayList<>();
    private Cart currentItem;
    private Cart cart = new Cart("-1", "", "", "", "", "", "", " ");


    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        final SearchAdapter.ViewHolder mViewHolder = new SearchAdapter.ViewHolder(mView);
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
                if (cartList!=null) {
                    for (int y = 0; y < cartList.size(); y++) {
                        if (item.getProduct().getId().equals(cartList.get(y).getProid())) {
                            if (item.getPrices().size() != 0) {
                                if (item.getPrices().get(i).getId().equals(cartList.get(y).getPriceid())) {
                                    mViewHolder.quantity.setText(cartList.get(y).getQuantity());
                                    cart.setQuantity(cartList.get(y).getQuantity());
                                    break;
                                }
                                else {
                                    mViewHolder.quantity.setText("0");
                                    cart.setQuantity("0");
                                }
                            }
                        }
                        else {
                            mViewHolder.quantity.setText("0");
                            cart.setQuantity("0");
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mViewHolder.addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int spinnerSelection = mViewHolder.spinner.getSelectedItemPosition();
                final ProductsWithPrice item = mProductWithPrice.get(mViewHolder.getAdapterPosition());
                currentItem = new Cart("-1", "", item.getProduct().getId(), item.getPrices().get(spinnerSelection).getId(), "", "", "", "");
                if (cartList!=null) {
                    for (int i = 0; i < cartList.size(); i++) {
                        if (item.getProduct().getId().equals(cartList.get(i).getProid())){
                            if(item.getPrices().get(spinnerSelection).getId().equals(cartList.get(i).getPriceid())){
                                currentItem = cartList.get(i);
                                break;
                            }
                        }
                    }
                }
                listener.onAddClick(currentItem);
            }
        });

        mViewHolder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductsWithPrice item = mProductWithPrice.get(mViewHolder.getAdapterPosition());
                currentItem = new Cart("-1", "", item.getProduct().getId(), item.getPrices().get(mViewHolder.spinner.getSelectedItemPosition()).getId(), "", "", "", "");
                if (cartList!=null) {
                    for (int i = 0; i < cartList.size(); i++) {
                        if (item.getProduct().getId().equals(cartList.get(i).getProid())){
                            if(item.getPrices().get(mViewHolder.spinner.getSelectedItemPosition()).getId().equals(cartList.get(i).getPriceid())){
                                currentItem = cartList.get(i);
                                break;
                            }
                        }
                    }
                }
                listener.onSubClick(currentItem);
            }
        });


        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        ProductsWithPrice item = mProductWithPrice.get(position);
        holder.proName.setText(item.getProduct().getTitle());
        cart.setProid(item.getProduct().getId());
        holder.proNameHindi.setText(item.getProduct().getHindiTitle());
        holder.quantity.setText("0");
        cart.setQuantity("0");
        GlideApp.with(mContext).load(item.getProduct().getImage()).centerCrop().into(holder.proUrl);
        priceList = new ArrayList<>();
        for (int i = 0; i < item.getPrices().size(); i++) {
            prices.addAll(item.getPrices());
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


    public SearchAdapter(Context mContext, List<ProductsWithPrice> productsWithPrices, List<Cart> cartList, CustomProductListener listener) {
        this.mContext = mContext;
        this.mProductWithPrice = productsWithPrices;
        this.listener = listener;
        this.cartList = cartList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
