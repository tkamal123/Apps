package com.trisha.ocr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.translation.Translator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    Button chooseImage, readText;
    TextView result;

    InputImage inputImage;
    TextRecognizer textRecognizer;
    TextToSpeech textToSpeech;
    private static final int PICK_IMAGE =123 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        image = findViewById(R.id.imageView);
        chooseImage = findViewById(R.id.chooseImage);
        readText = findViewById(R.id.readText);
        result = findViewById(R.id.result);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.US);

                }
            }
        });

        readText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textToSpeech.speak(result.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);




            }
        });

    }

    private void OpenGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            if(data!= null){
                byte[] byteArray = new byte[0];
                String filePath = null;
                try{
                    inputImage = InputImage.fromFilePath(this, data.getData());
                    Bitmap resultUri = inputImage.getBitmapInternal();
                    Glide.with(MainActivity.this).load(resultUri).into(image);


                    Task<Text> result = textRecognizer.process(inputImage)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text text) {
                                    processTextBlock(text);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                                }
                            });
                }catch(IOException e){
                    e.printStackTrace();

                }


            }
        }
    }


    private void processTextBlock(Text text) {
        String resultText = text.getText();
        for(Text.TextBlock block : text.getTextBlocks()) {
            String blockText = block.getText();
            result.append("\n");
            Point[] blockPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();

            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                result.append("\n");
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    result.append(" ");
                    String elementText = element.getText();
                    result.append(elementText);
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }


            }
        }
    }

    @Override
    protected void onPause() {
        if(!textToSpeech.isSpeaking()){
            super.onPause();
        }
    }


}
