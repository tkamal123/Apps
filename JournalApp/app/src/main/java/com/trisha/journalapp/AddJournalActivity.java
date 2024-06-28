package com.trisha.journalapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {
    private Button saveButton;
    private ImageView addPhotoBtn;
    private ProgressBar progressBar;
    private EditText titleEditText;
    private EditText thoughtsEditText;
    private ImageView imageView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cf = db.collection("Journal");

    StorageReference storage;

    // Firebase Auth : UserId and UserName

    private String currentUserId;
    private String currentUserName;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;
    FirebaseUser user;

    ActivityResultLauncher<String> launcher;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);


        progressBar = findViewById(R.id.post_progressBar);
        titleEditText = findViewById(R.id.post_title_et);
        thoughtsEditText = findViewById(R.id.post_description_et);
        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_journal_button);
        addPhotoBtn = findViewById(R.id.postCameraButton);

        storage = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageView.setImageURI(result);
                        imageUri = result;
                    }
                });
        if(user != null){
            currentUserId = user.getUid();
            currentUserName = user.getDisplayName();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveJournal();
            }
        });

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launcher.launch("image/*");

            }
        });
    }

    private void SaveJournal() {
        String title = titleEditText.getText().toString().trim();
        String thought = thoughtsEditText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
//                if(title != null && thought != null)
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thought)
                && imageUri != null) {
            final StorageReference filePath =
                    storage.child("journal_images")
                            .child("my_image_" + Timestamp.now().getSeconds());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                                       String imgUrl = uri.toString();
                                       Journal journal = new Journal();
                                       journal.setTitle(title);
                                       journal.setThoughts(thought);
                                       journal.setImageUrl(imgUrl);
                                       journal.setTimeAdded(new Timestamp(new Date()));
                                       journal.setUserName(currentUserName);
                                       journal.setUserId(currentUserId);

                                               cf.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                   @Override
                                                   public void onSuccess(DocumentReference documentReference) {
                                                             progressBar.setVisibility(View.INVISIBLE);
                                                             Intent i = new Intent(AddJournalActivity.this,
                                                                     JournalListActivity.class);
                                                             startActivity(i);
                                                             finish();
                                                   }
                                               }).addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       Toast.makeText(AddJournalActivity.this, 
                                                               "OPS Failed try again" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                   }
                                               })   ;
                     }
                 })  ;
                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(AddJournalActivity.this,
                                                       "OPS Failed try again" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })      ;

        }
        else{
                                       progressBar.setVisibility(View.INVISIBLE);
                                   }

    }
    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
    }
}