package com.willy.will.setting.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.willy.will.setting.service.NotificationService;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

    PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;
    SharedPreferences sharedPreferences;
    String alarmState;
    private static final String TAG = AlarmReceiver.class.getSimpleName();
    Context context;



    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("ALARM", MODE_PRIVATE);
        alarmState = sharedPreferences.getString("AlarmState", "default");
        alarmReceiverCheck(context, intent);

    }

    private void alarmReceiverCheck(Context context, Intent intent){
        switch (alarmState){
            case "on":
                acquireCPUWakeLock(context, intent);
                Intent serviceIntent = new Intent(context, NotificationService.class);
                serviceIntent.putExtra("AlarmState", alarmState);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    context.startForegroundService(serviceIntent);
                } else {
                    context.startService(serviceIntent);
                }
                break;
            case "off":
                releaseCpuLock();
                Intent stopIntent = new Intent(context, NotificationService.class);
                stopIntent.putExtra("AlarmState", alarmState);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    context.startForegroundService(stopIntent);
                } else {
                    context.startService(stopIntent);
                }
                break;
        }
    }


    @SuppressLint("InvalidWakeLockTag")
    private void acquireCPUWakeLock(Context context, Intent intent) {
        if (wakeLock != null) {
            return;
        }
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");
        wakeLock.acquire();
    }

    private void releaseCpuLock() {
        Log.e("PushWakeLock", "Releasing cpu WakeLock = " + wakeLock);

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

}
