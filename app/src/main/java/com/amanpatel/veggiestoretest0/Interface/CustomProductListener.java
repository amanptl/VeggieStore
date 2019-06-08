package com.amanpatel.veggiestoretest0.Interface;

import android.view.View;
import android.widget.Spinner;

import com.amanpatel.veggiestoretest0.Models.Cart;

public interface CustomProductListener {
    void onItemClick(View v, int position);
    Cart onAddClick(Cart c);
    Cart onSubClick(Cart c);
}
