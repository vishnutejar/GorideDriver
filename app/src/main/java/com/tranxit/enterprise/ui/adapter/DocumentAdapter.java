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
import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.data.network.model.Document;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {

    private List<Document> list;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView itemView;
        private ImageView image;
        private TextView name;

        MyViewHolder(View view) {
            super(view);
            itemView = view.findViewById(R.id.item_view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
        }
    }


    public DocumentAdapter(Context context, List<Document> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DocumentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_document, parent, false);

        return new DocumentAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.MyViewHolder holder, int position) {
        Document obj = list.get(position);
        holder.name.setText(obj.getName());

        if (obj.getDocument() != null) {
            Glide.with(context).load(obj.getDocument()).apply(RequestOptions.placeholderOf(R.drawable.ic_photo_camera).dontAnimate().error(R.drawable.ic_photo_camera)).into(holder.image);
        } else {
            Glide.with(context).load(BuildConfig.BASE_IMAGE_URL + obj.getUrl()).apply(RequestOptions.placeholderOf(R.drawable.ic_photo_camera).dontAnimate().error(R.drawable.ic_photo_camera)).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Document> getList() {
        return list;
    }

    public void setItem(Document item, Integer position) {
        list.set(position, item);
        notifyItemChanged(position);
    }
}

