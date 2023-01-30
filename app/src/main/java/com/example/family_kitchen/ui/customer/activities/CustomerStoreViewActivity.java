package com.example.family_kitchen.ui.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.CustomerMenuItemsAdapter;
import com.example.family_kitchen.databinding.ActivityCustomerStoreViewBinding;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.preference.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerStoreViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityCustomerStoreViewBinding binding;
    CustomerMenuItemsAdapter customerMenuItemsAdapter;
    ArrayList<Item> itemArrayList;
    ArrayList<Cart> itemArrayCartList;
    DatabaseReference db_items,db_cart;
    String store_id,user_id;

    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerStoreViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        store_id=getIntent().getStringExtra("user_id");
        user_id= FirebaseAuth.getInstance().getUid().toString();
        binding.imageViewBack.setOnClickListener(this);
        binding.buttonViewCart.setOnClickListener(this);
        itemArrayList=new ArrayList<>();
        itemArrayCartList=new ArrayList<>();

        pref = new SharedPref(this);

        db_items = FirebaseDatabase.getInstance().getReference("Items");
        db_cart = FirebaseDatabase.getInstance().getReference("Order");
        fetchItemsData();
    }
    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db_items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayList.clear();
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if(itemArrayList!=null && store_id.equals(item.getUserId())){
                        itemArrayList.add(item);
                    }
                }

                customerMenuItemsAdapter = new CustomerMenuItemsAdapter(CustomerStoreViewActivity.this, itemArrayList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(CustomerStoreViewActivity.this, 1));
                binding.rvItems.setAdapter(customerMenuItemsAdapter);

                binding.progressbarItems.setVisibility(View.GONE);

                if(itemArrayList.size()>0){
                    binding.textViewNoItemFound.setVisibility(View.GONE);
                }
                else{
                    binding.textViewNoItemFound.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        db_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayCartList.clear();
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cart item = postSnapshot.getValue(Cart.class);
                    if(itemArrayCartList!=null && user_id.equals(item.getUserId()) && item.getOrderStatus().equals("customer")){
                        itemArrayCartList.add(item);
                        binding.buttonViewCart.setVisibility(View.VISIBLE);
                        binding.buttonViewCart.setText("View Cart ("+itemArrayCartList.size()+")");
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.imageView_back) {
            finish();
        } else if(id==R.id.button_view_cart){
           startActivity(new Intent(this, CustomerCartActivity.class));
        }
    }
}