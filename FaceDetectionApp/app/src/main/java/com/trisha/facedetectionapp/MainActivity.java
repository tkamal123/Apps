package com.trisha.facedetectionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    ImageView imageView;
    TextView textView;

    public static final int REQUEST_CODE = 124;
    InputImage image;
    FaceDetector faceDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text1);
        btn = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        FirebaseApp.initializeApp(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFile();
            }
        });
        Toast.makeText(this, "APP IS STARTED", Toast.LENGTH_SHORT).show();
    }

    private void OpenFile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
          startActivityForResult(intent, REQUEST_CODE);

        }else{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        FaceDetectionProcess(bitmap);
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

    }

    private void FaceDetectionProcess(Bitmap bitmap) {
        textView.setText("Face detection in process");
        final StringBuilder builder = new StringBuilder();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        InputImage image1 = InputImage.fromBitmap(bitmap, 0);
        FaceDetectorOptions highAccuracy = new FaceDetectorOptions.Builder().setPerformanceMode(
                FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE
        ).setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL).enableTracking()
                .build();

        FaceDetector detector = FaceDetection.getClient(highAccuracy);
        Task<List<Face>> result = detector.process(image1);

        result.addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                if(faces.size()!=0){
                    if(faces.size() == 1){
                        builder.append(faces.size() + " Face Detected  \n");
                    }
                    else if (faces.size() > 1){
                        builder.append(faces.size() + " Face detect  \n");

                    }
                }
                for(Face face : faces){
                    int id = face.getTrackingId();
                    float rotY = face.getHeadEulerAngleY();
                    float rotZ = face.getHeadEulerAngleZ();
                    builder.append("1. Face Tracking id ["+id+"] \n");
                    builder.append("2. Rotating right head ["+String.format("%.2f",rotY)+"] \n");
                    builder.append("3. Rotating left head ["+String.format("%.2f",rotZ)+"] \n");

                    //Smiling prob
                    if(face.getSmilingProbability() > 0){
                        float smileProb = face.getSmilingProbability();
                        builder.append("The prob of smiling is ["
                                + String.format("%.2f", smileProb) + "]\n ");
                    }
                    if(face.getLeftEyeOpenProbability() > 0){
                        float leftEye = face.getLeftEyeOpenProbability();
                        builder.append("The prob of leftEye open is ["
                                + String.format("%.2f", leftEye) + "] \n ");
                    }
                    if(face.getRightEyeOpenProbability() > 0){
                        float rightEye = face.getRightEyeOpenProbability();
                        builder.append("The prob of rightEye open is ["
                                + String.format("%.2f", rightEye) + "] \n ");
                    }

                }
                ShowDetection("Face Detection ", builder, true);
            }
        });
        result.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StringBuilder builder1 = new StringBuilder();
                builder1.append("Sorry there is an error");
                ShowDetection("Face Detection ", builder, false);
            }
        });
    }

    private void ShowDetection(final String face_detection_, final StringBuilder builder, boolean b) {
        if(b == true) {
            textView.setText(null);
            textView.setMovementMethod(new ScrollingMovementMethod());
            if (builder.length() != 0) {
                textView.append(builder);
                if (face_detection_.substring(0, face_detection_.indexOf(' ')).equalsIgnoreCase("OCR")) {
                    textView.append(" \n (Hold Text To Copy It) ");
                } else {
                    textView.append("(Hold Text To Copy It)");

                }


                textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(face_detection_, builder);
                        clipboardManager.setPrimaryClip(clip);
                        return true;

                    }
                });
            } else {
                textView.append(face_detection_.substring(0, face_detection_.indexOf(' ')) + "Failed to find " +
                        "anything");
            }
        } else if(b == false){
            textView.setText(null);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.append(builder);
        }
    }

}