package com.alarmquestions.miragessee.alarmquestions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {

    public static int kontrol = -1;
    private DatabaseHandler dbHandler;
    private List<Integer> alarms = new ArrayList<>();
    private List<String> sorularTR = new ArrayList<>();
    private List<String> sorularEN = new ArrayList<>();
    private List<String> cevaplarTR = new ArrayList<>();
    private List<String> cevaplarEN = new ArrayList<>();
    private String soru = "";
    private int cevap = 0;
    private int cevap2 = 0;
    private String cevapx;
    TextView textView3;
    private BroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView3 = (TextView) findViewById(R.id.textView3);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        dbHandler = new DatabaseHandler(getApplicationContext());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(Locale.getDefault().getLanguage().equals("tr"))
        {
            setSorularTR();
        }
        else
        {
            setSorularEN();
        }

        List<AlarmManager2> addableAlarms = dbHandler.getAllTimes();
        int alarmCount = dbHandler.getTimesCount();

        alarms.clear();

        for (int i = 0; i < alarmCount; i++) {
            alarms.add(addableAlarms.get(i).get_difficulty());
        }

        if(alarms.get(MyAlarmService.preferences.getInt("kontrol",-1)) == 0)
        {
            textView3.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            Random r = new Random();
            for(int i=0;i<4;i++)
            {
                int a=r.nextInt(10);
                soru += a;
                if(i!=3)
                    soru +=" + ";
                cevap += a;
            }
            textView.setText(soru);
        }
        else if(alarms.get(MyAlarmService.preferences.getInt("kontrol",-1)) == 1)
        {
            Random r = new Random();
            for(int i=0;i<4;i++)
            {
                // 0 + 1 + 2 + 3
                int a=r.nextInt(10);
                soru += a;
                if(i == 0)
                {
                    soru +=" + ";
                    cevap += a;
                }
                else if(i == 1)
                {
                    soru +=" + ";
                    cevap += a;
                }
                else if(i == 2)
                {
                    soru +=" + ";
                    cevap += a;
                }
                else if(i == 3)
                {
                    cevap += a;
                }
            }
            textView.setText(soru);
            textView3.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
        }
        else
        {
            Random r = new Random();
            for(int i=0;i<4;i++)
            {
                // ( 0 + 1 ) * ( 2 + 3 )
                // 0 + 1 * 2 + 3
                int a=r.nextInt(10);

                if(i == 0)
                {
                    soru += "( ";
                    soru += a;
                    soru +=" + ";
                    cevap += a;
                }
                else if(i == 1)
                {
                    soru += a;
                    soru += " )";
                    cevap += a;
                    soru +=" * ";
                }
                else if(i == 2)
                {
                    soru += "( ";
                    soru += a;
                    soru +=" + ";
                    cevap2 += a;
                }
                else if(i == 3)
                {
                    soru += a;
                    cevap2 += a;
                    soru += " )";
                }
            }
            cevap *= cevap2;
            textView.setText(soru);
            textView3.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
        }

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(editText.getText().toString()) == cevap)
                {
                    if(textView3.getVisibility() == View.VISIBLE)
                    {
                        if(radioButton.isChecked())
                        {
                            if(cevapx == "1")
                            {
                                Intent myIntent = new Intent(getBaseContext(), MyAlarmService.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, 0);
                                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);
                                MyAlarmService.kontrolx = MyAlarmService.preferences.getInt("kontrol", -1);
                                MyAlarmService.player.stop();
                                finish();
                                System.exit(0);
                            }
                        }
                        else if(radioButton2.isChecked())
                        {
                            if(cevapx == "0")
                            {
                                Intent myIntent = new Intent(getBaseContext(), MyAlarmService.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, 0);
                                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);
                                MyAlarmService.kontrolx = MyAlarmService.preferences.getInt("kontrol", -1);
                                MyAlarmService.player.stop();
                                finish();
                                System.exit(0);
                            }
                        }
                    }
                    else
                    {
                        Intent myIntent = new Intent(getBaseContext(), MyAlarmService.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, 0);
                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                        MyAlarmService.kontrolx = MyAlarmService.preferences.getInt("kontrol", -1);
                        MyAlarmService.player.stop();
                        finish();
                        System.exit(0);
                    }
                }
            }
        });
    }

    private void setSorularTR(){
        sorularTR.clear();
        cevaplarTR.clear();
        sorularTR.add("Telefonun açık mı?");
        cevaplarTR.add("1");
        sorularTR.add("Bir insanın 2 ayağı vardır.");
        cevaplarTR.add("1");
        sorularTR.add("Bir tavşanın 2 ayağı vardır.");
        cevaplarTR.add("0");
        sorularTR.add("Bu uygulamanın adı Alarms Questions dur.");
        cevaplarTR.add("1");
        sorularTR.add("Penguenler uçabilir.");
        cevaplarTR.add("0");

        Random rnd = new Random();
        int a = rnd.nextInt(sorularTR.size());
        textView3.setText(sorularTR.get(a));
        cevapx = cevaplarTR.get(a);
    }

    private void setSorularEN(){
        sorularEN.clear();
        cevaplarEN.clear();
        sorularEN.add("The phone is open?");
        cevaplarEN.add("1");
        sorularEN.add("A human's has got 2 foot.");
        cevaplarEN.add("1");
        sorularEN.add("A rabbit's has got 2 foot.");
        cevaplarEN.add("0");
        sorularEN.add("This application's name is Alarms Questions.");
        cevaplarEN.add("1");
        sorularEN.add("Penguins can fly.");
        cevaplarEN.add("0");

        Random rnd = new Random();
        int a = rnd.nextInt(sorularEN.size());
        textView3.setText(sorularEN.get(a));
        cevapx = cevaplarEN.get(a);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                }
                return true;
            case KeyEvent.KEYCODE_POWER:
                if (action == KeyEvent.KEYCODE_POWER) {
                }
                return true;
            case KeyEvent.KEYCODE_MUTE:
                if (action == KeyEvent.KEYCODE_MUTE) {
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                if (action == KeyEvent.KEYCODE_BACK) {
                }
                return true;
            case KeyEvent.KEYCODE_HOME:
                if (action == KeyEvent.KEYCODE_HOME) {
                }
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (action == KeyEvent.KEYCODE_MENU) {
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        intent = new Intent(this, AlarmActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int keycode = event.getKeyCode();
        final int action = event.getAction();
        if (keycode == KeyEvent.KEYCODE_MENU) {
            return true; // consume the key press
        }
        return super.dispatchKeyEvent(event);
    }
}
