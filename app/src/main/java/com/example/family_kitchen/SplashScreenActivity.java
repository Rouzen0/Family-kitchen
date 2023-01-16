package com.example.family_kitchen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.customer.activities.CustomerHomeActivity;
import com.example.family_kitchen.ui.delivery.activities.DeliveryHomeActivity;
import com.example.family_kitchen.ui.family.activities.FamilyHomeActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pref=new SharedPref(this);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                if(pref.getLoginState().equals("customer_login")){
                    startActivity(new Intent(SplashScreenActivity.this, CustomerHomeActivity.class));
                }else if(pref.getLoginState().equals("family_login")){
                    startActivity(new Intent(SplashScreenActivity.this, FamilyHomeActivity.class));
                }else if(pref.getLoginState().equals("delivery_login")){
                    startActivity(new Intent(SplashScreenActivity.this, DeliveryHomeActivity.class));
                } else{
                    startActivity(new Intent(SplashScreenActivity.this, IntroActivity.class));
                }

                finish();
            }
        }, 3000);

    }
}