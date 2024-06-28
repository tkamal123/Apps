package com.trisha.emailmanagerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.trisha.emailmanagerapp.databinding.ContentMainBinding;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    private ArrayList<Contact> contactArrayList;

    public ContactAdapter(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContentMainBinding cmb = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.content_main,
                parent,
                false);
        return new MyViewHolder(cmb);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact contact = contactArrayList.get(position);
        holder.cmb.setContact(contact);

    }

    @Override
    public int getItemCount() {
       if(contactArrayList != null){
           return contactArrayList.size();
       }
       else{
           return 0;
       }
    }

    public void setContacts(ArrayList<Contact> contacts){
        this.contactArrayList = contacts;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ContentMainBinding cmb;

        public MyViewHolder(@NonNull ContentMainBinding cmb) {
            super(cmb.getRoot());
            this.cmb = cmb;
        }
    }
}
