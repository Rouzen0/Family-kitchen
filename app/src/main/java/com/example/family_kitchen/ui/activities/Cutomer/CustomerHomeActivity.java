package com.example.family_kitchen.ui.activities.Cutomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

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
}