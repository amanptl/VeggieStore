package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.Models.ProductCategory1;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class Category1Adapter extends RecyclerView.Adapter<Category1Adapter.ViewHolder> {

    private List<ProductCategory1> catList;
    private Context mContext;
    private CustomItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCategory1 category1 = catList.get(position);
        holder.caText.setText(category1.getName());
        GlideApp.with(mContext).load(category1.getImage()).centerCrop().into(holder.catImg);
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public Category1Adapter(Context mContext, List<ProductCategory1> catList, CustomItemClickListener listener) {
        this.catList = catList;
        this.mContext = mContext;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView caText;
        ImageView catImg;

        ViewHolder(View v) {
            super(v);
            caText = v.findViewById(R.id.cat_text);
            catImg = v.findViewById(R.id.cat_img);
        }
    }
}
