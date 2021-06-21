package com.example.householderproject.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.householderproject.MainActivity;
import com.example.householderproject.R;
import com.example.householderproject.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";

    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages.length == 0) {

            return;

        }

        String contents = messages[0].getMessageBody();

        Date today = new Date();
        String receivedDate = format.format(today);

        //TODO Message data Insert of DataBase

        String location = checkSMSLocation(contents);
        String credit = checkSMSCredit(contents);
        String location1 = SAMSUNGcheckSMSLocation(contents);
        String credit1 = SAMSUNGcheckSMSCredit(contents);

        if(!(location.equals("") && credit.equals(""))) {
            DBHelper.insertSpentData(context, receivedDate, credit, "지출", "카드", location);
        } else if(!(location1.equals("") && credit1.equals(""))) {
            DBHelper.insertSpentData(context, receivedDate, credit1, "지출", "카드", location1);
        } else {
            return;
        }

        //TODO

        Intent sendIntent = new Intent(context, MainActivity.class);
        sendIntent.putExtra("date", receivedDate);
        sendIntent.putExtra("credit", credit);
        sendIntent.putExtra("location", location);

        sendIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntentWithParentStack(sendIntent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify_001");

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.main_app_icon);
        builder.setContentTitle(receivedDate + contents + "사용");
        builder.setContentText(contents);
        builder.setPriority(Notification.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID, "NOTIFICATION_CHNNEL_NAME", importance);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(NOTIFICATION_ID);

        }

        assert notificationManager != null;
        notificationManager.notify(0, builder.build());

    }

    private String SAMSUNGcheckSMSCredit(String contents) {

        String resultCredit = "";

        if (contents.contains("삼성") && contents.contains("승인")) {

            String[] credit = contents.split("미");

            for (String checkedCredit : credit) {

                resultCredit = checkedCredit;

            }

            int index = resultCredit.indexOf("원");
            resultCredit = resultCredit.substring(0, index);

        } else {

            return resultCredit;

        }

        Log.e("!", resultCredit);

        resultCredit = resultCredit.replaceAll("\n", "");

        return resultCredit;

    }

    private String SAMSUNGcheckSMSLocation(String contents) {

        String resultLocation = "";

        if (contents.contains("삼성") && contents.contains("승인")) {

            String[] location = contents.split("\n", 5);
            String[] location1 = location[3].split(" ", 3);

            return location1[2];

        } else {

            return resultLocation;

        }

    }

    private String checkSMSCredit(String contents) {

        String resultCredit = "";

        if (contents.contains("신한") && contents.contains("승인")) {

            String[] credit = contents.split("액\\)");

            for (String checkedCredit : credit) {

                resultCredit = checkedCredit;

            }

            int index = resultCredit.indexOf("원");
            resultCredit = resultCredit.substring(0, index);

        } else {

            return resultCredit;

        }

        resultCredit = resultCredit.replaceAll(",", "");

        return resultCredit;

    }

    private String checkSMSLocation(String contents) {

        String resultLocation = "";

        if (contents.contains("신한") && contents.contains("승인")) {

            String[] location = contents.split("원 ");

            for (String checkedLocation : location) {

                resultLocation = checkedLocation;

            }

            int index = resultLocation.indexOf("[");
            resultLocation = resultLocation.substring(0, index);

        } else {

            return resultLocation;

        }

        return resultLocation;

    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {

        Object[] objects = (Object[]) bundle.get("pdus");

        SmsMessage[] messages = new SmsMessage[objects.length];

        for (int i = 0; i < objects.length; i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objects[i], format);

            } else {

                messages[i] = SmsMessage.createFromPdu((byte[]) objects[i]);

            }

        }

        return messages;

    }



}
