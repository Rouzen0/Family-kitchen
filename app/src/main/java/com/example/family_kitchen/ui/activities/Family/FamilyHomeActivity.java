package com.example.family_kitchen.ui.activities.Family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityFamilyHomeBinding;

public class FamilyHomeActivity extends AppCompatActivity {
    private ActivityFamilyHomeBinding binding;
    public NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_family_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigation,navController);

    }
}