package com.alarmquestions.miragessee.alarmquestions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakan on 19.11.2015.
 */

public class AlarmManagerListAdapter extends ArrayAdapter<AlarmManager2> {
    private Activity context;
    private List<AlarmManager2> alarms;
    private DatabaseHandler dbHandler;

    public AlarmManagerListAdapter(Activity context,List<AlarmManager2> alarms,DatabaseHandler dbHandler) {
        super (context, R.layout.list_single, alarms);
        this.context = context;
        this.alarms = alarms;
        this.dbHandler = dbHandler;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView textView6 = (TextView) rowView.findViewById(R.id.textView6);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox1);

        AlarmManager2 currentAlarm = alarms.get(position);

        if(currentAlarm.get_active() == 0)
        {
            checkBox.setChecked(false);
        }
        else
        {
            checkBox.setChecked(true);
        }

        txtTitle.setText(String.valueOf(currentAlarm.get_time()));
        textView6.setText(currentAlarm.get_repeat());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked())
                    alarms.get(position).set_active(1);
                else
                    alarms.get(position).set_active(0);
                dbHandler.updateAlarm(alarms.get(position));
            }
        });

        return rowView;
    }
}
