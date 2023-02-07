package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.delivery.activities.DeliveryChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeliveryMessageAdapter extends RecyclerView.Adapter<DeliveryMessageAdapter.ViewHolder> {

    ArrayList<Cart> list;
    private Context context;
    Cart item;
    private DatabaseReference db;

    private SharedPref pref;

    String name;

    public DeliveryMessageAdapter(Context ctx, ArrayList <Cart> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_chat_family,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);


        holder.tv_order_total_price.setText(item.getTotalOrderPrice()+" SR");
        holder.tv_order_date.setText("Delivery: "+item.getOrderDate());

        db= FirebaseDatabase.getInstance().getReference("User");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item =list.get(position);
                name=dataSnapshot.child(item.getUserId()).child("name").getValue(String.class);
                holder.tv_store_name.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        holder.btn_rate_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item =list.get(position);
                Intent intent=new Intent(context, DeliveryChatActivity.class);
                intent.putExtra("order_id",item.getOrderId());
                intent.putExtra("family_id",item.getFamilyId());
                intent.putExtra("user_id",item.getUserId());
                intent.putExtra("driver_id",item.getDriverId());

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public TextView tv_store_name;


        public TextView tv_order_date;
        public TextView tv_order_total_price;

        public Button btn_rate_order;


        public ViewHolder(@NonNull View v) {
            super(v);

            tv_store_name = (TextView) v.findViewById(R.id.textView_user_name);
            tv_order_date = (TextView) v.findViewById(R.id.textView_delivery_date);
            tv_order_total_price = (TextView) v.findViewById(R.id.textView_total_price);
            btn_rate_order = (Button) v.findViewById(R.id.button_rate_order);

        }
    }
}
