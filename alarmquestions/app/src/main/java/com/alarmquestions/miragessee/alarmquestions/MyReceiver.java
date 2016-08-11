package com.alarmquestions.miragessee.alarmquestions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Hakan on 14.12.2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        if (arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            PowerManager powerManager = (PowerManager) arg0.getSystemService(arg0.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
            wakeLock.acquire();
        } else if (arg1.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        }
    }
}