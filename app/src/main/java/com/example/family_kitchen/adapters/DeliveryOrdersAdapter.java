package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.delivery.activities.DeliveryOrderDetailActivity;
import com.example.family_kitchen.ui.family.activities.FamilyOrderDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryOrdersAdapter extends RecyclerView.Adapter<DeliveryOrdersAdapter.ViewHolder> {

    ArrayList<Cart> list;
    private Context context;
    Cart item;
    private DatabaseReference db;


    String name;

    String driver_Id;


    public DeliveryOrdersAdapter(Context ctx, ArrayList <Cart> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_order_delivery,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);

        holder.tv_order_id.setText("Order Id #"+item.getOrderId());
        holder.tv_order_date.setText(item.getOrderDate());


        if(item.getOrderStatus().equals("family_completed")) {

            holder.btn_accept.setVisibility(View.VISIBLE);
            holder.btn_deliver.setVisibility(View.GONE);
            holder.tv_order_status.setVisibility(View.GONE);
        }
        else if(item.getOrderStatus().equals("delivery_accepted")){

            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_deliver.setVisibility(View.VISIBLE);
            holder.tv_order_status.setVisibility(View.GONE);
        }else if(item.getOrderStatus().equals("delivery_completed")){

            holder.tv_order_status.setVisibility(View.VISIBLE);
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_deliver.setVisibility(View.GONE);
        }

        db= FirebaseDatabase.getInstance().getReference("User");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name=dataSnapshot.child(item.getUserId()).child("name").getValue(String.class);
                holder.tv_name.setText(name);
                holder.tv_image_text.setText(String.valueOf(name.charAt(0)));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item =list.get(position);
                driver_Id= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                db = FirebaseDatabase.getInstance().getReference("Order");
                Map<String, Object> updateValues = new HashMap<String, Object>();
                updateValues.put("orderStatus", "delivery_accepted");
                updateValues.put("driverId", driver_Id);
                db.child(item.getOrderItemId()).updateChildren(updateValues);


            }
        });


        holder.btn_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item =list.get(position);

                db = FirebaseDatabase.getInstance().getReference("Order");
                Map<String, Object> updateValues = new HashMap<String, Object>();
                updateValues.put("orderStatus", "delivery_completed");
                db.child(item.getOrderItemId()).updateChildren(updateValues);

                Toast.makeText(context, "Delivery Completed", Toast.LENGTH_SHORT).show();

            }
        });


        holder.iv_show_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item =list.get(position);
                Intent intent=new Intent(context, DeliveryOrderDetailActivity.class);
                intent.putExtra("customer_name",holder.tv_name.getText().toString());
                intent.putExtra("order_id",item.getOrderId());
                intent.putExtra("order_date",item.getOrderDate());
                intent.putExtra("customer_id",item.getUserId());
                intent.putExtra("family_id",item.getFamilyId());


                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_order_id;

        public TextView tv_name;

        public TextView tv_order_status;
        public TextView tv_image_text;

        public TextView tv_order_date;
        public TextView tv_order_total_price;

        public Button btn_accept;

        public Button btn_deliver;

        public ImageView iv_show_detail;

        public ViewHolder(@NonNull View v) {
            super(v);

            tv_order_id = (TextView) v.findViewById(R.id.textView_order_id);
            tv_name = (TextView) v.findViewById(R.id.textView_name);
            tv_order_status = (TextView) v.findViewById(R.id.textview_order_status);
            tv_image_text = (TextView) v.findViewById(R.id.textView_text_image);
            tv_order_date = (TextView) v.findViewById(R.id.textView_order_date_time);
            tv_order_total_price = (TextView) v.findViewById(R.id.textView_order_total_price);
            btn_accept = (Button) v.findViewById(R.id.button_accept);
            btn_deliver = (Button) v.findViewById(R.id.button_deliver);
            iv_show_detail = (ImageView) v.findViewById(R.id.imageView_show_detail);
        }
    }
}
