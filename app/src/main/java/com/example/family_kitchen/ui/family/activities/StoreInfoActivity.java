package com.example.family_kitchen.ui.family.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityStoreInfoBinding;
import com.example.family_kitchen.preference.SharedPref;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class StoreInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityStoreInfoBinding binding;
    private Uri imageUri;
    private String storeCategory,itemPrice,itemImageUri,storeName;
    private DatabaseReference db;
    private StorageReference mStorageRef;
    private SharedPref pref;
    private ProgressDialog mProgressDialog;

    String category,imageUrl;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityStoreInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db= FirebaseDatabase.getInstance().getReference("User");

        pref=new SharedPref(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("Store");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        binding.imageViewBack.setOnClickListener(this);
        binding.buttonAddItem.setOnClickListener(this);
        binding.imageViewItemPicture.setOnClickListener(this);
    }


    protected void onStart() {

        super.onStart();


        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrl = dataSnapshot.child(userId).child("image").getValue(String.class);
                if(!imageUrl.equals("")) {
                    Picasso.get().load(imageUrl).placeholder(R.drawable.image_place_holder).into(binding.imageViewItemPicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String categoryId=pref.getCategory();
        if(categoryId!="") {
            binding.spinnerCategory.setSelection(Integer.parseInt(categoryId));
        }

    }


    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.imageView_back){
            finish();
        } else if(id==R.id.button_add_item){

            storeCategory=binding.spinnerCategory.getSelectedItem().toString();

            if(!storeCategory.equals("Select Category")) {



                String sid = db.push().getKey();
                storeName = pref.getUserName();


                if (imageUri != null) {
                    mProgressDialog = ProgressDialog.show(this, "Uploading", "Please wait...", false, false);
                    StorageReference fileReference = mStorageRef.child(sid);
                    fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl= uri.toString();
                                    Map<String, Object> updateValues = new HashMap<String, Object>();
                                    updateValues.put("category", storeCategory);
                                    updateValues.put("image", imageUrl);
                                    db.child(userId).updateChildren(updateValues);

                                    Toast.makeText(StoreInfoActivity.this, "Store Information Saved", Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                    finish();

                                    int categoryId=binding.spinnerCategory.getSelectedItemPosition();

                                    pref.setCategory(String.valueOf(categoryId));
                                }
                            });
                        }
                    });


                } else {

                    Map<String, Object> updateValues = new HashMap<String, Object>();
                    updateValues.put("category", storeCategory);
                    db.child(userId).updateChildren(updateValues);

                    Toast.makeText(StoreInfoActivity.this, "Store Information Saved", Toast.LENGTH_SHORT).show();

                }
            }
            else{
                Toast.makeText(this, "Please Select Any Category", Toast.LENGTH_SHORT).show();
            }


        }  else if(id==R.id.imageView_item_picture){
            ImagePicker.with(this)
                    .compress(512)
                    .maxResultSize(600, 600)
                    .crop()
                    .start();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            binding.imageViewItemPicture.setImageURI(imageUri);
        }
    }
}