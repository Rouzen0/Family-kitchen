package com.example.family_kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.family_kitchen.databinding.ActivityIntroBinding;
import com.example.family_kitchen.ui.LoginActivity;
import com.example.family_kitchen.ui.SignupActivity;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonLogin.setOnClickListener(this);
        binding.buttonSignUp.setOnClickListener(this);
        binding.textViewSignupDelivery.setOnClickListener(this);
        binding.textViewSignupFamily.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.button_Login){
            startActivity(new Intent(this, LoginActivity.class));

        }else if(id==R.id.button_sign_up){
            Intent intent=new Intent(this, SignupActivity.class);
            intent.putExtra("user_type","customer");
            startActivity(intent);

        }else if(id==R.id.textView_signup_delivery){

            Intent intent=new Intent(this, SignupActivity.class);
            intent.putExtra("user_type","delivery");
            startActivity(intent);

        }else if(id==R.id.textView_signup_family){

            Intent intent=new Intent(this, SignupActivity.class);
            intent.putExtra("user_type","family");
            startActivity(intent);
        }
    }
}