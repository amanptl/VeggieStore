package com.amanpatel.veggiestoretest0.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.DeliverySlots;
import com.amanpatel.veggiestoretest0.Models.Holiday;
import com.amanpatel.veggiestoretest0.Models.ValidationFlag;
import com.amanpatel.veggiestoretest0.Models.Voucher;
import com.amanpatel.veggiestoretest0.OrderViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.FragmentDeliveryDetailsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaMinAmt;
import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaShipping;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class DeliveryDetailsFrag extends Fragment {
    private FragmentDeliveryDetailsBinding binding;
    private OrderViewModel orderViewModel;
    SharedPrefs sharedPrefs;
    private Context c;
    private String customerId;
    private String areaName;
    private String areaId;
    private String dealerId;
    private boolean defaultAddr = false;
    private ArrayAdapter<String> aAdapter;
    private DateFormat dateFormat;
    private DateFormat dateTimeFormatter;
    private Date date;
    private Date yDate;
    String valFlag = "";
    private String currentTime;
    private Calendar calendar;
    private String cartTotal = "0";
    private String payable = "0";
    private String delivery = "0";
    private String d = "0";
    private String walletAmount = "0";
    private String walletRemaining = "0";
    private int payWallFlag = 1;
    private String timeSlot = "";
    private String paymentMode = "Cash On Delivery";
    private List<DeliverySlots> deliverySlotsList = new ArrayList<>();
    private SimpleDateFormat timeFormat;
    private List<AddressBook> addressBook = new ArrayList<>();
    private ArrayList<String> addressString = new ArrayList<>();
    private ArrayList<String> slotsString = new ArrayList<>();
    String s = "";
    private boolean customerStatus = false;
    private List<String> holidayList = new ArrayList<>();
    StringTokenizer dateToken;
    private boolean voucherApplied = false;
    private float fromWallet = 0;
    private ColorStateList colorStateList;
    private String orderDate = "";
    private AddressBook address = new AddressBook("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        super.onCreate(savedInstanceState);

        sharedPrefs = new SharedPrefs(getContext());
        customerId = sharedPrefs.getString(getContext(), SharedPrefs.CID);
        areaName = sharedPrefs.getString(getContext(), SharedPrefs.Area);
        areaId = sharedPrefs.getString(getContext(), SharedPrefs.AreaID);
        dealerId = sharedPrefs.getString(getContext(), SharedPrefs.DealerID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_details, container, false);
        final View v = binding.getRoot();
        c = v.getContext();
        binding.setOrderViewModel(orderViewModel);

        orderViewModel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(v, R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                loadData();
                                Intent intent = new Intent(c, CartActivity.class);
                            }
                        }).setActionTextColor(ContextCompat.getColor(v.getContext(), R.color.snackbarActionColor)).show();
            }
        });

        orderViewModel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        orderViewModel.setUidDid(customerId, dealerId);

        orderViewModel.getVerification(customerId).observe(DeliveryDetailsFrag.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("true")) {
                    customerStatus = true;
                } else
                    customerStatus = false;
            }
        });

        orderViewModel.getWalletAmount(customerId).observe(DeliveryDetailsFrag.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.equals("0")) {
                    walletAmount = s;
                    walletRemaining = s;
                    binding.wallet.setText(String.format(getResources().getString(R.string.rupees), walletAmount));
                    binding.walletRemaining.setText(String.format(getResources().getString(R.string.rupees), walletAmount));
                }
            }
        });


        dateFormat = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.getDefault());
        dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault());
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("hh:mmaa", java.util.Locale.getDefault());
        date = calendar.getTime();
        currentTime = timeFormat.format(calendar.getTime());
        binding.date.setText("Select the day");
        binding.delivery.setText(String.format(getResources().getString(R.string.rupees), delivery));
        binding.discount.setText(String.format(getResources().getString(R.string.rupees), d));
        binding.wallet.setText(String.format(getResources().getString(R.string.rupees), walletAmount));
        binding.walletRemaining.setText(String.format(getResources().getString(R.string.rupees), "0"));
        final AlertDialog.Builder addressDialog = new AlertDialog.Builder(c);
        getDeliverySlots();
        getHolidays();

        colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{

                        R.color.black //disabled
                        , R.color.primaryColor //enabled

                }
        );
        aAdapter = new ArrayAdapter<>(c,
                android.R.layout.simple_spinner_dropdown_item,
                addressString);
        addressDialog.setAdapter(aAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                address = addressBook.get(which);
                setAddress();
            }
        });

        orderViewModel.getAddresses(customerId, areaName).observe(this, new Observer<List<AddressBook>>() {
            @Override
            public void onChanged(List<AddressBook> addressBooks) {

                for (int i = 0; i < addressBooks.size(); i++) {
                    addressBook.addAll(addressBooks);
                    String Addr = String.format(getResources().getString(R.string.address_dialog), addressBooks.get(i).getAddress(), addressBooks.get(i).getDistrict(), addressBooks.get(i).getCity());
                    addressString.add(Addr);
                    if (addressBooks.get(i).getMobiledefault().equals("true")) {
                        defaultAddr = true;
                        address = addressBooks.get(i);
                        setAddress();
                    }
                }
                if (!defaultAddr) {
                    address = addressBooks.get(0);
                    setAddress();
                }
                aAdapter.notifyDataSetChanged();
            }
        });

        binding.addressChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressDialog.show();
            }
        });

        orderViewModel.getValFlag().observe(this, new Observer<ValidationFlag>() {
            @Override
            public void onChanged(ValidationFlag validationFlag) {
                if (validationFlag != null) {
                    valFlag = validationFlag.getFlag();
                    switch (valFlag) {
                        case "nameInvalid":
                            binding.name.setError("Invalid Name");
                            break;
                    }
                    valFlag = "";
                }
            }
        });

        binding.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.today) {
                    boolean isHoliday = false;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    binding.radioGroup2.clearCheck();
                    ((RadioButton) binding.radioGroup2.getChildAt(0)).setEnabled(false);
                    calendar = Calendar.getInstance();
                    date = calendar.getTime();
                    for (int z = 0; z < holidayList.size(); z++) {
                        try {
                            Date holidayDate = simpleDateFormat.parse(holidayList.get(z));
                            Calendar hCal = Calendar.getInstance();
                            Calendar cCal = Calendar.getInstance();
                            hCal.setTime(holidayDate);
                            cCal.setTime(date);
                            cCal.set(Calendar.HOUR_OF_DAY, 0);
                            cCal.set(Calendar.MINUTE, 0);
                            cCal.set(Calendar.SECOND, 0);
                            cCal.set(Calendar.MILLISECOND, 0);

                            if (hCal.equals(cCal)) {
                                isHoliday = true;
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isHoliday) {
                        binding.date.setText("The dealer is on holiday.");
                        binding.radioGroup2.clearCheck();
                        for (int y = 0; y < binding.radioGroup2.getChildCount(); y++) {
                            ((RadioButton) binding.radioGroup2.getChildAt(y)).setEnabled(false);
                        }
                    } else {
                        binding.date.setText(dateFormat.format(date));
                        orderDate = dateTimeFormatter.format(date);
                        try {
                            Date mToday = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String curTime = sdf.format(mToday);
                            Date userDate = sdf.parse(curTime);
                            for (int y = 1; y < deliverySlotsList.size(); y++) {
                                if (deliverySlotsList.get(y).getActive().equals("false")) {
                                    String slotTime = deliverySlotsList.get(y).getTimeslot();
                                    Date slotDate = sdf.parse(slotTime);
                                    if (userDate.before(slotDate)) {
                                        binding.radioGroup2.getChildAt(y).setEnabled(true);
                                    }
                                } else {
                                    String slotTime = deliverySlotsList.get(y).getTimeslot();
                                    Date slotDate = sdf.parse(slotTime);
                                    if (userDate.after(slotDate)) {
                                        binding.radioGroup2.getChildAt(y).setEnabled(true);
                                    }
                                }
                            }

                        } catch (ParseException e) {
                        }
                    }

                } else if (i == R.id.tomorrow) {
                    boolean isHoliday = false;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    binding.radioGroup2.clearCheck();
                    calendar = Calendar.getInstance();
                    yDate = calendar.getTime();
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    date = calendar.getTime();
                    for (int z = 0; z < holidayList.size(); z++) {
                        try {
                            Date holidayDate = simpleDateFormat.parse(holidayList.get(z));
                            Calendar hCal = Calendar.getInstance();
                            Calendar cCal = Calendar.getInstance();
                            hCal.setTime(holidayDate);
                            cCal.setTime(date);
                            cCal.set(Calendar.HOUR_OF_DAY, 0);
                            cCal.set(Calendar.MINUTE, 0);
                            cCal.set(Calendar.SECOND, 0);
                            cCal.set(Calendar.MILLISECOND, 0);

                            if (hCal.equals(cCal)) {
                                isHoliday = true;
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isHoliday) {
                        binding.date.setText("The dealer is on holiday.");
                        binding.radioGroup2.clearCheck();
                        for (int y = 0; y < binding.radioGroup2.getChildCount(); y++) {
                            ((RadioButton) binding.radioGroup2.getChildAt(y)).setEnabled(false);
                        }
                    } else {
                        orderDate = dateTimeFormatter.format(yDate);
                        binding.date.setText(dateFormat.format(date));
                        for (int y = 0; y < binding.radioGroup2.getChildCount(); y++) {
                            ((RadioButton) binding.radioGroup2.getChildAt(y)).setEnabled(true);
                        }
                    }
                }
            }
        });


        binding.radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = binding.radioGroup2.findViewById(i);
                if (checkedRadioButton != null)
                    timeSlot = checkedRadioButton.getText().toString();
            }
        });

        orderViewModel.getCartTotal(customerId, areaId).observe(DeliveryDetailsFrag.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                cartTotal = s;
                payable = cartTotal;
                binding.total.setText(String.format(getResources().getString(R.string.rupees), cartTotal));
                binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                if (Float.valueOf(cartTotal) < Float.valueOf(sharedPrefs.getString(c, AreaMinAmt))) {
                    delivery = sharedPrefs.getString(c, AreaShipping);
                    Float deliveryChrg = Float.valueOf(delivery);
                    Float cart = Float.valueOf(cartTotal);
                    payable = String.valueOf(cart + deliveryChrg);
                    binding.delivery.setText(String.format(getResources().getString(R.string.rupees), delivery));
                    binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                }

            }
        });

        binding.voucherApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!voucherApplied) {
                    if (binding.voucherInput.getText() != null && !binding.voucherInput.getText().toString().equals("")) {
                        orderViewModel.getVoucher(sharedPrefs.getString(c, AreaID), binding.voucherInput.getText().toString()).observe(DeliveryDetailsFrag.this, new Observer<Voucher>() {
                            @Override
                            public void onChanged(Voucher voucher) {
                                if (voucher != null && !voucherApplied) {
                                    try {
                                        Date mToday = new Date();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        String curDate = sdf.format(mToday);
                                        Date userDate = sdf.parse(curDate);
                                        Date voucherDate = sdf.parse(voucher.getExpiryDate());
                                        if (userDate.before(voucherDate)) {
                                            if (Float.valueOf(voucher.getMinimumAmount()) < Float.valueOf(cartTotal)) {
                                                Float cartT = Float.valueOf(cartTotal);
                                                Float discount = Float.valueOf(voucher.getVoucherAmount());
                                                if (voucher.getType().equals("0")) { //Percentage
                                                    Float newT = cartT - (cartT * discount) / 100;
                                                    Float payableF = Float.valueOf(payable);
                                                    d = String.valueOf(cartT - newT);
                                                    payable = String.valueOf(payableF - (cartT - newT));
                                                    binding.discount.setText(String.format(getResources().getString(R.string.rupees), d));
                                                    binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                                                    orderViewModel.setDiscountInfo(voucher.getType(), voucher.getAdminNote());
                                                    binding.voucherApply.setText("Remove");
                                                    voucherApplied = true;
                                                } else {
                                                    Float newT = cartT - discount;
                                                    Float payableF = Float.valueOf(payable);
                                                    d = String.valueOf(cartT - newT);
                                                    payable = String.valueOf(payableF - (cartT - newT));
                                                    binding.discount.setText(String.format(getResources().getString(R.string.rupees), d));
                                                    binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                                                    orderViewModel.setDiscountInfo(voucher.getType(), voucher.getAdminNote());
                                                    binding.voucherApply.setText("Remove");
                                                    voucherApplied = true;
                                                }

                                            } else {
                                                toastMaker("Minimum amount should be Rs." + voucher.getMinimumAmount());
                                            }
                                        } else {
                                            toastMaker("Code expired");
                                        }
                                    } catch (ParseException e) {
                                    }
                                }
                            }
                        });
                    } else
                        toastMaker("Enter a code");
                } else {
                    binding.voucherInput.setText("");
                    d = String.valueOf(0);
                    payable = cartTotal;
                    binding.discount.setText(String.format(getResources().getString(R.string.rupees), d));
                    binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                    orderViewModel.setDiscountInfo("", "");
                    voucherApplied = false;
                    binding.voucherApply.setText("Apply");
                }
            }

        });

        binding.includeWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    fromWallet = 1;
                    Float payableF = Float.valueOf(payable);
                    Float wallet = Float.valueOf(walletAmount);
                    if (payableF >= wallet) {
                        payWallFlag = 0;
                        payableF = payableF - wallet;
                        wallet = 0f;
                        walletRemaining = String.valueOf(wallet);
                        payable = String.valueOf(payableF);
                        fromWallet = Float.valueOf(walletAmount);
                        binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                        binding.walletRemaining.setText(String.format(getResources().getString(R.string.rupees), String.valueOf(wallet)));
                    } else {
                        wallet = wallet - payableF;
                        payableF = 0f;
                        walletRemaining = String.valueOf(wallet);
                        fromWallet = Float.valueOf(payable);
                        payable = String.valueOf(payableF);
                        binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                        binding.walletRemaining.setText(String.format(getResources().getString(R.string.rupees), String.valueOf(wallet)));
                    }
                } else {
                    fromWallet = 0;
                    if (payWallFlag == 1) {
                        binding.walletRemaining.setText(String.format(getResources().getString(R.string.rupees), walletAmount));
                        Float walletA = Float.valueOf(walletAmount);
                        Float walletR = Float.valueOf(walletRemaining);
                        payable = String.valueOf(walletA - walletR);
                        binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                    } else {
                        Float walletA = Float.valueOf(walletAmount);
                        Float payableF = Float.valueOf(payable);
                        payable = String.valueOf(walletA + payableF);
                        walletRemaining = walletAmount;
                        binding.payable.setText(String.format(getResources().getString(R.string.rupees), payable));
                        binding.walletRemaining.setText(String.format(getResources().getString(R.string.rupees), walletAmount));
                    }
                }

            }
        });

        binding.placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customerStatus) {
                    orderViewModel.setAmounts(cartTotal, d, delivery, String.valueOf(fromWallet), payable);
                    orderViewModel.setDeliveryDetails(binding.date.getText().toString() + " " + timeSlot, paymentMode, orderDate);
                    if (binding.radioGroup2.getCheckedRadioButtonId() == -1) {
                        toastMaker("Please select time slot");
                    } else
                        orderViewModel.validate(areaId).observe(DeliveryDetailsFrag.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == 201) {
                                    Intent intent = new Intent(c, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                } else {
                    toastMaker("Your account is suspended. Contact admin.");
                    Intent intent = new Intent(c, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


        return v;
    }

    private void getHolidays() {
        orderViewModel.getHolidays(sharedPrefs.getString(getContext(), AreaID)).observe(this, new Observer<List<Holiday>>() {
            @Override
            public void onChanged(List<Holiday> holidays) {
                for (int i = 0; i < holidays.size(); i++) {
                    dateToken = new StringTokenizer(holidays.get(i).getHolidaydate(), "T");
                    holidayList.add(dateToken.nextToken());
                }
            }
        });
    }

    private void getDeliverySlots() {

        orderViewModel.getDeliverySlots(sharedPrefs.getString(getContext(), AreaID)).observe(this, new Observer<List<DeliverySlots>>() {
            @Override
            public void onChanged(List<DeliverySlots> deliverySlots) {
                deliverySlotsList.clear();
                for (int i = 0; i < deliverySlots.size(); i++) {
                    int s = deliverySlotsList.size();
                    deliverySlotsList.add(deliverySlots.get(i));
                    DeliverySlots slot = deliverySlots.get(i);
                    RadioButton rb = new RadioButton(getContext());
                    rb.setText(slot.getTitle());
                    rb.setEnabled(false);
                    rb.setButtonTintList(colorStateList);
                    binding.radioGroup2.addView(rb);
                }
            }
        });
    }

    private void setAddress() {
        binding.name.setText(address.getName());
        orderViewModel.setName(address.getName());
        binding.email.setText(address.getEmail());
        orderViewModel.setEmail(address.getEmail());
        binding.address.setText(address.getAddress());
        binding.areaCityPin.setText(String.format(getResources().getString(R.string.area_city_pin), address.getDistrict(), address.getCity(), address.getPostCode()));
        binding.landmark.setText(address.getLandmark());
        binding.phone.setText(address.getPhone());
        orderViewModel.setAddress(address);
    }

    private void toastMaker(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
