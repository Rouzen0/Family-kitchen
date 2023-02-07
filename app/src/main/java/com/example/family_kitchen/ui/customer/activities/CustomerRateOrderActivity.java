package com.example.family_kitchen.ui.customer.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityCustomerRateOrderBinding;
import com.example.family_kitchen.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CustomerRateOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityCustomerRateOrderBinding binding;
    String orderId,familyId,driverId,ratingStore,ratingDelivery,familyName,driverName;
    
    DatabaseReference db_rating,db_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rate_order);

        binding = ActivityCustomerRateOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderId=getIntent().getStringExtra("order_id");
        familyId=getIntent().getStringExtra("family_id");
        driverId=getIntent().getStringExtra("driver_id");

        binding.imageViewBack.setOnClickListener(this);
        binding.buttonSubmit.setOnClickListener(this);

        db_rating = FirebaseDatabase.getInstance().getReference("Rating");
        db_user = FirebaseDatabase.getInstance().getReference("User");

    }

    @Override
    public void onStart() {
        super.onStart();
        db_rating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    ratingDelivery=dataSnapshot.child(orderId).child("deliveryRating").getValue(String.class);
                    ratingStore=dataSnapshot.child(orderId).child("storeRating").getValue(String.class);

                    if(ratingDelivery!=null){
                        binding.ratingBarDelivery.setRating(Float.parseFloat(ratingDelivery));
                        binding.ratingBarStore.setRating(Float.parseFloat(ratingStore));
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                familyName=dataSnapshot.child(familyId).child("name").getValue(String.class);
                driverName=dataSnapshot.child(driverId).child("name").getValue(String.class);

                binding.textViewStoreName.setText("Store: "+familyName);
                binding.textViewDriverName.setText("Delivery : "+driverName);

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
        } else if(id==R.id.button_submit){
            ratingStore= String.valueOf(binding.ratingBarStore.getRating());
            ratingDelivery= String.valueOf(binding.ratingBarDelivery.getRating());

//            private String DeliveryId,orderId,FamilyId,StoreRating,DeliveryRating;
            
            Rating rating=new Rating(driverId,orderId,familyId,ratingStore,ratingDelivery);
            db_rating.child(orderId).setValue(rating);

            Toast.makeText(this, "Rating Submitted", Toast.LENGTH_SHORT).show();

        }
    }
}