package com.example.family_kitchen.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityLoginBinding;
import com.example.family_kitchen.preference.SharedPref;
import com.example.family_kitchen.ui.customer.activities.CustomerHomeActivity;
import com.example.family_kitchen.ui.delivery.activities.DeliveryHomeActivity;
import com.example.family_kitchen.ui.family.activities.FamilyHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private String email,password,name;
    private FirebaseAuth mAuth;
    private SharedPref pref;
    private boolean textChanged = true;
    private ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        pref = new SharedPref(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        binding.buttonLogin.setOnClickListener(this);

        binding.tvUserType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textChanged) {

                    validateUser();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.button_Login){
            validateUser();


        }
    }

    private void validateUser(){
        int a=0;
        email=binding.editTextEmail.getText().toString().trim();
        password=binding.editTextPassword.getText().toString().trim();


        if(binding.editTextEmail.getText().toString().isEmpty()){
            a++;
            binding.editTextEmail.setError("Please Enter Email");
        }
        else{
            a--;
            binding.editTextEmail.setError(null);
        }


        if(binding.editTextPassword.getText().toString().isEmpty()){
            a++;
            binding.editTextPassword.setError("Please Enter Password");
        }
        else{
            a--;
            binding.editTextPassword.setError(null);
        }

        if(a<1){
            LoginUser(email,password);
        }
    }


    private void LoginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            final String UserId = mAuth.getCurrentUser().getUid();
                            DatabaseReference UserNode = FirebaseDatabase.getInstance().getReference("User");
                            UserNode.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String usertype = dataSnapshot.child(UserId).child("userType").getValue(String.class);
                                    name = dataSnapshot.child(UserId).child("name").getValue(String.class);
                                    binding.tvUserType.setText(usertype);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                            String gettext = binding.tvUserType.getText().toString();
                            if (gettext.equals("customer")) {
                                Toast.makeText(LoginActivity.this, "User Login Successfully",
                                        Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser();
                                String Uid = user.getUid();
                                Intent i=new Intent(LoginActivity.this, CustomerHomeActivity.class);
                                i.putExtra("key",Uid);
                                startActivity(i);
                                textChanged = false;
                                pref.setLoginState("customer_login");
                                finish();
                                progressDialog.dismiss();
                                // progressBar.setVisibility(View.INVISIBLE);
                            }  else if(gettext.equals("family")){
                                Toast.makeText(LoginActivity.this, "User Login Successfully",
                                        Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser();
                                String Uid = user.getUid();
                                Intent i=new Intent(LoginActivity.this, FamilyHomeActivity.class);
                                i.putExtra("key",Uid);
                                startActivity(i);
                                textChanged = false;
                                pref.setLoginState("family_login");
                                pref.setUserName(name);
                                finish();
                                progressDialog.dismiss();
                                // progressBar.setVisibility(View.INVISIBLE);

                            }
                            else if(gettext.equals("delivery")){
                                Toast.makeText(LoginActivity.this, "User Login Successfully",
                                        Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser();
                                String Uid = user.getUid();
                                Intent i=new Intent(LoginActivity.this, DeliveryHomeActivity.class);
                                i.putExtra("key",Uid);
                                startActivity(i);
                                textChanged = false;
                                pref.setLoginState("delivery_login");

                                finish();

                                progressDialog.dismiss();

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}