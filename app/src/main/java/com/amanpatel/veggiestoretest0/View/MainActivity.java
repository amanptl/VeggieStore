package com.amanpatel.veggiestoretest0.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;


import android.os.Bundle;
import android.os.Handler;

import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.databinding.ActivityMainBinding;
import com.amanpatel.veggiestoretest0.databinding.NavHeaderMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.Area;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CEmail;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CName;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CPhone;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CityId;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.isLoggedIn;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public ActivityMainBinding binding;
    SharedPrefs sharedPrefs;
    public MarketViewModel mainactivityviewmodel;
    private Boolean exit = false;
    Toolbar toolbar;
    DrawerLayout drawer;
    Menu menuD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);


        sharedPrefs = new SharedPrefs(this);
        if (!sharedPrefs.findKey(this, Area)) {
            Intent i = new Intent(MainActivity.this, LocationActivity.class);
            startActivity(i);
        }

        super.onCreate(savedInstanceState);
        bind();
        if (sharedPrefs.findKey(this, Area)) {
            mainactivityviewmodel.setAreaName(sharedPrefs.getString(this, Area));
        }
        if (sharedPrefs.findKey(this, CName)) {
            mainactivityviewmodel.setCName(sharedPrefs.getString(this, CName));
        }
        if (sharedPrefs.findKey(this, isLoggedIn)) {
            Menu menu = binding.navView.getMenu();
            MenuItem navLogin = menu.findItem(R.id.nav_login);
            MenuItem navProfile = menu.findItem(R.id.nav_profile);
            MenuItem navLogout = menu.findItem(R.id.nav_logout);
            if (sharedPrefs.getString(this, isLoggedIn).equals("true")) {
                navLogin.setVisible(false);
                navLogout.setVisible(true);
                navProfile.setVisible(true);
            } else {
                navLogin.setVisible(true);
                navLogout.setVisible(false);
                navProfile.setVisible(false);
            }
        } else {
            Menu menu = binding.navView.getMenu();
            MenuItem navLogin = menu.findItem(R.id.nav_login);
            MenuItem navProfile = menu.findItem(R.id.nav_profile);
            MenuItem navLogout = menu.findItem(R.id.nav_logout);
            navLogin.setVisible(true);
            navLogout.setVisible(false);
            navProfile.setVisible(false);
        }

    }

    public void bind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final NavHeaderMainBinding bindingnav = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, binding.navView, false);
        binding.navView.addHeaderView(bindingnav.getRoot());
        binding.navView.setNavigationItemSelectedListener(this);
        toolbar = binding.appBarInclude.toolbar;
        drawer = binding.drawerLayout;
        setSupportActionBar(toolbar);
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        binding.setMainactivityviewmodel(mainactivityviewmodel);
        bindingnav.setMainactivityviewmodel(mainactivityviewmodel);
        binding.setLifecycleOwner(this);
        bindingnav.setLifecycleOwner(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (getIntent().getExtras() != null) {
            String activity = getIntent().getExtras().getString("from");
            if (activity != null) {
                switch (activity) {
                    case "Address":
                        displaySelectedScreen(R.id.nav_address);
                        break;
                    default:
                        displaySelectedScreen(R.id.nav_market);
                        break;
                }
            } else
                displaySelectedScreen(R.id.nav_market);
        } else
            displaySelectedScreen(R.id.nav_market);

        menuD = binding.navView.getMenu();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                finishAffinity();
            } else {
                Toast.makeText(this, "Press back again to exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_location) {
            Intent intent = new Intent(MainActivity.this, LocationActivity.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void displaySelectedScreen(int itemId) {
        Intent intent;
        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_market:
                fragment = new MarketFrag();
                break;
            case R.id.nav_address:
                fragment = new AddressFrag();
                break;
            case R.id.nav_orders:
                fragment = new OrdersFrag();
                break;
            case R.id.nav_wallet:
                fragment = new WalletFrag();
                break;
            case R.id.nav_notification:
                fragment = new NotificationFrag();
                break;
            case R.id.nav_franchise:
                fragment = new FranchiseFrag();
                break;
            case R.id.nav_clear_location:
                sharedPrefs.removeKey(this, Area);
                Toast.makeText(MainActivity.this, "Location successfully cleared.", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_login:
                intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_about_app:
                fragment = new AboutFrag();
                break;
            case R.id.nav_privacy_policy:
                fragment = new PrivacyPolicyFrag();
                break;
            case R.id.nav_share:
                final String appPackageNames = getPackageName();
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Checkout VeggieStore app on Play Store\nhttps://play.google.com/store/apps/");
//                share.putExtra(Intent.EXTRA_TEXT, "Checkout VeggieStore app on Play Store\nhttps://play.google.com/store/apps/details?id=" + appPackageNames);
                startActivity(Intent.createChooser(share, "Share App with Friends!"));
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        final AlertDialog.Builder logoutDialog = new AlertDialog.Builder(this);
        logoutDialog.setTitle(Html.fromHtml("<font color='#000000'>Log Out</font>", 0));
        logoutDialog.setMessage(Html.fromHtml("<font color='#000000'>Are you sure?</font>", 0));
        logoutDialog.setIcon(R.drawable.outline_exit_to_app_black_24);
        logoutDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        logoutDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mainactivityviewmodel.setCName("User");
                sharedPrefs.removeKey(MainActivity.this, isLoggedIn);
                sharedPrefs.removeKey(MainActivity.this, CName);
                sharedPrefs.removeKey(MainActivity.this, CID);
                sharedPrefs.removeKey(MainActivity.this, CPhone);
                sharedPrefs.removeKey(MainActivity.this, CEmail);
                sharedPrefs.removeKey(MainActivity.this, Area);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final AlertDialog dialog = logoutDialog.create();

        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void startEditAddressActivity(View v) {
        Intent intent = new Intent(MainActivity.this, EditAddressActivity.class);
        startActivity(intent);
    }

    public void startMarketFrag(View v) {
        displaySelectedScreen(R.id.nav_market);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPrefs.findKey(this, CName)) {
            mainactivityviewmodel.setCName(sharedPrefs.getString(this, CName));
        }
        if (sharedPrefs.findKey(this, Area)) {
            mainactivityviewmodel.setAreaName(sharedPrefs.getString(this, Area));
        }
        if (sharedPrefs.findKey(this, isLoggedIn)) {
            Menu menu = binding.navView.getMenu();
            MenuItem navLogin = menu.findItem(R.id.nav_login);
            MenuItem navProfile = menu.findItem(R.id.nav_profile);
            MenuItem navLogout = menu.findItem(R.id.nav_logout);
            if (sharedPrefs.getString(this, isLoggedIn).equals("true")) {
                navLogin.setVisible(false);
                navLogout.setVisible(true);
                navProfile.setVisible(true);
            } else {
                navLogin.setVisible(true);
                navLogout.setVisible(false);
                navProfile.setVisible(false);
            }
        } else {
            Menu menu = binding.navView.getMenu();
            MenuItem navLogin = menu.findItem(R.id.nav_login);
            MenuItem navProfile = menu.findItem(R.id.nav_profile);
            MenuItem navLogout = menu.findItem(R.id.nav_logout);
            navLogin.setVisible(true);
            navLogout.setVisible(false);
            navProfile.setVisible(false);
        }
    }
}

