package com.willy.will.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.willy.will.R;

public class AlarmActivity extends Activity {
    Switch alarmSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmSwitch = findViewById(R.id.alarm_switch);
        sharedPreferences = getSharedPreferences("ALARM", MODE_PRIVATE);
        editor = sharedPreferences.edit();



        alarmSwitch.setChecked(sharedPreferences.getString("AlarmState","default").equals("off")?false:true);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putString("AlarmState", alarmSwitch.isChecked()?"on":"off");
                editor.commit();
            }
        });
    }


    /** Back to MainActivity **/
    public void backToMain(View view) {
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        this.finish();
    }
    /*~ Back to MainActivity (Main View) */
}
