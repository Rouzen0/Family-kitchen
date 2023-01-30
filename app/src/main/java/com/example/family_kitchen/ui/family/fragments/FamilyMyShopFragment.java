package com.example.family_kitchen.ui.family.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.FamilyItemsAdapter;
import com.example.family_kitchen.databinding.FragmentCustomerMoreBinding;
import com.example.family_kitchen.databinding.FragmentFamilyMyShopBinding;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.ui.family.activities.FamilyAddItemActivity;
import com.example.family_kitchen.ui.family.activities.StoreInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FamilyMyShopFragment extends Fragment implements View.OnClickListener {
    private FragmentFamilyMyShopBinding binding;
    FamilyItemsAdapter familyItemsAdapter;
    ArrayList<Item> itemArrayList;
    DatabaseReference db;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFamilyMyShopBinding.inflate(inflater,container,false);
        binding.buttonAddNewItem.setOnClickListener(this);
        binding.buttonStoreInfo.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        userId=firebaseAuth.getCurrentUser().getUid().toString();
        itemArrayList=new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Items");
        fetchItemsData();
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.button_add_new_item){
            startActivity(new Intent(getActivity(), FamilyAddItemActivity.class));
        } else if(id==R.id.button_store_info){
            startActivity(new Intent(getActivity(), StoreInfoActivity.class));
        }
    }

    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayList.clear();
                //getting all data
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if(itemArrayList!=null && item.getUserId().equals(userId)){
                        itemArrayList.add(item);
                    }
                }

                familyItemsAdapter = new FamilyItemsAdapter(getActivity(), itemArrayList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                binding.rvItems.setAdapter(familyItemsAdapter);

                binding.progressbarItems.setVisibility(View.GONE);

                if(itemArrayList.size()>0){
                    binding.textViewNoItemFound.setVisibility(View.GONE);
                }
                else{
                    binding.textViewNoItemFound.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}