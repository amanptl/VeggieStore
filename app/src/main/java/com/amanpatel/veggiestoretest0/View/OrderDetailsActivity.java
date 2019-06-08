package com.amanpatel.veggiestoretest0.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amanpatel.veggiestoretest0.Adapters.OrderDetailsAdapter;
import com.amanpatel.veggiestoretest0.Adapters.ReviewOrderAdapter;
import com.amanpatel.veggiestoretest0.LocationViewModel;
import com.amanpatel.veggiestoretest0.MarketViewModel;
import com.amanpatel.veggiestoretest0.Models.Holiday;
import com.amanpatel.veggiestoretest0.Models.OrderHistory;
import com.amanpatel.veggiestoretest0.Models.OrderItems;
import com.amanpatel.veggiestoretest0.OrderViewModel;
import com.amanpatel.veggiestoretest0.R;
import com.amanpatel.veggiestoretest0.Utils.SharedPrefs;
import com.amanpatel.veggiestoretest0.databinding.ActivityMainBinding;
import com.amanpatel.veggiestoretest0.databinding.ActivityOrderDetailsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import static com.amanpatel.veggiestoretest0.Utils.SharedPrefs.AreaID;

public class OrderDetailsActivity extends AppCompatActivity {
    public ActivityOrderDetailsBinding binding;
    private OrderViewModel orderviewmodel;
    private SharedPrefs sharedPrefs;
    private String orderID = "";
    private String orderNum = "";
    private OrderDetailsAdapter mAdapter;
    private OrderHistory orderDetails;
    private String rating = "";
    private List<OrderItems> orderItems = new ArrayList<>();
    StringTokenizer dateToken;
    StringTokenizer timeToken;
    StringTokenizer dDateToken;
    String date1 = "";
    private Date deliveredTime;
    private MutableLiveData<String> isReviewed = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        orderviewmodel = ViewModelProviders.of(this).get(OrderViewModel.class);
        binding.setOrderviewmodel(orderviewmodel);
        binding.setLifecycleOwner(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            orderID = getIntent().getExtras().getString("id");
            orderNum = getIntent().getExtras().getString("orderNum");
            setTitle("");
            getSupportActionBar().setElevation(0f);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this);
        binding.recyclerViewOrderDetails.setLayoutManager(mLayoutManager);
        binding.recyclerViewOrderDetails.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewOrderDetails.setNestedScrollingEnabled(false);
        mAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderItems);
        binding.recyclerViewOrderDetails.setAdapter(mAdapter);

        orderviewmodel.getOperationToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastMaker(s);
            }
        });

        orderviewmodel.getInternetToast().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(binding.getRoot(), R.string.internet_snackbar, Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData();
                            }
                        }).setActionTextColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.snackbarActionColor)).show();
            }
        });

        loadData();

        binding.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = binding.radioGroup1.findViewById(i);
                if (checkedRadioButton != null)
                    rating = checkedRadioButton.getText().toString();
            }
        });

        binding.reviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating.equals("")) {
                    toastMaker("Rate the order first.");
                } else {
                    final String review = Objects.requireNonNull(binding.review.getText()).toString();
                    orderviewmodel.setReview(orderID, rating, review).observe(OrderDetailsActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            if (integer == 200) {
                                binding.buttonLayout.setVisibility(View.GONE);
                                binding.layout5.setVisibility(View.GONE);
                                binding.reviewMessage.setText(String.format(getResources().getString(R.string.feedback), rating));
                                binding.reviewMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderviewmodel.returnOrder(orderID).observe(OrderDetailsActivity.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if(integer == 200){
                            binding.returnBtn.setClickable(false);
                            binding.returnBtn.setText("Return Requested.");
                        }
                    }
                });
            }
        });
    }

    private void loadData() {
        orderviewmodel.getOrderItems(orderID).observe(this, new Observer<OrderHistory>() {
            @Override
            public void onChanged(OrderHistory orderHistory) {
                if (orderHistory != null) {
                    orderItems.clear();

                    binding.orderNumber.setText(String.format(getResources().getString(R.string.od_order_number), orderHistory.getObjordermaster().getOrdernumber()));
                    dateToken = new StringTokenizer(orderHistory.getObjordermaster().getOrderdate(), "T");

                    dDateToken = new StringTokenizer(orderHistory.getObjordermaster().getModifyDate(), "T");
                    date1 = dDateToken.nextToken();
                    timeToken = new StringTokenizer(dDateToken.nextToken(), ".");

                    binding.orderDate.setText(String.format(getResources().getString(R.string.od_order_date), dateToken.nextToken()));
                    binding.paymentMode.setText(orderHistory.getObjordermaster().getPaymentmode());
                    binding.orderStatus.setText(getStatusText(orderHistory.getObjordermaster().getOrderStatus()));
                    binding.totalAmount.setText(String.format(getResources().getString(R.string.rupees), orderHistory.getObjordermaster().getGrandtotal()));
                    binding.subtotal.setText(String.format(getResources().getString(R.string.rupees), orderHistory.getObjordermaster().getSubtotal()));
                    binding.discount.setText(String.format(getResources().getString(R.string.rupees_minus), orderHistory.getObjordermaster().getDiscount()));
                    binding.delivery.setText(String.format(getResources().getString(R.string.rupees), orderHistory.getObjordermaster().getDelivery()));
                    orderItems.addAll(orderHistory.getObjorderitem());
                    mAdapter.notifyDataSetChanged();

                    if (orderHistory.getObjordermaster().getOrderStatus() != null) {
                        if (Integer.valueOf(orderHistory.getObjordermaster().getOrderStatus()) > 3) {
                            binding.reviewText.setVisibility(View.VISIBLE);
                            if (orderHistory.getObjordermaster().getIsrating().equals("0")) {
                                binding.buttonLayout.setVisibility(View.VISIBLE);
                                binding.layout5.setVisibility(View.VISIBLE);
                            } else {
                                binding.reviewMessage.setText(String.format(getResources().getString(R.string.feedback), orderHistory.getObjordermaster().getRating()));
                                binding.reviewMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });

    }


    private void toastMaker(String message) {
        Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String getStatusText(String status) {

        if (status != null) {
            switch (status) {
                case "0":
                    return "Not Confirmed";
                case "1":
                    return "Confirmed";
                case "2":
                    return "Packed";
                case "3":
                    return "Out For Delivery";
                case "4":
                    String date2 = date1;
                    String time = timeToken.nextToken();
                    SimpleDateFormat refucnDTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date dt = refucnDTF.parse(date2+" "+time);
                        Calendar eDate = Calendar.getInstance();
                        eDate.setTime(dt);
                        eDate.add(Calendar.HOUR_OF_DAY, 2);

                        Calendar cDate = Calendar.getInstance();
                        String  c = cDate.getTime().toString();
                        String e = eDate.getTime().toString();
                        if(cDate.getTime().before(eDate.getTime())){
                            binding.returnBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            binding.returnBtn.setVisibility(View.INVISIBLE);
                        }
                    } catch (ParseException e) {
                    }


                    return "Delivered";
                case "5":
                    return "Return";
                case "6":
                    return "Refunded";
                default:
                    return "Not Confirmed";
            }
        } else
            return "Not Confirmed";
    }
}
