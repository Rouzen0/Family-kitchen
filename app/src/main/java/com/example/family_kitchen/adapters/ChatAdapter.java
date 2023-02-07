package com.example.family_kitchen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_kitchen.R;
import com.example.family_kitchen.model.Chat;
import com.example.family_kitchen.preference.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<Chat> list;
    private Context context;
    Chat item;
    private DatabaseReference db;

    private SharedPref pref;

    String name;

    String user_id;

    public ChatAdapter(Context ctx, ArrayList <Chat> list) {
        this.list = list;
        context=ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        item =list.get(position);

        holder.tv_message.setText(item.getMessage());
        holder.tv_date.setText(item.getMessageDate());
        holder.tv_time.setText(item.getMessageTime());


        db= FirebaseDatabase.getInstance().getReference("User");



        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item =list.get(position);
                user_id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


                name=dataSnapshot.child(item.getSenderId()).child("name").getValue(String.class);

                if(user_id.equals(item.getSenderId())){
                    holder.tv_user_name.setText("Me");
                }
                else{
                    holder.tv_user_name.setText(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_user_name;

        public TextView tv_message;

        public TextView tv_date;
        public TextView tv_time;

        public Button btn_rate_order;


        public ViewHolder(@NonNull View v) {
            super(v);

            tv_user_name = (TextView) v.findViewById(R.id.tv_username);
            tv_message = (TextView) v.findViewById(R.id.tv_message);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_time = (TextView) v.findViewById(R.id.tv_time);

        }
    }
}
