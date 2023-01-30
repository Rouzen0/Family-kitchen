package com.example.family_kitchen.ui.family.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.FamilyOrderDetailAdapter;
import com.example.family_kitchen.databinding.ActivityFamilyOrderDetailBinding;
import com.example.family_kitchen.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FamilyOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityFamilyOrderDetailBinding binding;

    FamilyOrderDetailAdapter familyOrderDetailAdapter;
    ArrayList<Cart> itemArrayCartList;
    DatabaseReference db_cart;
    String user_id;
    int orderTotalPrice;
    String orderId,order_total_cost,order_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId=getIntent().getStringExtra("order_id");
        order_total_cost=getIntent().getStringExtra("order_total_cost");
        order_status=getIntent().getStringExtra("order_status");

        itemArrayCartList=new ArrayList<>();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        db_cart = FirebaseDatabase.getInstance().getReference("Order");
        binding.imageViewBack.setOnClickListener(this);


        binding.textViewTotalCost.setText(order_total_cost);
        binding.textViewOrderId.setText(orderId);


        if(order_status.equals("delivery_completed")){
            binding.textViewDeliveryState.setText("Completed");
        } else {
            binding.textViewDeliveryState.setText("In Progress");
        }


        fetchItemsData();

    }

    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayCartList.clear();
                orderTotalPrice=0;
                //getting all data
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cart cart = postSnapshot.getValue(Cart.class);
                    if(itemArrayCartList!=null && cart.getOrderId().equals(orderId)){

                        orderTotalPrice += Integer.parseInt(cart.getTotalPrice());
                        itemArrayCartList.add(cart);
                    }
                }


                familyOrderDetailAdapter = new FamilyOrderDetailAdapter(FamilyOrderDetailActivity.this, itemArrayCartList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(FamilyOrderDetailActivity.this, 1));
                binding.rvItems.setAdapter(familyOrderDetailAdapter);
//
                binding.progressbarItems.setVisibility(View.GONE);


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
        }
    }
}