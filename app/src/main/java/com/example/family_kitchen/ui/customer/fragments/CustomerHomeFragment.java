package com.example.family_kitchen.ui.customer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.CustomerItemsAdapter;
import com.example.family_kitchen.adapters.FamilyItemsAdapter;
import com.example.family_kitchen.databinding.FragmentCustomerHomeBinding;
import com.example.family_kitchen.databinding.FragmentFamilyMyShopBinding;
import com.example.family_kitchen.model.Item;
import com.example.family_kitchen.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CustomerHomeFragment extends Fragment {

    private FragmentCustomerHomeBinding binding;
    CustomerItemsAdapter customerItemsAdapter;
    ArrayList<User> userArrayList;
    DatabaseReference db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCustomerHomeBinding.inflate(inflater,container,false);
        userArrayList=new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("User");
        fetchItemsData();
        return binding.getRoot();
    }

    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                userArrayList.clear();
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User item = postSnapshot.getValue(User.class);
                    if(userArrayList!=null && item.getUserType().equals("family")){
                        userArrayList.add(item);
                    }
                }

                customerItemsAdapter = new CustomerItemsAdapter(getActivity(), userArrayList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                binding.rvItems.setAdapter(customerItemsAdapter);

                binding.progressbarItems.setVisibility(View.GONE);

                if(userArrayList.size()>0){
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