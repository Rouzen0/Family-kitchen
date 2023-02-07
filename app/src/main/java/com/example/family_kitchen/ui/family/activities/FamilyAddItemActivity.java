package com.example.family_kitchen.ui.family.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.family_kitchen.R;
import com.example.family_kitchen.databinding.ActivityFamilyAddItemBinding;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.preference.SharedPref;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FamilyAddItemActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityFamilyAddItemBinding binding;
    private Uri imageUri;
    private String itemName,itemPrice,itemImageUri,storeName;
    private DatabaseReference db;
    private StorageReference mStorageRef;
    private SharedPref pref;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db= FirebaseDatabase.getInstance().getReference("Items");

        pref=new SharedPref(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("Items");

        binding.imageViewBack.setOnClickListener(this);
        binding.buttonAddItem.setOnClickListener(this);
        binding.buttonCancel.setOnClickListener(this);
        binding.imageViewItemPicture.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.imageView_back){
            finish();
        } else if(id==R.id.button_add_item){


            int a=0;
            itemName=binding.editTextItemName.getText().toString();
            itemPrice=binding.editTextItemPrice.getText().toString();


            if(binding.editTextItemName.getText().toString().isEmpty()){
                a++;
                binding.editTextItemName.setError("Please Enter Item Name");
            }
            else{

                binding.editTextItemName.setError(null);
            }


            if(binding.editTextItemPrice.getText().toString().isEmpty()){
                a++;
                binding.editTextItemPrice.setError("Please Enter Item Price");
            }
            else{

                binding.editTextItemPrice.setError(null);
            }

            if(a<1){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                String sid=db.push().getKey();
                storeName=pref.getUserName();


                if (imageUri != null) {
                    mProgressDialog = ProgressDialog.show(this, "Uploading Item", "Please wait...", false, false);
                    StorageReference fileReference = mStorageRef.child(sid);
                    fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl= uri.toString();
                                    Item item = new Item(itemName,itemPrice,imageUrl,storeName,userId,sid);
                                    db.child(sid).setValue(item);
                                    Toast.makeText(FamilyAddItemActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    });


                } else{

                    Item item = new Item(itemName,itemPrice,"",storeName,userId,sid);
                    db.child(sid).setValue(item);
                    Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


        }  else if(id==R.id.button_cancel){
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