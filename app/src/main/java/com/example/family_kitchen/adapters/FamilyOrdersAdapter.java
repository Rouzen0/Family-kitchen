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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.family.activities.FamilyOrderDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FamilyOrdersAdapter extends RecyclerView.Adapter<FamilyOrdersAdapter.ViewHolder> {

    ArrayList<Cart> list;
    private Context context;
    Cart item;
    private DatabaseReference db;

    public FamilyOrdersAdapter(Context ctx, ArrayList <Cart> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_order_family,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);
        holder.tv_order_id.setText(item.getOrderId());
        holder.tv_order_total_price.setText(item.getTotalOrderPrice()+" SR");
        if(item.getOrderStatus().equals("family")) {

            holder.ll_accept_reject.setVisibility(View.VISIBLE);
            holder.btn_complete.setVisibility(View.GONE);
        }
        else if(item.getOrderStatus().equals("family_accepted")){
            holder.btn_complete.setVisibility(View.VISIBLE);
            holder.ll_accept_reject.setVisibility(View.GONE);
        } else if(item.getOrderStatus().equals("delivery_completed")) {

            holder.tv_order_status.setVisibility(View.VISIBLE);
            holder.tv_order_status.setText("Completed");
            holder.btn_complete.setVisibility(View.GONE);
            holder.ll_accept_reject.setVisibility(View.GONE);
        }
        else {
            holder.tv_order_status.setVisibility(View.VISIBLE);
            holder.btn_complete.setVisibility(View.GONE);
            holder.ll_accept_reject.setVisibility(View.GONE);
            holder.tv_order_status.setText("In Progress");
        }


        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item =list.get(position);
                db = FirebaseDatabase.getInstance().getReference("Order");
                Map<String, Object> updateValues = new HashMap<String, Object>();
                updateValues.put("orderStatus", "family_accepted");
                db.child(item.getOrderItemId()).updateChildren(updateValues);
            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = FirebaseDatabase.getInstance().getReference("Order");
                Map<String, Object> updateValues = new HashMap<String, Object>();
                updateValues.put("orderStatus", "family_completed");
                db.child(item.getOrderItemId()).updateChildren(updateValues);

            }
        });


        holder.iv_show_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item =list.get(position);
                Intent intent=new Intent(context, FamilyOrderDetailActivity.class);
                intent.putExtra("order_id",item.getOrderId());
                intent.putExtra("order_total_cost",item.getTotalOrderPrice());
                intent.putExtra("order_status",item.getOrderStatus());
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
        public TextView tv_order_total_price;

        public TextView tv_order_status;

        public Button btn_accept;

        public Button btn_reject;

        public Button btn_complete;

        public LinearLayout ll_accept_reject;

        public ImageView iv_show_detail;

        public ViewHolder(@NonNull View v) {
            super(v);

            tv_order_id = (TextView) v.findViewById(R.id.textView_order_id);
            tv_order_status = (TextView) v.findViewById(R.id.textview_order_status);
            tv_order_total_price = (TextView) v.findViewById(R.id.textView_order_total_price);
            btn_accept = (Button) v.findViewById(R.id.button_accept);
            btn_reject = (Button) v.findViewById(R.id.button_reject);
            btn_complete = (Button) v.findViewById(R.id.button_complete);
            ll_accept_reject=(LinearLayout) v.findViewById(R.id.linearLayout_accept_reject);
            iv_show_detail=(ImageView) v.findViewById(R.id.imageView_show_detail);
        }
    }
}
