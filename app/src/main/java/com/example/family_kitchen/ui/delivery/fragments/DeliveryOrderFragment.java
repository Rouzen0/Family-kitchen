package com.example.family_kitchen.ui.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.DeliveryOrdersAdapter;
import com.example.family_kitchen.adapters.FamilyOrdersAdapter;
import com.example.family_kitchen.databinding.FragmentDeliveryOrderBinding;
import com.example.family_kitchen.databinding.FragmentFamilyOrdersBinding;
import com.example.family_kitchen.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeliveryOrderFragment extends Fragment {
    private FragmentDeliveryOrderBinding binding;
    DeliveryOrdersAdapter deliveryOrdersAdapter;
    ArrayList<Cart> itemArrayList;
    DatabaseReference db;
    String user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryOrderBinding.inflate(inflater,container,false);
        itemArrayList=new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Order");
        fetchItemsData();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        return binding.getRoot();
    }

    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayList.clear();
                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Cart item = postSnapshot.getValue(Cart.class);
                    if(!item.getDriverId().equals("")){
                        if(itemArrayList!=null && user_id.equals(item.getDriverId())  && ((item.getOrderStatus().equals("family_completed")
                                || item.getOrderStatus().equals("delivery_accepted") || item.getOrderStatus().equals("delivery_completed")))){
                            itemArrayList.add(item);
                        }}
                    else{
                        if(itemArrayList!=null  && ((item.getOrderStatus().equals("family_completed")
                                || item.getOrderStatus().equals("delivery_accepted") || item.getOrderStatus().equals("delivery_completed")))){
                            itemArrayList.add(item);
                        }
                    }
                }

                deliveryOrdersAdapter = new DeliveryOrdersAdapter(getActivity(), itemArrayList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                binding.rvItems.setAdapter(deliveryOrdersAdapter);

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