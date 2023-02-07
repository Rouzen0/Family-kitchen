package com.example.family_kitchen.ui.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.DeliveryMessageAdapter;
import com.example.family_kitchen.adapters.FamilyMessageAdapter;
import com.example.family_kitchen.databinding.FragmentDeliveryMessageBinding;
import com.example.family_kitchen.databinding.FragmentFamilyMessageBinding;
import com.example.family_kitchen.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DeliveryMessageFragment extends Fragment {

    private FragmentDeliveryMessageBinding binding;
    DeliveryMessageAdapter deliveryMessageAdapter;
    ArrayList<Cart> itemArrayList;
    DatabaseReference db;
    String user_id,order_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryMessageBinding.inflate(inflater,container,false);

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
                    if(itemArrayList!=null && user_id.equals(user_id) ){

                        if(!item.getOrderId().equals(order_id) && user_id.equals(item.getDriverId())) {

                            itemArrayList.add(item);
                            order_id=item.getOrderId();
                        }

                    }
                }
                order_id="";
                deliveryMessageAdapter = new DeliveryMessageAdapter(getActivity(), itemArrayList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                binding.rvItems.setAdapter(deliveryMessageAdapter);

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