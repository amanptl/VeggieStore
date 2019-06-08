package com.amanpatel.veggiestoretest0.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.amanpatel.veggiestoretest0.Models.User;


public class SharedPrefs {

    private Context context;
    private static final String myPreference = "vstorepref";
    public static final String Area = "areaVal";
    public static final String AreaID = "areaIdVal";
    public static final String DealerID = "dealerIdVal";
    public static final String AreaShipping = "areaShipping";
    public static final String AreaMinAmt = "areaMinAmt";
    public static final String City = "cityVal";
    public static final String CityId = "cityIdVal";
    public static final String isLoggedIn = "loggedInVal";
    public static final String CName = "User";
    public static final String CID = "cId";
    public static final String CPhone = "cPhone";
    public static final String CEmail = "cEmail";


    public SharedPrefs(Context context) {
        this.context = context;
    }

    public void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean findKey(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        return sharedPref.contains(key);
    }

    public void removeKey(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
}
