package com.tranxit.enterprise.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tranxit.enterprise.common.SharedHelper;
import com.goride.provider.R;
import com.tranxit.enterprise.common.Constants;
import com.tranxit.enterprise.data.network.model.HistoryList;

import java.util.List;

/**
 * Created by suthakar@appoets.com on 28-06-2018.
 */
public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.MyViewHolder> {

    private List<HistoryList> list;
    private Context context;

    private ClickListener clickListener;

    public PastTripAdapter(List<HistoryList> list, Context con) {
        this.list = list;
        this.context = con;
    }

    public void setList(List<HistoryList> list) {
        this.list = list;
    }

    public void setClickListener(PastTripAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void redirectClick(HistoryList historyList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_past_trip, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryList historyList = list.get(position);

        holder.finishedAt.setText(historyList.getFinishedAt());
        holder.bookingId.setText(historyList.getBookingId());
        if (historyList.getPayment() != null) {
            holder.payable.setText(SharedHelper.getKey(context, SharedHelper.CURRENCY)
                    + " " + historyList.getPayment().getPayable());
        } else {
            holder.payable.setText("-");

        }
        if(historyList.getServicetype()!=null)
        holder.lblServiceName.setText(historyList.getServicetype().getName());
        Glide.with(context).load(historyList.getStaticMap()).apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).dontAnimate().error(R.drawable.ic_launcher_background)).into(holder.staticMap);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView itemView;
        private TextView bookingId, payable, finishedAt, lblServiceName;
        private ImageView staticMap;


        private MyViewHolder(View view) {
            super(view);

            itemView = view.findViewById(R.id.item_view);
            bookingId = view.findViewById(R.id.booking_id);
            payable = view.findViewById(R.id.payable);
            lblServiceName = view.findViewById(R.id.lblServiceName);
            finishedAt = view.findViewById(R.id.finished_at);
            staticMap = view.findViewById(R.id.static_map);

            itemView.setOnClickListener(v -> {

                if (clickListener != null) {
                    clickListener.redirectClick(list.get(getAdapterPosition()));
                }
            });
        }
    }
}