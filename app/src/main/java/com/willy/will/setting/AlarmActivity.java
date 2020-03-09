package com.willy.will.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.willy.will.R;
import com.willy.will.main.view.MainActivity;
import com.willy.will.receiver.AlarmReceiver;

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



        alarmSwitch.setChecked(sharedPreferences.getBoolean("AlarmState",false));
        setAlarm();
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("AlarmState", alarmSwitch.isChecked());
                editor.commit();
                setAlarm();
            }
        });


    }


    public void setAlarm(){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent1, 0);

        if(alarmSwitch.isChecked()){
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000,5000, alarmIntent);
        }else{
            alarmMgr.cancel(alarmIntent);
        }
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
