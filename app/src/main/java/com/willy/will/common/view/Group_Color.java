package com.willy.will.common.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.will.R;

public class Group_Color extends AppCompatActivity {
    private Button color1,color2,color3,color4,color5,color6,
            color7,color8,color9,color10,color11,color12;
    int select1=0,select2=0,select3=0,select4=0,select5=0,select6=0,
            select7=0,select8=0,select9=0,select10=0,select11=0,select12=0;

    int choice=0;

    private Object view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_color);
        color1 = findViewById(R.id.color1);
        color2 = findViewById(R.id.color2);
        color3 = findViewById(R.id.color3);
        color4 = findViewById(R.id.color4);
        color5 = findViewById(R.id.color5);
        color6 = findViewById(R.id.color6);
        color7 = findViewById(R.id.color7);
        color8 = findViewById(R.id.color8);
        color9 = findViewById(R.id.color9);
        color10 = findViewById(R.id.color10);
        color11 = findViewById(R.id.colot11);
        color12 = findViewById(R.id.color12);

    }
    public void color1_checked(View view) {
        Intent intent = new Intent(getApplicationContext(), GroupManagementActivity.class);
        intent.putExtra("color1", "color1"); // 입력폼에 적은 값 담아주기

        this.finish();
    }

    public void color2_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color2"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color3_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color3"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color4_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color4"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color5_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color5"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color6_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color6"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color7_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color7"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color8_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color8"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color9_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color9"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color10_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color10"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color11_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color11"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
    public void color12_checked(View view) {
        Intent intent = new Intent();
        intent.putExtra("comeback", "color12"); // 입력폼에 적은 값 담아주기
        setResult(RESULT_OK, intent); // 결과 값 설정
        this.finish();
    }
}
