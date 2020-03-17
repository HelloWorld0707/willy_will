package com.willy.will.setting.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.willy.will.setting.receiver.AlarmReceiver;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class AlarmSet {
    private static final int ALARM_REQUEST_CODE = 1111;
    private static AlarmManager alarmManager = null;
    private static PendingIntent pendingIntent = null;
    private static Intent alarmIntent = null;
    private static Context mContext;
    private static Calendar calendar;




    public static void setAlarm(Context context){

        mContext = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("ALARM", MODE_PRIVATE);
        String alarmState = sharedPreferences.getString("AlarmState", "default");

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); //create AlarmManager
        alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context,ALARM_REQUEST_CODE, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        if(alarmState.equals("on")){
            onAlarm(mContext);
        } else{
            offAlarm(mContext);
        }

    }




    public static void  onAlarm(Context context){
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        //calendar.set(Calendar.MINUTE,42);

        alarmIntent.putExtra("AlarmState","on");

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public static void  offAlarm(Context context){
        alarmManager.cancel(pendingIntent);
        alarmIntent.putExtra("AlarmState","off");
        context.sendBroadcast(alarmIntent);
    }
}
