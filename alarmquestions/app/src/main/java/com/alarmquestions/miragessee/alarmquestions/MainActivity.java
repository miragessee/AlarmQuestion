package com.alarmquestions.miragessee.alarmquestions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    ArrayAdapter<AlarmManager2> adapterx;
    public List<AlarmManager2> alarms = new ArrayList<AlarmManager2>();
    public static TextView empty;
    public static ListView list;
    public DatabaseHandler dbHandler;
    public static int pressx = 0;
    private BroadcastReceiver mReceiver = null;
    public static final String BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mReceiver = new SampleBootReceiver();
        IntentFilter intentFilter = new IntentFilter(BOOT);
        registerReceiver( mReceiver , intentFilter);
        dbHandler = new DatabaseHandler(getApplicationContext());
        empty = (TextView) findViewById(R.id.empty);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PropertiesActivity.positionx = position;
                pressx = 1;
                PropertiesActivity.updatex = 1;

                Intent startNewActivityOpen = new Intent(MainActivity.this, PropertiesActivity.class);
                startActivityForResult(startNewActivityOpen, 0);

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                refresh();
                                dbHandler.deleteAlarm(alarms.get(position));
                                alarms.remove(position);
                                refresh();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.delete));
                builder.setMessage(getString(R.string.deletethisalarm)).setPositiveButton(getString(R.string.yes), dialogClickListener).setNegativeButton(getString(R.string.no), dialogClickListener).show();
                return false;
            }
        });

        refresh();

        Intent myIntent = new Intent(MainActivity.this, MyAlarmService.class);
        pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 50000, pendingIntent);
    }

    public void populateList() {
        adapterx = new AlarmManagerListAdapter(MainActivity.this, alarms, dbHandler);
        list.setAdapter(adapterx);
    }

    public void refresh() {
        MainActivity.list.setAdapter(null);
        alarms.clear();
        List<AlarmManager2> addableAlarms = dbHandler.getAllAlarms();
        int alarmCount = dbHandler.getAlarmsCount();

        if (alarmCount > 0)
            empty.setText("");
        else
            empty.setText(getString(R.string.no_alarms));

        for (int i = 0; i < alarmCount; i++) {
            alarms.add(addableAlarms.get(i));
        }

        if (!addableAlarms.isEmpty()) {
            populateList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_new) {
            PropertiesActivity.updatex = 0;
            Intent startNewActivityOpen = new Intent(MainActivity.this, PropertiesActivity.class);
            startActivityForResult(startNewActivityOpen, 0);
        }
        if (id == R.id.menu_item_rate) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
            return true;
        }

        if (id == R.id.menu_item_deleteAll) {
            dbHandler.deleteAllAlarm();
            list.setAdapter(null);
            empty.setText(R.string.no_alarms);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
