package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.OrderItems;
import com.amanpatel.veggiestoretest0.Models.ProductPrice;
import com.amanpatel.veggiestoretest0.Models.Products;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<OrderItems> mOrderItemList;
    private Context mContext;

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_row, parent, false);
        return new OrderDetailsAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        OrderItems item = mOrderItemList.get(position);

        holder.proName.setText(item.getTitle());
        holder.proNameHindi.setText(item.getHindiTitle());
        GlideApp.with(mContext).load(item.getImage()).centerCrop().into(holder.proImg);
        holder.weight.setText(String.format(mContext.getResources().getString(R.string.weight_unit), item.getWeight(), item.getUnit()));
        holder.proPriceTotal.setText(String.format(mContext.getResources().getString(R.string.rupees), item.getAmount()));
        holder.quantityTotal.setText(String.format(mContext.getResources().getString(R.string.cart_q_desc), item.getQty(), item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mOrderItemList.size();
    }

    public OrderDetailsAdapter(Context mContext, List<OrderItems> items) {
        this.mContext = mContext;
        this.mOrderItemList = items;
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



