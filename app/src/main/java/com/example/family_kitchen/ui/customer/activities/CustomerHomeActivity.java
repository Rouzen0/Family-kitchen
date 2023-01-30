package com.example.family_kitchen.ui.customer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityCustomerHomeBinding;

public class CustomerHomeActivity extends AppCompatActivity {
    private ActivityCustomerHomeBinding binding;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_customer_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigation,navController);

    }
    public void showCustomerMore(MenuItem item) {
        startActivity(new Intent(this,CustomerMoreActivity.class));
    }
}