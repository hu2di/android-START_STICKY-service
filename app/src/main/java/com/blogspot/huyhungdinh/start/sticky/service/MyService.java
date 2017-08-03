package com.blogspot.huyhungdinh.start.sticky.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {

    private BatteryReceiver batteryReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("myLog", "Service: onCreate");

        //% BATTERY LISTENER
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myLog", "Service: onStartCommand");
        /*if (Build.VERSION.SDK_INT <= 19) {
            onTaskRemoved(intent);
        }*/
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("myLog", "Service: onTaskRemoved");

        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);

        /*if (Build.VERSION.SDK_INT <= 19) {
            Intent restart = new Intent(getApplicationContext(), this.getClass());
            restart.setPackage(getPackageName());
            startService(restart);
        }*/
        
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("myLog", "Service: onDestroy");
        unregisterReceiver(batteryReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
