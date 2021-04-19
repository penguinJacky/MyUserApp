package com.example.myuserapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuserapp.R;
import com.example.myuserapp.bean.MyUser;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<MyUser> userArrayList;

    public MyRecyclerViewAdapter(ArrayList<MyUser> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyUser user = userArrayList.get(position);
        holder.avatarImg.setImageResource(user.getAvatar());
        holder.nameTv.setText(user.getName());
        holder.phoneTv.setText(user.getPhone());
        holder.addressTv.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImg;
        TextView nameTv;
        TextView phoneTv;
        TextView addressTv;

        public MyViewHolder(@NonNull View v) {
            super(v);
            avatarImg = v.findViewById(R.id.avatar_img);
            nameTv = v.findViewById(R.id.name_tv);
            phoneTv = v.findViewById(R.id.phone_tv);
            addressTv = v.findViewById(R.id.address_tv);
        }
    }
}


