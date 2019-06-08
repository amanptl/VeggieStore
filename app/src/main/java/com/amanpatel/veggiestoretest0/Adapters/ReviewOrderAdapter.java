package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductPrice;
import com.amanpatel.veggiestoretest0.Models.Products;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewOrderAdapter extends RecyclerView.Adapter<ReviewOrderAdapter.ViewHolder> {
    private List<Cart> mCartList;
    private Context mContext;
    private List<ProductsWithPrice> productsWithPricesList;

    @NonNull
    @Override
    public ReviewOrderAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_row, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewOrderAdapter.ViewHolder holder, int position) {
        Cart item = mCartList.get(position);
        for (int i = 0; i < productsWithPricesList.size(); i++) {
            if (productsWithPricesList.get(i).getProduct().getId().equals(item.getProid())) {
                Products product = productsWithPricesList.get(i).getProduct();
                holder.proName.setText(productsWithPricesList.get(i).getProduct().getTitle());
                holder.proNameHindi.setText(productsWithPricesList.get(i).getProduct().getHindiTitle());
                GlideApp.with(mContext).load(productsWithPricesList.get(i).getProduct().getImage()).centerCrop().into(holder.proImg);
                for (int y = 0; y < productsWithPricesList.get(i).getPrices().size(); y++) {
                    if (productsWithPricesList.get(i).getPrices().get(y).getId().equals(item.getPriceid())) {
                        ProductPrice price = productsWithPricesList.get(i).getPrices().get(y);
                        holder.weight.setText(String.format(mContext.getResources().getString(R.string.weight_unit), price.getWeight(), price.getUnit()));
                        String dP = price.getDPrice();
                        String q = item.getQuantity();
                        String total = String.valueOf(Float.valueOf(q) * Float.valueOf(dP));
                        holder.proPriceTotal.setText(String.format(mContext.getResources().getString(R.string.rupees), total));
                        holder.quantityTotal.setText(String.format(mContext.getResources().getString(R.string.cart_q_desc), q, dP));
                    }
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public ReviewOrderAdapter(Context mContext, List<Cart> carts, List<ProductsWithPrice> productsWithPricesList) {
        this.mContext = mContext;
        this.mCartList = carts;
        this.productsWithPricesList = productsWithPricesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView proImg;
        TextView proName;
        TextView proNameHindi;
        TextView weight;
        TextView proPriceTotal;
        TextView quantityTotal;

        ViewHolder(View v) {
            super(v);
            proImg = v.findViewById(R.id.product_img);
            proName = v.findViewById(R.id.product_name);
            proNameHindi = v.findViewById(R.id.product_name_hindi);
            weight = v.findViewById(R.id.weight);
            proPriceTotal = v.findViewById(R.id.product_price_total);
            quantityTotal = v.findViewById(R.id.quatity_total);
        }

    }

}

