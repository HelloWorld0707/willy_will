package com.willy.will.setting.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.willy.will.R;
import com.willy.will.database.DBAccess;
import com.willy.will.main.view.MainActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationService extends Service {
    private static DBAccess dbHelper = null;
    private Resources resources;
    SQLiteDatabase db;
    private Calendar today;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        dbHelper = DBAccess.getDbHelper();
        db = dbHelper.getReadableDatabase();
        resources = getResources();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingI = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.notification_icon);
            String channelName = resources.getString(R.string.alarm_settings_title);
            String description = resources.getString(R.string.alarm_settings_msg);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channelId", channelName, importance);
            channel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        } else{
            builder.setSmallIcon(R.drawable.notification_icon);
        }

        List<String> list = alarmToDoItems();
        for(int i=0;i<list.size();i++){

            builder.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentTitle("오늘의 할일")
                    .setContentText(list.get(i))
                    .setContentIntent(pendingI);

            if (notificationManager != null) {
                notificationManager.notify(i, builder.build());
            }
        }
    }


    /** get itemList **/
    public ArrayList<String> alarmToDoItems() {

        today = Calendar.getInstance();
        String todayStr = dateFormat.format(today.getTime());
        ArrayList<String>  toDoItemList = new ArrayList<>();

        String selectQuery = "SELECT i.item_name " +
                "FROM _ITEM i, _CALENDAR c\n" +
                "WHERE i.item_id = c.item_id\n" +
                "AND c.calendar_date=\""+todayStr+"\"\n" +
                "AND i.done_date IS NULL;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        String itemName = null;

        if(cursor.moveToFirst()){
            do {
                itemName = cursor.getString(0);
                toDoItemList.add(itemName);
            }while (cursor.moveToNext());
        }

        return toDoItemList;
    }
    /*~ get itemList **/


}
