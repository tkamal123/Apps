package com.orange.videoplayer11;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView v, v2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v= findViewById(R.id.videoView);
        v2=findViewById(R.id.videoView2);

        //resourse file
        v.setVideoPath("android.resource://"+ getPackageName()+"/"+R.raw.mountains);


        MediaController mc= new MediaController(this);
        mc.setAnchorView(v);
        v.setMediaController(mc);
        v.start();

        //from google
        Uri uri = Uri.parse("https://static.videezy.com/system/resources/previews/000/002/231/original/5226496.mp4");
        v2.setVideoURI(uri);
        MediaController mc2 = new MediaController(this);
        mc2.setAnchorView(v2);
        v2.setMediaController(mc2);
        v2.start();


    }
}