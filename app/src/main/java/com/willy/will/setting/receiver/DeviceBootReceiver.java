package com.willy.will.setting.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.willy.will.setting.controller.AlarmSet;

import static android.content.Context.MODE_PRIVATE;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("ALARM", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String alarmState = sharedPreferences.getString("AlarmState", "default");
        if (alarmState.equals("on")) {
            AlarmSet.onAlarm(context);
        } else if (alarmState.equals("off")) {
            AlarmSet.offAlarm(context);
        }
    }

}