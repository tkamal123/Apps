package com.trisha.quadraticequationsolver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.trisha.quadraticequationsolver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mb = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MyEquation mq = new MyEquation(mb);
        mb.setNumbers(mq);
    }
}