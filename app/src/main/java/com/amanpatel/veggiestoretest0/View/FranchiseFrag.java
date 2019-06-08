package com.amanpatel.veggiestoretest0.View;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.FragmentFranchiseBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.City;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.CityId;

public class FranchiseFrag extends Fragment {
    public FragmentFranchiseBinding binding;
    private MarketViewModel mainactivityviewmodel;
    private Context c;
    View v;
    private String valFlag = "";
    AlertDialog.Builder countryDialog;
    ArrayAdapter<String> coDataAdapter;
    ArrayList<String> countries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainactivityviewmodel = ViewModelProviders.of(this).get(MarketViewModel.class);
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            getActivity().setTitle("Franchise");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_franchise, container, false);
        v = binding.getRoot();
        c = v.getContext();
        binding.setMainactivityviewmodel(mainactivityviewmodel);

        mainactivityviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(v.getRootView(), R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE);
            }
        });

        mainactivityviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainactivityviewmodel.onSubmitClick().observe(FranchiseFrag.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if(integer == 200){
                            binding.submitBtn.setEnabled(false);
                        }
                    }
                });
            }
        });

        mainactivityviewmodel.getValFlag().observe(this, new Observer<ValidationFlag>() {
            @Override
            public void onChanged(ValidationFlag validationFlag) {
                if (validationFlag != null) {
                    valFlag = validationFlag.getFlag();
                    switch (valFlag) {
                        case "ffNameInvalid":
                            binding.fnameInput.setError("Invalid First Name");
                            break;
                        case "flNameInvalid":
                            binding.lnameInput.setError("Invalid Last Name");
                            break;
                        case "fphoneInvalid":
                            binding.phoneInput.setError("Invalid Phone Number");
                            break;
                        case "femailInvalid":
                            binding.emailInput.setError("Invalid Email");
                            break;
                    }
                    valFlag = "";
                }
            }
        });



        countryDialog = new AlertDialog.Builder(getContext());

        coDataAdapter = new ArrayAdapter<> (getContext(),
                android.R.layout.simple_spinner_dropdown_item, countries);

        countryDialog.setAdapter(coDataAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainactivityviewmodel.setFCountry(coDataAdapter.getItem(which));
                binding.countryInput.setText(coDataAdapter.getItem(which));
            }
        });
        countryDialog.setTitle(Html.fromHtml("<font color='#000000'><b>Country</b></font>", 0));
        countryDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        binding.countryInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryDialog.show();
            }
        });
        getCountries();


        return v;

    }

    private void getCountries(){
        String[] isoCountries = Locale.getISOCountries();
        for (String country : isoCountries) {
            Locale locale = new Locale("en", country);
            String name = locale.getDisplayCountry();

            if (!"".equals(name)) {
                countries.add(name);
            }
        }

        // Sort the country by their name and then display the content
        // of countries collection object.
        Collections.sort(countries);

//
//        Locale[] locales = Locale.getAvailableLocales();
//        for (Locale locale : locales) {
//            String country = locale.getDisplayCountry();
//            countries.add(country);
//
//        }
//        Collections.sort(countries);
        coDataAdapter.notifyDataSetChanged();
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
