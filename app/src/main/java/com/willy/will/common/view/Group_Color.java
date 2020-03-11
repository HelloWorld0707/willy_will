package com.willy.will.common.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.will.R;

public class Group_Color extends AppCompatActivity {
    Button color1,color2,color3,color4,color5,color6,color7,color8,color9,color10,color11,color12;
    int select1=0,select2=0,select3=0,select4=0,select5=0,select6=0,
            select7=0,select8=0,select9=0,select10=0,select11=0,select12=0;

    int choice=0;

    private Object view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_color);
        color1 = (Button)findViewById(R.id.color1);
        color2 = (Button)findViewById(R.id.color2);
        color3 = (Button)findViewById(R.id.color3);
        color4 = (Button)findViewById(R.id.color4);
        color5 = (Button)findViewById(R.id.color5);
        color6 = (Button)findViewById(R.id.color6);
        color7 = (Button)findViewById(R.id.color7);
        color8 = (Button)findViewById(R.id.color8);
        color9 = (Button)findViewById(R.id.color9);
        color10 = (Button)findViewById(R.id.color10);
        color11 = (Button)findViewById(R.id.colot11);
        color12 = (Button)findViewById(R.id.color12);
    }



    public void color1_checked(View view) {
        color1.setSelected(true);
        this.finish();
    }
    public void color2_checked(View view) {
        color2.setSelected(true);
        this.finish();
    }
    public void color3_checked(View view) {
        color3.setSelected(true);
        this.finish();
    }
    public void color4_checked(View view) {
        color4.setSelected(true);
        this.finish();
    }
    public void color5_checked(View view) {
        color5.setSelected(true);
        this.finish();
    }
    public void color6_checked(View view) {
        color6.setSelected(true);
        this.finish();
    }
    public void color7_checked(View view) {
        color7.setSelected(true);
        this.finish();
    }
    public void color8_checked(View view) {
        color8.setSelected(true);
        this.finish();
    }
    public void color9_checked(View view) {
        color9.setSelected(true);
        this.finish();
    }
    public void color10_checked(View view) {
        color10.setSelected(true);
        this.finish();
    }
    public void color11_checked(View view) {
        color11.setSelected(true);
        this.finish();
    }
    public void color12_checked(View view) {
        color12.setSelected(true);
        this.finish();
    }

}
