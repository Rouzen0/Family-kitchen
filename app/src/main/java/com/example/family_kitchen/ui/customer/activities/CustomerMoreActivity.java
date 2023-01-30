package com.example.family_kitchen.ui.customer.activities;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityCustomerMoreBinding;
import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerMoreActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityCustomerMoreBinding binding;
    private SharedPref pref;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private String uid,name,phoneNumber,emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonSignOut.setOnClickListener(this);
        pref=new SharedPref(this);
        firebaseAuth=FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference("User");
        uid=firebaseAuth.getUid().toString();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.button_sign_out){
            FirebaseAuth.getInstance().signOut();
            pref.setLoginState("");
            startActivity(new Intent(this, LoginActivity.class));
            this.finishAffinity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.progressBarProfile.setVisibility(View.VISIBLE);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name=dataSnapshot.child(uid).child("name").getValue(String.class);
                phoneNumber=dataSnapshot.child(uid).child("phoneNumber").getValue(String.class);
                emailAddress=dataSnapshot.child(uid).child("email").getValue(String.class);

                binding.textViewEmail.setText(emailAddress);
                binding.textViewName.setText(name);
                binding.textViewPhone.setText(phoneNumber);
                binding.textViewTextImage.setText(String.valueOf(name.charAt(0)));
                binding.progressBarProfile.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}