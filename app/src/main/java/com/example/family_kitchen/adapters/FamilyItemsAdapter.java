package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.ui.family.activities.FamilyUpdateItemActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FamilyItemsAdapter extends RecyclerView.Adapter<FamilyItemsAdapter.ViewHolder> {

    ArrayList<Item> list;
    private Context context;
    Item item;
    private DatabaseReference db;

    public FamilyItemsAdapter(Context ctx, ArrayList <Item> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_my_shop,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);

        holder.tv_item_name.setText(item.getStoreName());
        holder.tv_item_price.setText(item.getItemName());
        if(!item.getItemImageUrl().equals("")) {
            Picasso.get().load(item.getItemImageUrl()).placeholder(R.drawable.image_placeholder).into(holder.iv_item);
        } else{
            holder.iv_item.setImageDrawable(context.getResources().getDrawable(R.drawable.image_place_holder));
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


        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item =list.get(position);
                        db= FirebaseDatabase.getInstance().getReference("Items");
                        db.child(item.getItemId()).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();

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
        public Button btn_remove;

        public ViewHolder(@NonNull View v) {
            super(v);

            tv_item_name = (TextView) v.findViewById(R.id.textView_item_name);
            tv_item_price = (TextView) v.findViewById(R.id.textView_item_price);
            iv_item = (ImageView) v.findViewById(R.id.imageView_item);
            btn_remove = (Button) v.findViewById(R.id.button_remove);
        }
    }
}
