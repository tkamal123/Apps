package com.trisha.quadraticequationsolver;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.trisha.quadraticequationsolver.databinding.ActivityMainBinding;

public class MyEquation extends BaseObservable {
    String a;
    String b;
    String c;
    ActivityMainBinding amb;

    public MyEquation(ActivityMainBinding amb) {
        this.amb = amb;
    }

    public MyEquation() {
    }
    @Bindable
    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
    @Bindable
    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
    @Bindable
    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public void solveEquation(View v){
        int a = Integer.parseInt(getA());
        int b = Integer.parseInt(getB());
        int c = Integer.parseInt(getC());

        double d = (b*b) - (4*a*c);
        double x1, x2;
        if(d > 0){
            x1 = ( - b + Math.sqrt(d)) / (2*a);
            x2 = ( - b - Math.sqrt(d)) / (2*a);

            amb.tv.setText("x1= " + x1 + " ,x2= " + x2);
        }
        else if (d < 0){
            amb.tv.setText("No real solutions");

        }else {
            x1 = -b / (2*a);
            amb.tv.setText("x1= " + x1);
        }
    }
}
