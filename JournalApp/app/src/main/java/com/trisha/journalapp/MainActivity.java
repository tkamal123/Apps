//package com.trisha.journalapp;
//
//import  androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class MainActivity extends AppCompatActivity {
//    Button loginBtn, createAccountBtn;
//    private EditText emailEt, passEt;
//
//    // Firebase Auth
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private FirebaseUser currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//
//
//        createAccountBtn = findViewById(R.id.button);
//
//        createAccountBtn.setOnClickListener(v -> {
//            // Onclick()
//            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
//            startActivity(i);
//
//        });
//
//
//        // Login
//        loginBtn = findViewById(R.id.email_signin);
//        emailEt = findViewById(R.id.email);
//        passEt = findViewById(R.id.editText2);
//
//        // Firebase Authentication
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        loginBtn.setOnClickListener(v -> {
//
//                    loginEmailPassUser(
//                            emailEt.getText().toString().trim(),
//                            passEt.getText().toString().trim()
//                    );
//                }
//
//        );
//
//    }
//
//    private void loginEmailPassUser(
//            String email, String pwd
//    ) {
//        // Checking for empty texts
//        if (!TextUtils.isEmpty(email)
//                && !TextUtils.isEmpty(pwd)
//        ) {
//            firebaseAuth.signInWithEmailAndPassword(
//                    email,
//                    pwd
//            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                    Intent i = new Intent(MainActivity.this, JournalListActivity.class);
//                    startActivity(i);
//                }
//
//            });
//
//        }
//
//
//    }
//}
//

//<EditText
//        android:id="@+id/editText"
//                android:textColorHint="@color/black"
//                android:textColor="@color/black"
//                android:layout_width="match_parent"
//                android:layout_height="wrap_content"
//                android:textSize="32dp"
//                android:layout_marginTop="180dp"
//                android:hint="Enter Username"
//                app:layout_constraintEnd_toEndOf="parent"
//                app:layout_constraintTop_toTopOf="parent"></EditText>
package com.trisha.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button loginBtn, createAccountBtn;
    private EditText emailEt, passEt;

    // Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountBtn = findViewById(R.id.button);
        loginBtn = findViewById(R.id.email_signin);
        emailEt = findViewById(R.id.email);
        passEt = findViewById(R.id.editText2);

        // Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        createAccountBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim();
            String pwd = passEt.getText().toString().trim();
            loginEmailPassUser(email, pwd);
        });
    }

    private void loginEmailPassUser(String email, String pwd) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent i = new Intent(MainActivity.this, JournalListActivity.class);
                            startActivity(i);
                            finish(); // Finish MainActivity to prevent returning to it on back press
                        }
                    }).addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }
    }
}
