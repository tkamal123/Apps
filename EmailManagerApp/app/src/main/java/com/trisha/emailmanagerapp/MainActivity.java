package com.trisha.emailmanagerapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.trisha.emailmanagerapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.LongUnaryOperator;

public class MainActivity extends AppCompatActivity {
    private ClickHandler clickHandler;
    private ContactAdapter adapter;
    private ActivityMainBinding mb;
    private ArrayList<Contact> contacts;
    private ContactDatabase contactDatabase;
//
//    private ExecutorService executorService;
//    private Handler Handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mb = DataBindingUtil.setContentView(this, R.layout.activity_main);
        clickHandler= new ClickHandler(getApplicationContext());
        mb.setClickHandler(clickHandler);

        RecyclerView rv = mb.rv;
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        contacts = new ArrayList<>();
        adapter = new ContactAdapter(contacts);

        contactDatabase = Room.databaseBuilder(getApplicationContext(),
                ContactDatabase.class,
                "ContactDao").allowMainThreadQueries().build();
        loadData();
        rv.setAdapter(adapter);
new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Contact contact = contacts.get(viewHolder.getAdapterPosition());
        delete(contact);

    }
}).attachToRecyclerView(rv);
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            String name = data.getStringExtra("Name");
            String email = data.getStringExtra("EMAIL");
            Contact contact = new Contact(0,name,email);
            insert(contact);




        }

    }

    public void loadData(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
       Handler Handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                contacts.addAll(contactDatabase.getContactDAO().getAllContact());
            }
        });
        Handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.setContacts(contacts);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void insert(Contact contact){
        ExecutorService  executorService = Executors.newSingleThreadExecutor();
      Handler  Handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                    contactDatabase.getContactDAO().insert(contact);
                    contacts.add(contact);
            }
        });
        Handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });




    }
    public void delete (Contact contact){
        ExecutorService   executorService = Executors.newSingleThreadExecutor();
     Handler   Handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                contactDatabase.getContactDAO().delete(contact);
                contacts.remove(contact);
            }
        });
        Handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });


    }
    public class ClickHandler{
        Context context;

        public ClickHandler(Context context) {
            this.context = context;
        }
        public void onFab(View view){
            Intent i = new Intent(MainActivity.this, add_new_contact.class);
            startActivityForResult(i,1);
        }
    }
}