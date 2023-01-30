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
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.customer.activities.CustomerStoreViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomerMenuItemsAdapter extends RecyclerView.Adapter<CustomerMenuItemsAdapter.ViewHolder> {

    ArrayList<Item> list;
    private Context context;

    String date;
    Item item;

    private SimpleDateFormat dateFormat;
    private DatabaseReference db;

    private SharedPref pref;

    public CustomerMenuItemsAdapter(Context ctx, ArrayList <Item> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_customer_menu,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);

        holder.tv_item_name.setText(item.getItemName());
        holder.tv_item_price.setText(item.getItemPrice()+" SR");
        if(!item.getItemImageUrl().equals("")) {
            Picasso.get().load(item.getItemImageUrl()).placeholder(R.drawable.image_place_holder_large).into(holder.iv_item);
        } else{
            holder.iv_item.setImageDrawable(context.getResources().getDrawable(R.drawable.image_place_holder_large));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref=new SharedPref(context);
                item =list.get(position);
                db= FirebaseDatabase.getInstance().getReference("Order");
                String uid= FirebaseAuth.getInstance().getUid().toString();
                String cartItemId=db.push().getKey();
                String currentDate=getCurrentDate();

                //  public cart(String itemName, String itemPrice, String itemImageUrl, String userId, String itemId) {

                if(pref.getOrderId()!=null && pref.getOrderId()!="") {
                    Cart cart = new Cart(item.getItemName(), item.getItemPrice(), item.getItemImageUrl(), uid, item.getItemId(), pref.getOrderId(),"1",item.getItemPrice(),item.getItemPrice(),item.getUserId(),"customer",currentDate,cartItemId,"");
                    db.child(cartItemId).setValue(cart);
                } else{
                    String orderId=db.push().getKey();
                    pref.setOrderId(orderId);
                    Cart cart = new Cart(item.getItemName(), item.getItemPrice(), item.getItemImageUrl(), uid, item.getItemId(), pref.getOrderId(),"1",item.getItemPrice(),item.getItemPrice(),item.getUserId(),"customer",currentDate,cartItemId,"");
                    db.child(cartItemId).setValue(cart);

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
        public ImageView iv_item;

        public ViewHolder(@NonNull View v) {
            super(v);

            tv_item_name = (TextView) v.findViewById(R.id.textView_item_name);
            tv_item_price = (TextView) v.findViewById(R.id.textView_item_price);
            iv_item = (ImageView) v.findViewById(R.id.imageView_item);
        }
    }

    public String getCurrentDate(){

        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        date = dateFormat.format(calendar.getTime());
        String currentDate=date;
        return currentDate;

    }
}
