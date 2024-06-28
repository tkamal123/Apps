package com.trisha.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText password, username, email;
    Button signUp;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;
    FirebaseUser user;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference reference =firebaseFirestore.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        password = findViewById(R.id.password_create);
        username = findViewById(R.id.username_create_ET);
        signUp = findViewById(R.id.acc_signUp_btn);
        email = findViewById(R.id.email_create);

        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //The user signed in

                }
                else {

                    // The user signed out


                }
            }
        };
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  password, username, email
               if(!TextUtils.isEmpty(password.getText().toString()) &&
                       !TextUtils.isEmpty(username.getText().toString()) &&
                       !TextUtils.isEmpty(email.getText().toString())
               ){
                   String name = username.getText().toString().trim();
                   String mail = email.getText().toString().trim();
                   String pass = password.getText().toString().trim();
                   CreateUserAccount(pass, name, mail);

               }
               else{
                   Toast.makeText(SignUpActivity.this, "Enter" +
                           "Details", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
    public void CreateUserAccount(String password, String username, String email){

        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(password)
        && !TextUtils.isEmpty(username)){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Successfully" +
                                "made an account", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}