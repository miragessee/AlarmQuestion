package com.alarmquestions.miragessee.alarmquestions;

import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.DialogInterface.*;

public class PropertiesActivity extends AppCompatActivity {
    CustomList2 adapter;
    ArrayAdapter<AlarmManager2> adapterx;
    List<AlarmManager2> alarms = new ArrayList<AlarmManager2>();
    public static int positionx;
    public static int updatex = 0;
    static ArrayList<String> dayList = new ArrayList<String>();
    static Uri urix;
    private String array_spinner[];
    private String array_spinner2[];
    private String array_spinner3[];
    private boolean checkedItems[];
    static CheckBox checkBox;
    static CheckBox checkBox2;
    ListView list2;
    DatabaseHandler dbHandler;
    String[] difs;
    String selecteds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);
        dbHandler = new DatabaseHandler(getApplicationContext());
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        list2 = (ListView) findViewById(R.id.list2);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        difs = new String[4];
        difs[0] = getString(R.string.settime);
        difs[1] = getString(R.string.repeat);
        difs[2] = getString(R.string.difficulty);
        difs[3] = getString(R.string.ringtone);
        array_spinner = new String[7];
        selecteds = new String[4];
        checkedItems = new boolean[7];

        array_spinner[0] = getString(R.string.monday);
        array_spinner[1] = getString(R.string.tuesday);
        array_spinner[2] = getString(R.string.wednesday);
        array_spinner[3] = getString(R.string.thursday);
        array_spinner[4] = getString(R.string.friday);
        array_spinner[5] = getString(R.string.saturday);
        array_spinner[6] = getString(R.string.sunday);

        array_spinner2 = new String[3];
        array_spinner2[0] = getString(R.string.easy);
        array_spinner2[1] = getString(R.string.medium);
        array_spinner2[2] = getString(R.string.hard);

        ArrayList<String> arrayList = new ArrayList();
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            arrayList.add(title);
        }

        array_spinner3 = new String[arrayList.size() + 1];
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        urix = ringtoneUri;
        Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), ringtoneUri);
        String name = ringtone.getTitle(getBaseContext());
        array_spinner3[0] = name;

        for (int i = 0; i < arrayList.size(); i++) {
            array_spinner3[i + 1] = arrayList.get(i);
        }

        if (updatex == 0) {
            selecteds[0] = df.format(c.getTime());
            selecteds[1] = getString(R.string.everday);
            selecteds[2] = getString(R.string.easy);
            selecteds[3] = name;
            adapter = new CustomList2(PropertiesActivity.this, difs, selecteds);
            list2 = (ListView) findViewById(R.id.list2);
            list2.setAdapter(adapter);
            list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(PropertiesActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                selecteds[0] = selectedHour + ":" + selectedMinute;
                                DateFormat sdf = new SimpleDateFormat("HH:mm");
                                Date date = null;
                                try {
                                    date = sdf.parse(selecteds[0]);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String dd = sdf.format(date);
                                selecteds[0] = dd;
                                adapter.notifyDataSetChanged();
                            }
                        }, hour, minute, true);
                        mTimePicker.setTitle(getString(R.string.selecttime));
                        mTimePicker.show();
                    }
                    if (position == 1) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PropertiesActivity.this);
                        AlertDialog.Builder builder = alertDialog;
                        builder.setTitle(getString(R.string.repeat));
                        builder.setMultiChoiceItems(array_spinner, checkedItems, new OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    checkedItems[which] = true;
                                } else if (dayList.contains(array_spinner[which])) {
                                    checkedItems[which] = false;
                                }
                            }
                        });
                        builder.setPositiveButton(getString(R.string.ok), new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selections = "";
                                for (int i = 0; i < checkedItems.length; i++) {
                                    if (checkedItems[i]) {
                                        dayList.add(array_spinner[i]);
                                    } else {
                                        dayList.remove(array_spinner[i]);
                                    }
                                }

                                for (String ms : dayList) {
                                    if (dayList.size() < 7) {
                                        if (!selections.equals("")) {
                                            selections += "," + ms;
                                        } else {
                                            selections += ms;
                                        }
                                    } else {
                                        selections = getString(R.string.everday);
                                    }
                                }
                                if (!selections.equals("")) {
                                    selecteds[1] = selections;
                                    adapter.notifyDataSetChanged();
                                } else {
                                    selecteds[1] = getString(R.string.everday);
                                    adapter.notifyDataSetChanged();
                                }

                                dayList.clear();
                            }
                        });
                        alertDialog.show();
                    }
                    if (position == 2) {
                        new AlertDialog.Builder(PropertiesActivity.this).setSingleChoiceItems(array_spinner2, 0, null).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition == 0) {
                                    selecteds[2] = getString(R.string.easy);
                                    adapter.notifyDataSetChanged();
                                } else if (selectedPosition == 1) {
                                    selecteds[2] = getString(R.string.medium);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    selecteds[2] = getString(R.string.hard);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).show();
                    }

                    if (position == 3) {
                        final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        startActivityForResult(ringtone, 0);
                    }
                }
            });
        }

        if (MainActivity.pressx == 1) {
            List<AlarmManager2> addableAlarms = dbHandler.getAllAlarms();
            int alarmCount = dbHandler.getAlarmsCount();

            for (int i = 0; i < alarmCount; i++) {
                alarms.add(addableAlarms.get(i));
            }

            AlarmManager2 currentAlarm = alarms.get(positionx);

            if (currentAlarm.get_active() == 0) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
            selecteds[0] = currentAlarm.get_time();
            selecteds[1] = currentAlarm.get_repeat();
            if (currentAlarm.get_difficulty() == 0) {
                selecteds[2] = getString(R.string.easy);
            } else if (currentAlarm.get_difficulty() == 1) {
                selecteds[2] = getString(R.string.medium);
            } else {
                selecteds[2] = getString(R.string.hard);
            }
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(currentAlarm.get_ringtone()));
            String namex = r.getTitle(this);
            selecteds[3] = namex;
            if (currentAlarm.get_vibrate() == 0) {
                checkBox2.setChecked(false);
            } else {
                checkBox2.setChecked(true);
            }

            adapter = new CustomList2(PropertiesActivity.this, difs, selecteds);
            list2 = (ListView) findViewById(R.id.list2);
            list2.setAdapter(adapter);
            list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (position == 0) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(PropertiesActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                selecteds[0] = selectedHour + ":" + selectedMinute;
                                DateFormat sdf = new SimpleDateFormat("HH:mm");
                                Date date = null;
                                try {
                                    date = sdf.parse(selecteds[0]);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String dd = sdf.format(date);
                                selecteds[0] = dd;
                                adapter.notifyDataSetChanged();
                            }
                        }, hour, minute, true);
                        mTimePicker.setTitle(getString(R.string.selecttime));
                        mTimePicker.show();
                    }
                    if (position == 1) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PropertiesActivity.this);
                        AlertDialog.Builder builder = alertDialog;
                        builder.setTitle(getString(R.string.repeat));
                        builder.setMultiChoiceItems(array_spinner, checkedItems, new OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    checkedItems[which] = true;
                                } else if (dayList.contains(array_spinner[which])) {
                                    checkedItems[which] = false;
                                }
                            }
                        });
                        builder.setPositiveButton(getString(R.string.ok), new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selections = "";
                                for (int i = 0; i < checkedItems.length; i++) {
                                    if (checkedItems[i]) {
                                        dayList.add(array_spinner[i]);
                                    } else {
                                        dayList.remove(array_spinner[i]);
                                    }
                                }

                                for (String ms : dayList) {
                                    if (dayList.size() < 7) {
                                        if (!selections.equals("")) {
                                            selections += "," + ms;
                                        } else {
                                            selections += ms;
                                        }
                                    } else {
                                        selections = getString(R.string.everday);
                                    }
                                }
                                if (!selections.equals("")) {
                                    selecteds[1] = selections;
                                    adapter.notifyDataSetChanged();
                                } else {
                                    selecteds[1] = getString(R.string.everday);
                                    adapter.notifyDataSetChanged();
                                }

                                dayList.clear();
                            }
                        });
                        alertDialog.show();
                    }
                    if (position == 2) {
                        new AlertDialog.Builder(PropertiesActivity.this).setSingleChoiceItems(array_spinner2, 0, null).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition == 0) {
                                    selecteds[2] = getString(R.string.easy);
                                    adapter.notifyDataSetChanged();
                                } else if (selectedPosition == 1) {
                                    selecteds[2] = getString(R.string.medium);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    selecteds[2] = getString(R.string.hard);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                                .show();
                    }

                    if (position == 3) {
                        final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        startActivityForResult(ringtone, 0);
                    }
                }
            });

            MainActivity.pressx = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            final Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            final Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            String temp = String.valueOf(uri);
            if (temp.length() == 4) {
                Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                urix = ringtoneUri;
            } else {
                urix = uri;
            }

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), urix);
            String namex = ringtone.getTitle(this);
            selecteds[3] = namex;
            adapter.notifyDataSetChanged();
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

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.menu_item_save) {
            int cb;
            if (checkBox.isChecked())
                cb = 1;
            else
                cb = 0;
            int cb2;
            if (checkBox2.isChecked())
                cb2 = 1;
            else
                cb2 = 0;

            int diff;
            if (selecteds[2].equals(getString(R.string.easy))) {
                diff = 0;
            } else if (selecteds[2].equals(getString(R.string.medium))) {
                diff = 1;
            } else {
                diff = 2;
            }

            DateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = sdf.parse(selecteds[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dd = sdf.format(date);

            if (updatex == 1) {
                alarms.get(positionx).set_active(cb);
                alarms.get(positionx).set_vibrate(cb2);
                alarms.get(positionx).set_difficulty(diff);
                alarms.get(positionx).set_time(dd);
                alarms.get(positionx).set_ringtone(urix.toString());
                alarms.get(positionx).set_repeat(selecteds[1]);
                dbHandler.updateAlarm(alarms.get(positionx));
                updatex = 0;
                refresh();
                NavUtils.navigateUpFromSameTask(this);
            } else if (updatex == 0) {
                AlarmManager2 alarmManager2 = new AlarmManager2(dbHandler.getAlarmsCount(), cb, cb2, diff, selecteds[1], urix.toString(), dd);
                dbHandler.createAlarm(alarmManager2);
                MainActivity.empty.setText("");
                refresh();
                NavUtils.navigateUpFromSameTask(this);
            }
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
        if (id == R.id.menu_item_delete) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            if (alarms.size() > 0)
                            {
                                dbHandler.deleteAlarm(alarms.get(positionx));
                                alarms.remove(positionx);
                                MainActivity.list.setAdapter(null);
                                refresh();
                            }
                            NavUtils.navigateUpFromSameTask(PropertiesActivity.this);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(PropertiesActivity.this);
            builder.setTitle(getString(R.string.delete));
            builder.setMessage(getString(R.string.deletethisalarm)).setPositiveButton(getString(R.string.yes), dialogClickListener).setNegativeButton(getString(R.string.no), dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateList() {
        adapterx = new AlarmManagerListAdapter(PropertiesActivity.this, alarms, dbHandler);
        MainActivity.list.setAdapter(adapterx);
    }

    public void refresh() {
        MainActivity.list.setAdapter(null);
        alarms.clear();
        List<AlarmManager2> addableAlarms = dbHandler.getAllAlarms();
        int alarmCount = dbHandler.getAlarmsCount();

        for (int i = 0; i < alarmCount; i++) {
            alarms.add(addableAlarms.get(i));
        }

        if (!addableAlarms.isEmpty()) {
            populateList();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem plus = menu.findItem(R.id.menu_item_new);
        plus.setVisible(false);
        MenuItem save = menu.findItem(R.id.menu_item_save);
        save.setVisible(true);
        MenuItem delete = menu.findItem(R.id.menu_item_delete);
        delete.setVisible(true);
        MenuItem deleteAll = menu.findItem(R.id.menu_item_deleteAll);
        deleteAll.setVisible(false);
        return true;
    }
}
