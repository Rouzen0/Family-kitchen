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

public class FamilyOrderDetailAdapter extends RecyclerView.Adapter<FamilyOrderDetailAdapter.ViewHolder> {

    ArrayList<Cart> list;
    private Context context;
    Cart cart;
    private DatabaseReference db;
    public FamilyOrderDetailAdapter(Context ctx, ArrayList <Cart> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_detail_item_family,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        cart =list.get(position);

        holder.tv_item_name.setText(cart.getItemName()+"  x"+cart.getItemQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_name;

        public ViewHolder(@NonNull View v) {
            super(v);
            tv_item_name = (TextView) v.findViewById(R.id.textView_item_name);
        }
    }
}
