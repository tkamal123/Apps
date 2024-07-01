package com.trisha.language_translator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    Spinner spinner1, spinner2;
    Button translate;
    TextView answer;
    EditText edit;

    String[] from = {"from","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech", "Welsh", "Hindi", "Urdu"};
    String[] to = {"to","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech", "Welsh", "Hindi", "Urdu"};

    String languageCode, fromLanguageCode, toLanguageCode;

    public static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = findViewById(R.id.spinner1);
        spinner2= findViewById(R.id.spinner2);
        translate = findViewById(R.id.button);
        answer = findViewById(R.id.answer);
        edit = findViewById(R.id.editText);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = GetlanguageCode(from[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter adapter = new ArrayAdapter(this , R.layout.spinner_item, from);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = GetlanguageCode(to[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.spinner_item, to);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText("");
                if(edit.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter details", Toast.LENGTH_SHORT).show();
                }else if (fromLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this, "Pick from Language", Toast.LENGTH_SHORT).show();

                }else if(toLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this, "Pick to Language", Toast.LENGTH_SHORT).show();
                }else {
                    TranslateText(fromLanguageCode,toLanguageCode, edit.getText().toString());
                }
            }
        });



    }

    private void TranslateText(String fromLanguageCode, String toLanguageCode, String src) {
        answer.setText("Downloading language model");

        try {
            TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode).setTargetLanguage(toLanguageCode)
                    .build();


            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    answer.setText("Translating...");
                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            answer.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Cant Translate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to download the language", Toast.LENGTH_SHORT).show();
                }
            });
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String GetlanguageCode(String language) {
        String LanguageCode;
        switch (language) {
            case "English":
                LanguageCode = TranslateLanguage.ENGLISH;
                break;
            case "Afrikaans":
                LanguageCode = TranslateLanguage.AFRIKAANS;
                break;
            case "Arabic":
                LanguageCode = TranslateLanguage.ARABIC;
                break;
            case "Belarusian":
                LanguageCode = TranslateLanguage.BELARUSIAN;
                break;
            case "Bengali":
                LanguageCode = TranslateLanguage.BENGALI;
                break;
            case "Catalan":
                LanguageCode = TranslateLanguage.CATALAN;
                break;
            case "Czech":
                LanguageCode = TranslateLanguage.CZECH;
                break;
            case "Welsh":
                LanguageCode = TranslateLanguage.WELSH;
                break;
            case "Hindi":
                LanguageCode = TranslateLanguage.HINDI;
                break;
            case "Urdu":
                LanguageCode = TranslateLanguage.URDU;
                break;
            default:
                LanguageCode = "";
        }
        return LanguageCode;
    }
    }




