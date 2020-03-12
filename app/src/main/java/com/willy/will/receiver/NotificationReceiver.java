package com.willy.will.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.willy.will.R;
import com.willy.will.database.DBAccess;
import com.willy.will.detail.controller.DetailController;
import com.willy.will.detail.view.DetailActivity;
import com.willy.will.main.view.MainActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.willy.will.main.view.MainActivity.dbHelper;

public class NotificationReceiver extends BroadcastReceiver {
    private static DBAccess dbHelper = null;
    private DetailController detailCtrl;
    private Resources resources;
    SQLiteDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {

        resources = context.getResources();
        dbHelper = new DBAccess(context, resources.getString(R.string.database_file_name), null, resources.getInteger(R.integer.database_version));
        db = dbHelper.getReadableDatabase();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingI = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelId");


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
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
                    .setWhen(System.currentTimeMillis())
                    .setTicker("{Time to watch some cool stuff!}")
                    .setContentTitle("오늘의 할일")
                    .setContentText(list.get(i))
                    .setContentInfo("INFO")
                    .setContentIntent(pendingI);

            if (notificationManager != null) {
                notificationManager.notify(i, builder.build());
            }
        }
    }
    /** get itemList **/
    public ArrayList<String> alarmToDoItems() {

        LocalDate today = LocalDate.now();
        String monthValStr = null;
        int monthVal = today.getMonthValue();
        if(monthVal<10){
            monthValStr = "0" + monthVal;
        }else{
            monthValStr = monthVal+"";
        }

        String todayStr = today.getYear() + "-" + monthValStr+ "-" + today.getDayOfMonth();
        ArrayList<String>  toDoItemList = new ArrayList<>();
        //todayStr = "2020-03-12";

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
