package com.alarmquestions.miragessee.alarmquestions;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Hakan on 9.11.2015.
 */
public class CustomList2 extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] difs;
    private final String[] selecteds;

    public CustomList2(Activity context,
                       String[] difs, String[] selecteds) {
        super(context, R.layout.list_single, difs);
        this.context = context;
        this.difs = difs;
        this.selecteds = selecteds;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_properties, null, true);
        TextView txt2 = (TextView) rowView.findViewById(R.id.txt2);

        TextView textView9 = (TextView) rowView.findViewById(R.id.textView9);
        textView9.setText(selecteds[position]);
        txt2.setText(difs[position]);

        return rowView;
    }
}
