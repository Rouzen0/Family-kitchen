package com.example.family_kitchen.ui.delivery.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.family_kitchen.R;
import com.example.family_kitchen.adapters.ChatAdapterDelivery;
import com.example.family_kitchen.databinding.ActivityFamilyChatBinding;
import com.example.family_kitchen.model.ChatDelivery;
import com.example.family_kitchen.preference.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DeliveryChatActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityFamilyChatBinding binding;
    String customerId,familyId,orderId,message,customerName,familyName,driverId;

    ChatAdapterDelivery chatAdapterDelivery;
    ArrayList<ChatDelivery> itemArrayCartList;
    DatabaseReference db_user;
    String user_id;

    SharedPref pref;

    int orderTotalPrice;

    DatabaseReference db_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat);

        binding = ActivityFamilyChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        customerId = getIntent().getStringExtra("user_id");
        familyId = getIntent().getStringExtra("family_id");
        orderId = getIntent().getStringExtra("order_id");
        driverId = getIntent().getStringExtra("driver_id");

        db_chat = FirebaseDatabase.getInstance().getReference("Chat_Driver_Customer");
        db_user = FirebaseDatabase.getInstance().getReference("User");

        binding.textViewSend.setOnClickListener(this);
        binding.imageViewBack.setOnClickListener(this);

        pref = new SharedPref(this);
        itemArrayCartList=new ArrayList<>();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        fetchItemsData();
    }


    @Override
    public void onStart() {
        super.onStart();


        db_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                customerName=dataSnapshot.child(customerId).child("name").getValue(String.class);
                familyName=dataSnapshot.child(familyId).child("name").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }


    private void fetchItemsData() {
        binding.progressbarItems.setVisibility(View.VISIBLE);
        db_chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous User list
                itemArrayCartList.clear();

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ChatDelivery chat = postSnapshot.getValue(ChatDelivery.class);
                    if(itemArrayCartList!=null  && chat.getOrderId().equals(orderId)){
                        itemArrayCartList.add(chat);
                    }
                }

                chatAdapterDelivery = new ChatAdapterDelivery(DeliveryChatActivity.this, itemArrayCartList);
                binding.rvItems.setLayoutManager(new GridLayoutManager(DeliveryChatActivity.this, 1));
                binding.rvItems.setAdapter(chatAdapterDelivery);

                binding.progressbarItems.setVisibility(View.GONE);

                if(itemArrayCartList.size()>0){
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


    @Override
    public void onClick(View view) {
        int id= view.getId();

        if(id==R.id.textView_send){

            message = binding.editTextTypeMessage.getText().toString();

            if(!message.equals("")) {


                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                Date date = new Date();
                String messageDate = dateFormat.format(date);
                String messageTime = timeFormat.format(date);

                String messageId = db_chat.push().getKey();

                ChatDelivery chat = new ChatDelivery(orderId, customerId, familyId, messageId,message,messageDate,messageTime,user_id,driverId);

                db_chat.child(messageId).setValue(chat);

                binding.editTextTypeMessage.setText("");

            }
        } else if(id==R.id.imageView_back){
            finish();
        }
    }
}