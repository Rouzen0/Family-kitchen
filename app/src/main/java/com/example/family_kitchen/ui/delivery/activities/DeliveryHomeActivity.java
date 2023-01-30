package com.example.family_kitchen.ui.delivery.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityCustomerHomeBinding;
import com.example.family_kitchen.databinding.ActivityDeliveryHomeBinding;

public class DeliveryHomeActivity extends AppCompatActivity {
    private ActivityDeliveryHomeBinding binding;
    public NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_delivery_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigation,navController);
    }
    public void ShowDeliveryMore(MenuItem item) {
        startActivity(new Intent(this,DeliveryMoreActivity.class));
    }
}