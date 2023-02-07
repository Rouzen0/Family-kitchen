package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.model.Rating;
import com.example.family_kitchen.model.User;
import com.example.family_kitchen.ui.customer.activities.CustomerCartActivity;
import com.example.family_kitchen.ui.customer.activities.CustomerStoreViewActivity;
import com.example.family_kitchen.ui.family.activities.FamilyUpdateItemActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomerItemsAdapter extends RecyclerView.Adapter<CustomerItemsAdapter.ViewHolder> {

    ArrayList<User> list;
    private Context context;
    User item;
    float family_rating,count;
    private DatabaseReference db_rating;

    public CustomerItemsAdapter(Context ctx, ArrayList <User> list) {
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

        holder.tv_item_name.setText(item.getName());
        holder.tv_item_price.setText(item.getCategory());
        if(!item.getImage().equals("")) {
            Picasso.get().load(item.getImage()).placeholder(R.drawable.image_place_holder_large).into(holder.iv_item);
        } else{
            holder.iv_item.setImageDrawable(context.getResources().getDrawable(R.drawable.image_place_holder_large));
        }

        if(item.getAvailable().equals("false")){
            holder.tv_item_store_status.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = list.get(position);
                if(item.getAvailable().equals("true")) {
                    Intent intent = new Intent(context, CustomerStoreViewActivity.class);
                    intent.putExtra("user_id", item.getUserId());
                    context.startActivity(intent);
                } else{
                    Toast.makeText(context, "Store is closed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        db_rating = FirebaseDatabase.getInstance().getReference("Rating");

        db_rating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item =list.get(position);
                    Rating rating = postSnapshot.getValue(Rating.class);
                    String s=rating.getFamilyId();
                    if(s.equals(item.getUserId())) {

                        family_rating += Float.parseFloat(rating.getStoreRating());
                        count++;

                        float rate = family_rating / count;
                        if(rate==5.0){
                            holder.tv_item_rating.setText("5.0");
                        }
                        else {
                            holder.tv_item_rating.setText(new DecimalFormat("#.#").format(rate));
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
        public TextView tv_item_rating;
        public TextView tv_item_store_status;
        public ImageView iv_item;

        public ViewHolder(@NonNull View v) {
            super(v);

            tv_item_name = (TextView) v.findViewById(R.id.textView_item_name);
            tv_item_rating = (TextView) v.findViewById(R.id.textView_rating);
            tv_item_price = (TextView) v.findViewById(R.id.textView_item_price);
            tv_item_store_status = (TextView) v.findViewById(R.id.textView_item_store_status);
            iv_item = (ImageView) v.findViewById(R.id.imageView_item);
        }
    }
}
