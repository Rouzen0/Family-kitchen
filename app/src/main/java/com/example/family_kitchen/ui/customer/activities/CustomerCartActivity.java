package com.example.family_kitchen.ui.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.CustomerCartItemsAdapter;
import com.example.family_kitchen.databinding.ActivityCustomerCartBinding;
import com.example.family_kitchen.interfaces.SetTotalOrderPrice;
import com.example.family_kitchen.model.Cart;
import com.example.family_kitchen.preference.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerCartActivity extends AppCompatActivity implements View.OnClickListener  {
    private ActivityCustomerCartBinding binding;
    CustomerCartItemsAdapter customerCartItemsAdapter;
    ArrayList<Cart> itemArrayCartList;
    DatabaseReference db_cart,db_user;
    String user_id;
    SharedPref pref;
    String order_id;
    int orderTotalPrice;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageViewBack.setOnClickListener(this);
        binding.clPlaceOrder.setOnClickListener(this);

        pref = new SharedPref(this);
        itemArrayCartList=new ArrayList<>();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        db_cart = FirebaseDatabase.getInstance().getReference("Order");
        db_user= FirebaseDatabase.getInstance().getReference("User");
        order_id=pref.getOrderId();
        fetchItemsData();
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    }


    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayCartList.clear();
                orderTotalPrice=0;
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cart cart = postSnapshot.getValue(Cart.class);
                    if(itemArrayCartList!=null && user_id.equals(cart.getUserId())&& (cart.getOrderStatus().equals("customer"))){

                        orderTotalPrice += Integer.parseInt(cart.getTotalPrice());
                        itemArrayCartList.add(cart);
                    }
                }

                binding.textViewOrderTotal.setText(String.valueOf(orderTotalPrice)+" SR");
                binding.textviewPlaceOrderPrice.setText(String.valueOf(orderTotalPrice+10));

                customerCartItemsAdapter = new CustomerCartItemsAdapter(CustomerCartActivity.this, itemArrayCartList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(CustomerCartActivity.this, 1));
                binding.rvItems.setAdapter(customerCartItemsAdapter);

                binding.progressbarItems.setVisibility(View.GONE);

                if(itemArrayCartList.size()>0){
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
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.imageView_back){
            finish();
        } else if(id==R.id.cl_place_order){

            db_cart.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //clearing the previous User list
                 //   itemArrayCartList.clear();
                    //getting all nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Cart cart = postSnapshot.getValue(Cart.class);
                        if(itemArrayCartList!=null && user_id.equals(cart.getUserId())
                                && cart.getOrderStatus().equals("customer") && cart.getOrderId().equals(order_id)){

                            Map<String, Object> updateValues = new HashMap<String, Object>();
                            updateValues.put("orderStatus", "family");
                            updateValues.put("totalOrderPrice", String.valueOf(orderTotalPrice+10));
                            db_cart.child(cart.getOrderItemId()).updateChildren(updateValues);
                            pref.setOrderId("");

                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
            finishAffinity();
            startActivity(new Intent(this,CustomerHomeActivity.class));
        }
    }

}