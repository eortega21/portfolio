package com.zybooks.projectlist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
/*ENHANCE TO USE ITS OWN CLASS AND CUSTOMIZE THE MESSAGE SENDING. less redundant.
this class is intended to send notification to the user.
The user needs to give permission first in order to user this type
of notification.
* */


public class notification extends Application {
    public static final String CHANNEL_ID = "channel1";

    @Override
    public void onCreate(){
        super.onCreate();
        createNotificationChannels();
    }

    /*notification channel will notify the user even when out of the app if it applies.
    the user must give permission first prior to using notification.
    * */
    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
            NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is channel");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);

    }


}
