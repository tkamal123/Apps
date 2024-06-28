package com.trisha.frenchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button black, green , purple, red;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        black = findViewById(R.id.black);
        green = findViewById(R.id.green);
        purple = findViewById(R.id.purple);
        red = findViewById(R.id.red);

        black.setOnClickListener(this);
        green.setOnClickListener(this);
        purple.setOnClickListener(this);
        red.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.black){
            player(R.raw.black);
        }
       else if(id == R.id.green){
            player(R.raw.green);
        }
        else if(id == R.id.purple){
            player(R.raw.purple);
        }
        else if(id == R.id.red){
            player(R.raw.red);
        }

    }

    private void player(int i) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, i);
        mediaPlayer.start();
    }

}