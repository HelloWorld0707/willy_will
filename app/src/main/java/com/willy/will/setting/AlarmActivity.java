package com.willy.will.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.willy.will.R;
import com.willy.will.receiver.AlarmReceiver;

public class AlarmActivity extends Activity {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Switch alarmSwitch = findViewById(R.id.alarm_switch);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);



        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                } else {
                    // The toggle is disabled
                    Log.d("switch", "check off: ");
                }
            }
        });
    }



    public void test2(View view) {
        //노티피케이션 관리 객체를 생성  Context.NOTIFICATION_SERVICE 은 사실 문자열
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //노티피케이션에 출력되는 부분
        //getApplicationContext()대신 this도 가능합니다.
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this);
        //옵션설정
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_btn_speak_now));
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentTitle("노티피케이션 제목");
        builder.setContentText("노티피케이션 내용");
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        //노티피케이션에 수행될 내용
        //Chrome에서 구글로 이동하는 intent
        //알림을 클릭하면 수행할 내용
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com"));
        TaskStackBuilder stackBuilder= TaskStackBuilder.create(this);
        stackBuilder.addParentStack(getClass());
        stackBuilder.addNextIntent(intent);
        //작업수행후 화면에 출력될 인텐트 설정
        PendingIntent resultPendingIntent=
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        //알림을 수행 - 1은 구분하기 위한 아이디이고 두번째 수행할 작업 설정
        manager.notify(1,builder.build());

    }

}
