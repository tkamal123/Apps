package com.trisha.emailmanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.trisha.emailmanagerapp.databinding.ActivityAddNewContactBinding;

public class add_new_contact extends AppCompatActivity {

    ActivityAddNewContactBinding cmb;
    MyClickHandler handler;
    Contact contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        handler = new MyClickHandler(this);
        contact = new Contact();
        cmb = DataBindingUtil.setContentView(this,R.layout.activity_add_new_contact);
        cmb.setContact(contact);
        cmb.setHandler(handler);

    }


    public class MyClickHandler {
        Context context;

        public MyClickHandler(Context context) {
            this.context = context;
        }

        public void OnSubmit(View view) {
            if (contact.getName() == null) {
                Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();

            } else {
                Intent i = new Intent();
                i.putExtra("Name", contact.getName());
                i.putExtra("EMAIL", contact.getEmail());
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

}