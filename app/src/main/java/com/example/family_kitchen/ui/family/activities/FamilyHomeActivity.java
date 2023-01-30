package com.example.family_kitchen.ui.family.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
    public void ShowFamilyMore(MenuItem item) {
        startActivity(new Intent(this,FamilyMoreActivity.class));
    }
}