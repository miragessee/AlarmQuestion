package com.alarmquestions.miragessee.alarmquestions;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakan on 7.11.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "miragesseealarmquestions",
    TABLE_ALARMS = "alarm",
    KEY_ID = "id",
    KEY_ACTIVE = "active",
    KEY_TIME = "time",
    KEY_REPEAT = "repeat",
    KEY_DIFFICULTY = "difficulty",
    KEY_RINGTONE = "ringtone",
    KEY_VIBRATE = "vibrate";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ALARMS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ACTIVE + " INTEGER," + KEY_TIME + " VARCHAR," + KEY_REPEAT + " VARCHAR," + KEY_DIFFICULTY + " INTEGER," + KEY_RINGTONE + " VARCHAR," + KEY_VIBRATE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + TABLE_ALARMS);

        onCreate(db);
    }

    public void createAlarm(AlarmManager2 alarmManager2) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, alarmManager2.get_active());
        values.put(KEY_TIME, String.valueOf(alarmManager2.get_time()));
        values.put(KEY_REPEAT, alarmManager2.get_repeat());
        values.put(KEY_DIFFICULTY, alarmManager2.get_difficulty());
        values.put(KEY_RINGTONE, alarmManager2.get_ringtone());
        values.put(KEY_VIBRATE, alarmManager2.get_vibrate());

        db.insert(TABLE_ALARMS, null, values);
        db.close();
    }

    public AlarmManager2 getAlarmManager(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS, new String[] {KEY_ID, KEY_ACTIVE, KEY_TIME, KEY_REPEAT, KEY_DIFFICULTY, KEY_RINGTONE, KEY_VIBRATE},KEY_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        AlarmManager2 alarmManager2 = new AlarmManager2(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        db.close();
        cursor.close();
        return alarmManager2;
    }

    public void deleteAlarm(AlarmManager2 alarmManager2) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + "=?", new String[]{String.valueOf(alarmManager2.get_id())});
        db.close();
    }

    public void deleteAllAlarm(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ALARMS);
        db.close();
    }

    public int getAlarmsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public int updateAlarm(AlarmManager2 alarmManager2) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVE, alarmManager2.get_active());
        values.put(KEY_TIME, String.valueOf(alarmManager2.get_time()));
        values.put(KEY_REPEAT, alarmManager2.get_repeat());
        values.put(KEY_DIFFICULTY, alarmManager2.get_difficulty());
        values.put(KEY_RINGTONE, alarmManager2.get_ringtone());
        values.put(KEY_VIBRATE, alarmManager2.get_vibrate());

        int rowsAffected = db.update(TABLE_ALARMS, values, KEY_ID + "=?", new String[] {String.valueOf(alarmManager2.get_id())});
        db.close();
        return rowsAffected;
    }

    public List<AlarmManager2> getAllAlarms() {
        List<AlarmManager2> alarms = new ArrayList<AlarmManager2>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS +" ORDER BY "+ KEY_TIME, null);

        if(cursor.moveToFirst()){
            do{
                alarms.add(new AlarmManager2(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(4)), cursor.getString(3), cursor.getString(5), cursor.getString(2)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarms;
    }

    public List<AlarmManager2> getAllTimes() {
        List<AlarmManager2> alarms = new ArrayList<AlarmManager2>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS +" where active = 1 ", null);

        if(cursor.moveToFirst()){
            do{
                alarms.add(new AlarmManager2(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(4)), cursor.getString(3), cursor.getString(5), cursor.getString(2)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarms;
    }

    public int getTimesCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARMS +" where active = 1 ", null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }
}
