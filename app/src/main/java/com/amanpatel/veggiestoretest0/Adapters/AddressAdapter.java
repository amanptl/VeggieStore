package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Interface.CustomAddressListener;
import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.View.EditAddressActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private List<AddressBook> mFilteredList = new ArrayList<>();
    private CustomAddressListener listener;
    private Context mContext;
    private MutableLiveData<String> defID = new MutableLiveData<>();
    private String delID = "-1";
    private int editID;
    Intent intent;

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_row, parent, false);
        final AddressAdapter.ViewHolder mViewHolder = new AddressAdapter.ViewHolder(mView);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onItemClick(view, mViewHolder.getAdapterPosition(), mFilteredList.get(mViewHolder.getAdapterPosition()).getId());
            }
        });

        mViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.getAdapterPosition() != RecyclerView.NO_POSITION)
                    listener.onDeleteClick(view, mViewHolder.getAdapterPosition(), mFilteredList.get(mViewHolder.getAdapterPosition()).getId());
            }
        });
        mViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onEditClick(view, mViewHolder.getAdapterPosition(), mFilteredList.get(mViewHolder.getAdapterPosition()));
                }

            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.name.setText(mFilteredList.get(position).getName());
        holder.email.setText(mFilteredList.get(position).getEmail());
        holder.phone.setText(mFilteredList.get(position).getPhone());
        holder.address.setText(mFilteredList.get(position).getAddress());
        holder.area.setText(mFilteredList.get(position).getDistrict());
        holder.city.setText(mFilteredList.get(position).getCity());
        holder.pin.setText(mFilteredList.get(position).getPostCode());
        holder.landmark.setText(mFilteredList.get(position).getLandmark());
        if (mFilteredList.get(position).getMobiledefault().equals("true")) {
            holder.isDefault.setVisibility(View.VISIBLE);
        } else {
            holder.isDefault.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public AddressAdapter(Context mContext, List<AddressBook> addresses, CustomAddressListener listener) {
        this.mContext = mContext;
        this.mFilteredList = addresses;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView phone;
        TextView address;
        TextView area;
        TextView city;
        TextView pin;
        TextView landmark;
        ImageView isDefault;
        ImageView delete;
        ImageView edit;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            email = v.findViewById(R.id.email);
            phone = v.findViewById(R.id.phone);
            address = v.findViewById(R.id.address);
            area = v.findViewById(R.id.area);
            city = v.findViewById(R.id.city);
            pin = v.findViewById(R.id.pin);
            landmark = v.findViewById(R.id.landmark);
            isDefault = v.findViewById(R.id.is_default);
            delete = v.findViewById(R.id.delete);
            edit = v.findViewById(R.id.edit);
        }
    }
}