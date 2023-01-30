package com.example.family_kitchen.ui.delivery.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityDeliveryOrderDetailBinding;
public class DeliveryOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDeliveryOrderDetailBinding binding;

    String orderId, customerName, orderDate, customerId, familyId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeliveryOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        orderId = getIntent().getStringExtra("order_id");
        customerName = getIntent().getStringExtra("customer_name");
        orderDate = getIntent().getStringExtra("order_date");
        customerId = getIntent().getStringExtra("customer_id");
        familyId = getIntent().getStringExtra("family_id");


        binding.textViewName.setText(customerName);
        binding.textViewOrderDateTime.setText(orderDate);
        binding.textViewOrderId.setText("Order Id #" + orderId);
        binding.textViewTextImage.setText(String.valueOf(customerName.charAt(0)));

        binding.imageViewBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.imageView_back){
            finish();
        }
    }
}