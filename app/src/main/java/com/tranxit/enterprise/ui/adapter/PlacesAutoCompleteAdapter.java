package com.tranxit.enterprise.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.tranxit.enterprise.driver.R;

import java.util.ArrayList;
import java.util.List;

public class PlacesAutoCompleteAdapter
        extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PlacesViewHolder> {

    private Context mContext;
    private CharacterStyle styleBold;
    private CharacterStyle styleNormal;
    private List<AutocompletePrediction> predictions;

    public PlacesAutoCompleteAdapter(Context context) {
        mContext = context;
        styleBold = new StyleSpan(Typeface.BOLD);
        styleNormal = new StyleSpan(Typeface.NORMAL);
        predictions = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_location, viewGroup, false);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder placesViewHolder, int i) {
        placesViewHolder.area.setText(predictions.get(i).getPrimaryText(styleBold).toString());
        placesViewHolder.address.setText(predictions.get(i).getSecondaryText(styleNormal).toString());
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public List<AutocompletePrediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<AutocompletePrediction> predictions) {
        this.predictions = predictions;
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder {

        private TextView area;
        private TextView address;

        PlacesViewHolder(View itemView) {
            super(itemView);
            area = itemView.findViewById(R.id.area);
            address = itemView.findViewById(R.id.address);
        }
    }
}