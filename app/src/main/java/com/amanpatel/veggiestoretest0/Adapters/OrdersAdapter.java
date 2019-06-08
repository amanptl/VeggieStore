package com.amanpatel.veggiestoretest0.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Interface.CustomItemClickListener;
import com.amanpatel.veggiestoretest0.Interface.CustomOrderClickListener;
import com.amanpatel.veggiestoretest0.Models.OrderHistory;
import com.amanpatel.veggiestoretest0.Models.OrderMaster;
import com.amanpatel.veggiestoretest0.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<OrderHistory> mOrdersList = new ArrayList<>();
    private CustomOrderClickListener listener;
    private Context mContext;
    StringTokenizer dateToken;
    StringTokenizer timeToken;

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row, parent, false);
        final OrdersAdapter.ViewHolder mViewHolder = new OrdersAdapter.ViewHolder(mView);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, Integer.valueOf(mOrdersList.get(mViewHolder.getAdapterPosition()).getObjordermaster().getId()), mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        OrderMaster orderMaster = mOrdersList.get(position).getObjordermaster();
        dateToken = new StringTokenizer(orderMaster.getOrderdate(), "T");
        String date = dateToken.nextToken();
        timeToken = new StringTokenizer(dateToken.nextToken(), ".");
        String time = timeToken.nextToken();
        holder.orderDate.setText(String.format(mContext.getResources().getString(R.string.order_history_date_time), date, time));
        holder.orderNum.setText(String.format(mContext.getResources().getString(R.string.order_id), orderMaster.getOrdernumber()));
        holder.deliveryOn.setText(String.format(mContext.getResources().getString(R.string.delivery_on), orderMaster.getDeliveryOn()));
        holder.paymentMode.setText(String.format(mContext.getResources().getString(R.string.payment_mode), orderMaster.getPaymentmode()));
        holder.address.setText(String.format(mContext.getResources().getString(R.string.order_address), orderMaster.getAddress(), orderMaster.getDistrict(), orderMaster.getCity()));
        holder.amount.setText(String.format(mContext.getResources().getString(R.string.rupees), orderMaster.getGrandtotal()));
        if (orderMaster.getOrderStatus() != null) {
            switch (orderMaster.getOrderStatus()) {
                case "0":
                    holder.progress.setProgress(1);
                    holder.orderStatus.setText("Not Confirmed");
                    break;
                case "1":
                    holder.progress.setProgress(25);
                    holder.orderStatus.setText("Confirmed");
                    break;
                case "2":
                    holder.progress.setProgress(50);
                    holder.orderStatus.setText("Packed");
                    break;
                case "3":
                    holder.progress.setProgress(75);
                    holder.orderStatus.setText("Out For Delivery");
                    break;
                case "4":
                    holder.progress.setProgress(100);
                    holder.orderStatus.setText("Delivered");
                    break;
                case "5":
                    holder.progress.setProgress(100);
                    holder.orderStatus.setText("Return");
                    break;
                case "6":
                    holder.progress.setProgress(100);
                    holder.orderStatus.setText("Refunded");
                    break;
                default:
                    holder.progress.setProgress(1);
                    holder.orderStatus.setText("Not Confirmed");
                    break;

            }
        } else {
            holder.progress.setProgress(1);
            holder.orderStatus.setText("Not Confirmed");
        }


    }

    @Override
    public int getItemCount() {
        return mOrdersList.size();
    }

    public OrdersAdapter(Context mContext, List<OrderHistory> orders, CustomOrderClickListener listener) {
        this.mContext = mContext;
        this.mOrdersList = orders;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate;
        TextView orderStatus;
        TextView orderNum;
        TextView deliveryOn;
        TextView paymentMode;
        TextView address;
        TextView amount;
        ProgressBar progress;


        ViewHolder(View v) {
            super(v);
            orderDate = v.findViewById(R.id.order_date);
            deliveryOn = v.findViewById(R.id.delivery_on);
            orderNum = v.findViewById(R.id.order_number);
            orderStatus = v.findViewById(R.id.order_status);
            paymentMode = v.findViewById(R.id.payment_mode);
            address= v.findViewById(R.id.address_orders);
            amount = v.findViewById(R.id.amount);
            progress = v.findViewById(R.id.progress_bar);
        }
    }
}
