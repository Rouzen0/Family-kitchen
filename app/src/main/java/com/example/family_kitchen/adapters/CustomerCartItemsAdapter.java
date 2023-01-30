package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.interfaces.SetTotalOrderPrice;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.preference.SharedPref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerCartItemsAdapter extends RecyclerView.Adapter<CustomerCartItemsAdapter.ViewHolder> {

    ArrayList<Cart> list;
    private Context context;
    Cart cart;
    private DatabaseReference db;
    int totalOrderItemsPrice;
    public CustomerCartItemsAdapter(Context ctx, ArrayList <Cart> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_menu_cart,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        cart =list.get(position);

        holder.tv_item_name.setText(cart.getItemName());
        holder.tv_item_price.setText(cart.getTotalPrice());
        holder.tv_item_quantity.setText(cart.getItemQuantity());


        // itemQuantity,totalPrice;
        holder.iv_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart =list.get(position);
                db= FirebaseDatabase.getInstance().getReference("Order");
                Map<String, Object> updateValues = new HashMap<String, Object>();

                int totalItemQuantity= Integer.parseInt(cart.getItemQuantity())+1;
                int totalItemPrice=Integer.parseInt(cart.getItemPrice())*totalItemQuantity;
                updateValues.put("itemQuantity", String.valueOf(totalItemQuantity));
                updateValues.put("totalPrice", String.valueOf(totalItemPrice));
                db.child(cart.getOrderItemId()).updateChildren(updateValues);

            }
        });


        holder.iv_minus_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart =list.get(position);
                db= FirebaseDatabase.getInstance().getReference("Order");
                Map<String, Object> updateValues = new HashMap<String, Object>();
                int totalItemQuantity= Integer.parseInt(cart.getItemQuantity())-1;
                int totalItemPrice=Integer.parseInt(cart.getTotalPrice())-Integer.parseInt(cart.getItemPrice());
                if(totalItemQuantity>=1) {
                    updateValues.put("itemQuantity", String.valueOf(totalItemQuantity));
                    updateValues.put("totalPrice", String.valueOf(totalItemPrice));
                    updateValues.put("totalOrderPrice", String.valueOf(totalOrderItemsPrice));
                    db.child(cart.getOrderItemId()).updateChildren(updateValues);
                }

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

        public TextView tv_item_quantity;

        public ImageView iv_add_item;

        public ImageView iv_minus_item;


        public ViewHolder(@NonNull View v) {
            super(v);

            tv_item_name = (TextView) v.findViewById(R.id.textView_item_name);
            tv_item_price = (TextView) v.findViewById(R.id.textView_item_price);
            tv_item_quantity = (TextView) v.findViewById(R.id.textView_item_quantity);
            iv_add_item = (ImageView) v.findViewById(R.id.imageView_add);
            iv_minus_item = (ImageView) v.findViewById(R.id.imageView_minus);
        }
    }
}
