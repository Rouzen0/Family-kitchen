package com.example.family_kitchen.ui.customer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;
import java.util.Locale;


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

        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });

        fetchItemsData();
        return binding.getRoot();
    }

    private void filter(String s){
        ArrayList<User> searchedArrayList = new ArrayList<>();

        for (User user : userArrayList)
        {
            if(user.getName().toLowerCase().contains(s.toLowerCase(Locale.getDefault()))){

                searchedArrayList.add(user);

            }
        }


        customerItemsAdapter = new CustomerItemsAdapter(getActivity(), searchedArrayList);
        binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        binding.rvItems.setAdapter(customerItemsAdapter);

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