package com.alarmquestions.miragessee.alarmquestions;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Hakan on 29.11.2015.
 */
public class MyAlarmService extends Service {
    static MediaPlayer player;
    public static int kontrolx = -1;
    public DatabaseHandler dbHandler;
    public List<String> alarms = new ArrayList<>();
    public static SharedPreferences preferences;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        dbHandler = new DatabaseHandler(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        List<AlarmManager2> addableAlarms = dbHandler.getAllTimes();
        int alarmCount = dbHandler.getTimesCount();

        alarms.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        for (int i = 0; i < alarmCount; i++) {
            String temp = addableAlarms.get(i).get_repeat();
            String[] word = temp.split(",");
            for(int j = 0; j < word.length; j++)
            {
                if(dayOfTheWeek.equals(word[j]))
                {
                    alarms.add(addableAlarms.get(i).get_time());
                }
            }
            if(addableAlarms.get(i).get_repeat().equals("Her gün"))
                alarms.add(addableAlarms.get(i).get_time());
        }

        Calendar mcurrentTime = Calendar.getInstance();
        String hour = String.valueOf(mcurrentTime.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(mcurrentTime.get(Calendar.MINUTE));

        if(hour.length()==1) {
            hour = "0" + hour;
        }
        if(minute.length()==1) {
            minute = "0" + minute;
        }

        String saat;

        for(int i=0;i<alarms.size();i++)
        {
            saat = alarms.get(i);
            String[] words = saat.split(":");
            if(words[0].equals(hour) && words[1].equals(minute))
            {
                if(kontrolx == i) {
                    i++;
                }
                else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("kontrol",i);
                    editor.commit();
                    AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mgr.setStreamVolume(AudioManager.STREAM_RING, mgr.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);

                    if (addableAlarms.get(i).get_vibrate() == 1)
                    {
                        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(new long[] {
                                1000L, 200L, 200L, 200L
                        }, 0);
                    }
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(this, Uri.parse(addableAlarms.get(i).get_ringtone()));
                        player.setAudioStreamType(4);
                        player.setLooping(true);
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopSelf();
                    Intent startNewActivityOpen = new Intent(MyAlarmService.this, AlarmActivity.class);
                    startNewActivityOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startNewActivityOpen);
                }
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
