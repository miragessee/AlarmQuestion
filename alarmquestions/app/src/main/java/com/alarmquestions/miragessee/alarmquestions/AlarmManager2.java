package com.alarmquestions.miragessee.alarmquestions;

/**
 * Created by Hakan on 8.11.2015.
 */

public class AlarmManager2 {
    private int _id,_active,_vibrate,_difficulty;
    private String _repeat,_ringtone,_time;

    public AlarmManager2(int _id, int _active, int _vibrate, int _difficulty, String _repeat, String _ringtone, String _time) {
        this._id = _id;
        this._active = _active;
        this._vibrate = _vibrate;
        this._difficulty = _difficulty;
        this._repeat = _repeat;
        this._ringtone = _ringtone;
        this._time = _time;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_active() {
        return _active;
    }

    public void set_active(int _active) {
        this._active = _active;
    }

    public int get_vibrate() {
        return _vibrate;
    }

    public void set_vibrate(int _vibrate) {
        this._vibrate = _vibrate;
    }

    public int get_difficulty() {
        return _difficulty;
    }

    public void set_difficulty(int _difficulty) {
        this._difficulty = _difficulty;
    }

    public String get_repeat() {
        return _repeat;
    }

    public void set_repeat(String _repeat) {
        this._repeat = _repeat;
    }

    public String get_ringtone() {
        return _ringtone;
    }

    public void set_ringtone(String _ringtone) {
        this._ringtone = _ringtone;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }
}
