package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.ui.family.activities.FamilyUpdateItemActivity;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerItemsAdapter extends RecyclerView.Adapter<CustomerItemsAdapter.ViewHolder> {

    ArrayList<Item> list;
    private Context context;
    Item item;
    private DatabaseReference db;

    public CustomerItemsAdapter(Context ctx, ArrayList <Item> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_customer_home,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);

        holder.tv_item_name.setText(item.getStoreName());
        holder.tv_item_price.setText(item.getItemName());
        if(!item.getItemImageUrl().equals("")) {
            Picasso.get().load(item.getItemImageUrl()).placeholder(R.drawable.image_place_holder_large).into(holder.iv_item);
        } else{
            holder.iv_item.setImageDrawable(context.getResources().getDrawable(R.drawable.image_place_holder_large));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item =list.get(position);
                Intent intent=new Intent(context, FamilyUpdateItemActivity.class);
                intent.putExtra("item_id",item.getItemId());
                intent.putExtra("item_name",item.getItemName());
                intent.putExtra("item_price",item.getItemPrice());
                intent.putExtra("item_url",item.getItemImageUrl());
                intent.putExtra("user_id",item.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_name;
        public TextView tv_item_price;
        public ImageView iv_item;

        public ViewHolder(@NonNull View v) {
            super(v);

            tv_item_name = (TextView) v.findViewById(R.id.textView_item_name);
            tv_item_price = (TextView) v.findViewById(R.id.textView_item_price);
            iv_item = (ImageView) v.findViewById(R.id.imageView_item);
        }
    }
}
