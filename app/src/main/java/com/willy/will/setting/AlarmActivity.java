package com.willy.will.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.willy.will.R;

public class AlarmActivity extends Activity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        final Switch alarmSwitch = findViewById(R.id.alarm_switch);

        sharedPreferences = getSharedPreferences("alarmIsChecked", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //editor.putBoolean("switchIsChecked",alarmSwitch.isChecked());
        Boolean isCheck = sharedPreferences.getBoolean("switchIsChecked",false);

        if(isCheck==null || isCheck == false){
            alarmSwitch.setChecked(false);
        }else{
            alarmSwitch.setChecked(true);
        }
        editor.putBoolean("switchIsChecked",alarmSwitch.isChecked());
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("switchIsChecked",alarmSwitch.isChecked());
            }
        });
        editor.commit();
        //Log.d("SharedPreferences", sharedPreferences.get);
    }
}
