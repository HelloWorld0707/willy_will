package com.willy.will.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.LongDef;
import com.willy.will.receiver.AlarmReceiver;
import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);

        SharedPreferences sharedPreferences = context.getSharedPreferences("ALARM", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("AlarmState",false)){
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000,5000, alarmIntent);
        }else{
            alarmMgr.cancel(alarmIntent);
        }


            /*
            // Set the alarm to start at approximately 2:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 19);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(context, AlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
            //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            60 * 1000, alarmIntent);

             */


    }


}
