package com.example.family_kitchen.ui.family.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityFamilyUpdateItemBinding;
import com.example.family_kitchen.preference.SharedPref;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class FamilyUpdateItemActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityFamilyUpdateItemBinding binding;
    private String itemName,itemPrice,itemImageUrl,storeName,itemId,userId;
    private StorageReference mStorageRef;
    private DatabaseReference db;
    private Uri imageUri;
    private ProgressDialog mProgressDialog;
    private SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyUpdateItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        itemName=getIntent().getStringExtra("item_name");
        itemPrice=getIntent().getStringExtra("item_price");
        itemImageUrl=getIntent().getStringExtra("item_url");
        itemId=getIntent().getStringExtra("item_id");
        userId=getIntent().getStringExtra("user_id");
        pref=new SharedPref(this);
        db= FirebaseDatabase.getInstance().getReference("Items");
        mStorageRef = FirebaseStorage.getInstance().getReference("Items");
        binding.editTextItemName.setText(itemName);
        binding.editTextItemPrice.setText(itemPrice);


            Picasso.get().load(itemImageUrl).placeholder(R.drawable.image_place_holder).into(binding.imageViewItemPicture);


        binding.buttonUpdateItem.setOnClickListener(this);
        binding.buttonCancel.setOnClickListener(this);
        binding.imageViewBack.setOnClickListener(this);
        binding.imageViewItemPicture.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_update_item) {


            int a = 0;
            itemName = binding.editTextItemName.getText().toString();
            itemPrice = binding.editTextItemPrice.getText().toString();


            if (binding.editTextItemName.getText().toString().isEmpty()) {
                a++;
                binding.editTextItemName.setError("Please Enter Item Name");
            } else {
                a--;
                binding.editTextItemName.setError(null);
            }


            if (binding.editTextItemPrice.getText().toString().isEmpty()) {
                a++;
                binding.editTextItemPrice.setError("Please Enter Item Price");
            } else {
                a--;
                binding.editTextItemPrice.setError(null);
            }

            if (a < 1) {
                storeName = pref.getUserName();

                if (imageUri != null) {
                    mProgressDialog = ProgressDialog.show(this, "Uploading Item", "Please wait...", false, false);
                    StorageReference fileReference = mStorageRef.child(itemId);
                    fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();


                                    Map<String, Object> updateValues = new HashMap<String, Object>();

                                    updateValues.put("itemName", itemName);
                                    updateValues.put("itemPrice", itemPrice);
                                    updateValues.put("itemImageUrl", imageUrl);
                                    db.child(itemId).updateChildren(updateValues);

                                    Toast.makeText(FamilyUpdateItemActivity.this, "Item Updated", Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    });


                } else {

                    Map<String, Object> updateValues = new HashMap<String, Object>();

                    updateValues.put("itemName", itemName);
                    updateValues.put("itemPrice", itemPrice);
                    updateValues.put("itemImageUrl", itemImageUrl);

                    db.child(itemId).updateChildren(updateValues);
                    Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            } else if (id == R.id.button_cancel) {
                finish();
            } else if (id == R.id.imageView_back) {
                finish();
            } else if(id==R.id.imageView_item_picture){
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