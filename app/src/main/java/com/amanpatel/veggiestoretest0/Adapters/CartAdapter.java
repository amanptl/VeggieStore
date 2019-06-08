package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Interface.CustomCartListListener;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Cart> mCartList;
    private Context mContext;
    private CustomCartListListener listener;
    private List<ProductsWithPrice> productsWithPricesList;
    private float fTotal = 0f;

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        final CartAdapter.ViewHolder mViewHolder = new CartAdapter.ViewHolder(mView);
        mViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(view, mViewHolder.getAdapterPosition(), mCartList.get(mViewHolder.getAdapterPosition()).getId(), mCartList.get(mViewHolder.getAdapterPosition()).getProid(), mCartList.get(mViewHolder.getAdapterPosition()).getPriceid());
                }
            }
        });

        mViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddClick(mViewHolder.getAdapterPosition(), mCartList.get(mViewHolder.getAdapterPosition()));
            }
        });

        mViewHolder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSubClick(mViewHolder.getAdapterPosition(), mCartList.get(mViewHolder.getAdapterPosition()));
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart item = mCartList.get(position);
        for (int i = 0; i < productsWithPricesList.size(); i++) {
            if (item.getProid().equals(productsWithPricesList.get(i).getProduct().getId())) {
                Products product = productsWithPricesList.get(i).getProduct();
                holder.proName.setText(product.getTitle());
                holder.proNameHindi.setText(product.getHindiTitle());
                GlideApp.with(mContext).load(product.getImage()).centerCrop().into(holder.proImg);
                for (int y = 0; y < productsWithPricesList.get(i).getPrices().size(); y++) {
                    if (item.getPriceid().equals(productsWithPricesList.get(i).getPrices().get(y).getId())) {
                        ProductPrice price = productsWithPricesList.get(i).getPrices().get(y);
                        holder.weight.setText(String.format(mContext.getResources().getString(R.string.weight_unit), price.getWeight(), price.getUnit()));
                        String dP = price.getDPrice();
                        String q = item.getQuantity();
//                        fTotal = fTotal + (Float.valueOf(q) * Float.valueOf(dP));
                        String total = String.valueOf(Float.valueOf(q) * Float.valueOf(dP));
                        holder.proPriceTotal.setText(String.format(mContext.getResources().getString(R.string.rupees), total));
                        holder.quantityTotal.setText(String.format(mContext.getResources().getString(R.string.cart_q_desc), q, dP));
                        holder.proQuantity.setText(q);
                        break;
                    }
                }
                break;
            }
        }
    }

//    public String getGuestTotal(){
//        return String.valueOf(fTotal);
//    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public CartAdapter(Context mContext, List<Cart> carts, List<ProductsWithPrice> productsWithPricesList, CustomCartListListener listener) {
        this.mContext = mContext;
        this.listener = listener;
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
        TextView proQuantity;
        ImageView add;
        ImageView sub;
        ImageView delete;

        ViewHolder(View v) {
            super(v);
            proImg = v.findViewById(R.id.product_img);
            proName = v.findViewById(R.id.product_name);
            proNameHindi = v.findViewById(R.id.product_name_hindi);
            weight = v.findViewById(R.id.weight);
            proPriceTotal = v.findViewById(R.id.product_price_total);
            quantityTotal = v.findViewById(R.id.quatity_total);
            proQuantity = v.findViewById(R.id.product_quantity);
            delete = v.findViewById(R.id.delete);
            add = v.findViewById(R.id.product_plus);
            sub = v.findViewById(R.id.product_minus);
        }

    }

}
