package com.example.finances.views;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.finances.Database.models.DailyGrowthDao;

import java.util.ArrayList;

public class MyEasyTable extends TableLayout {

    public MyEasyTable(Context context) {
        super(context);
    }

    public MyEasyTable(Context context, ArrayList<DailyGrowthDao> values, int id) {
        super(context);
        this.setId(id);

        String toAdd = "";

        for(int i = 0; i < values.size(); i++) {
            if(i % 5 == 0 && i > 0) {
                addRow(toAdd);
                toAdd = "";
            }
            toAdd += values.get(i).toString();
        }

        if(toAdd.length() > 0)
            addRow(toAdd.substring(0, toAdd.length()));
    }

    public void addRow(String value) {
        TableRow row = new TableRow(this.getContext());
        TextView rowText = new TextView(this.getContext());
        rowText.setText(value);

        row.addView(rowText);
        addView(row);
    }

    public void Clear() {
        removeAllViews();
    }
}
