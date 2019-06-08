package com.amanpatel.veggiestoretest0.Interface;

import android.view.View;

import com.amanpatel.veggiestoretest0.Models.Cart;

public interface CustomCartListListener {
    void onItemClick(View v, int position);
    void onAddClick(int position, Cart c);
    void onSubClick(int position, Cart c);
    void onDeleteClick(View v, int position, String id, String proid, String priceid);
}
