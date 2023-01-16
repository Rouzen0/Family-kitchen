package com.example.family_kitchen.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityIntroBinding;
import com.example.family_kitchen.databinding.ActivitySignupBinding;
import com.example.family_kitchen.model.UserRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignupBinding binding;
    private String name,email,password,phoneNumber,maroofNumber;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCreateAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference("User");

        userType=getIntent().getStringExtra("user_type");

        if(userType.equals("family")){
            binding.editTextMaroofNumber.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.button_create_account){

            int a= 0;

            name=binding.editTextName.getText().toString().trim();
            email=binding.editTextEmail.getText().toString().trim();
            password=binding.editTextPassword.getText().toString().trim();
            phoneNumber=binding.editTextPhoneNumber.getText().toString().trim();
            maroofNumber=binding.editTextPhoneNumber.getText().toString().trim();

            if(binding.editTextName.getText().toString().isEmpty()){
                a++;
                binding.editTextName.setError("Please Enter Name");
            }
            else{
                a--;
                binding.editTextName.setError(null);
            }

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

            if(binding.editTextPhoneNumber.getText().toString().isEmpty()){
                a++;
                binding.editTextPhoneNumber.setError("Please Enter Phone Number");
            }
            else{
                a--;
                binding.editTextPhoneNumber.setError(null);
            }

            if(userType.equals("family")) {
                binding.editTextMaroofNumber.setVisibility(View.VISIBLE);

                if (binding.editTextMaroofNumber.getText().toString().isEmpty()) {
                    a++;
                    binding.editTextMaroofNumber.setError("Please Enter Maroof Number");
                } else {
                    a--;
                    binding.editTextMaroofNumber.setError(null);
                }
            }
            if(a<1){

                RegisterUser(email,password);


            }
        }
    }

    private void RegisterUser(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignupActivity.this, "User Registered Successfully",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();


                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    addDataInDb();
                                }
                            }, 5000);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addDataInDb() {
        name=binding.editTextName.getText().toString().trim();
        email=binding.editTextEmail.getText().toString().trim();
        password=binding.editTextPassword.getText().toString().trim();
        phoneNumber=binding.editTextPhoneNumber.getText().toString().trim();
        maroofNumber=binding.editTextMaroofNumber.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        String Name,Email,Password,PhoneNumber,MaroofNumber,UserId;
        UserRegistration userRegistration= new UserRegistration(name,email,password,phoneNumber,maroofNumber,userType,userId);
        db.child(userId).setValue(userRegistration);
    }

}