package com.amanpatel.veggiestoretest0.Interface;

import android.view.View;

import com.amanpatel.veggiestoretest0.Models.AddressBook;

public interface CustomAddressListener {
    void onItemClick(View v, int position, String id);
    void onDeleteClick(View v, int position, String id);
    void onEditClick(View v, int position, AddressBook address);
}
