package com.trisha.image2texapp;

import static android.Manifest.permission_group.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class ActivityScanner extends AppCompatActivity {

    private ImageView captureIV;
    private TextView resultTV;
    private Button snapbtn,detectBtn;
    private Bitmap imageBitmap;
    static  final int REQUEST_IMAGE_CAPTURE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        captureIV=findViewById(R.id.imageView2);
        resultTV=findViewById(R.id.tv2);
        snapbtn=findViewById(R.id.cap2);
        detectBtn=findViewById(R.id.cap3);
//

        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detectText();
            }
        });

        snapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()){
                    captureImage();
                }else{
                    requestPermission();
                }
            }
        });

    }

    private boolean checkPermission(){
        int camerapermission = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return camerapermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_CODE);
    }

    private void captureImage(){

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
                captureImage();
            }else{
                Toast.makeText(getApplicationContext(), "Permission denied !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            captureIV.setImageBitmap((imageBitmap));
        }
    }

    private void detectText() {
        InputImage image = InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {

                StringBuilder result = new StringBuilder();
                for(Text.TextBlock block: text.getTextBlocks()){
                    String blockText = block.getText();
                    Point[] blockCornerpoint = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for(Text.Line line : block.getLines()){
                        String lineText = line.getText();
                        Point[] lineCornerPoint = line.getCornerPoints();
                        Rect lineRect = line.getBoundingBox();
                        for(Text.Element element: line.getElements()){
                            String elementText = element.getText();
                            result.append(elementText);
                        }
                        resultTV.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to detect text From image ....!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
//
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.PackageManagerCompat;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.text.Text;
//import com.google.mlkit.vision.text.TextRecognition;
//import com.google.mlkit.vision.text.TextRecognizer;
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
//
//import java.nio.BufferUnderflowException;
//
//public class ActivityScanner extends AppCompatActivity {
//    private ImageView captureIV;
//    private TextView resultTV;
//    private Button snapbtn,detectBtn;
//    private Bitmap imageBitmap;
//    static  final int REQUEST_IMAGE_CAPTURE=1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scanner);
//        captureIV=findViewById(R.id.imageView2);
//        resultTV=findViewById(R.id.tv2);
//        snapbtn=findViewById(R.id.cap2);
//        detectBtn=findViewById(R.id.cap3);
//
//        detectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detectText();
//            }
//        });
//
//        snapbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(CheckPermission()) {
//                    SnapImage();
//                }
//             RequestPermission();
//            }
//        });
//    }
//
//    private boolean CheckPermission() {
//        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA_SERVICE);
//        return permission == PackageManager.PERMISSION_GRANTED;
//    }
//    private void RequestPermission() {
//        int PERMISSION_CODE = 200;
//        ActivityCompat.requestPermissions(this, new String[] {
//                Manifest.permission.CAMERA
//        } ,PERMISSION_CODE );
//
//    }
//
//
//
//    public void SnapImage(){
//        Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(image.resolveActivity(getPackageManager()) != null){
//            startActivityForResult(image, REQUEST_IMAGE_CAPTURE);
//        }
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//  //      boolean cameraPermission = false;
//        if(grantResults.length > 0) {
//            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
//            if (cameraPermission) {
//                Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
//                SnapImage();
//            } else{
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK){
//            Bundle extra = data.getExtras();
//            imageBitmap  = (Bitmap) extra.get("data");
//            captureIV.setImageBitmap((imageBitmap));
//        }
//    }
//
//
//
//
//    private void detectText() {
//        InputImage image = InputImage.fromBitmap(imageBitmap,0);
//        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
//            @Override
//            public void onSuccess(Text text) {
//
//                StringBuilder result = new StringBuilder();
//                for(Text.TextBlock block: text.getTextBlocks()){
//                    String blockText = block.getText();
//                    Point[] blockCornerpoint = block.getCornerPoints();
//                    Rect blockFrame = block.getBoundingBox();
//                    for(Text.Line line : block.getLines()){
//                        String lineText = line.getText();
//                        Point[] lineCornerPoint = line.getCornerPoints();
//                        Rect lineRect = line.getBoundingBox();
//                        for(Text.Element element: line.getElements()){
//                            String elementText = element.getText();
//                            result.append(elementText);
//                        }
//                        resultTV.setText(blockText);
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failed to detect text From image ....!"+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}