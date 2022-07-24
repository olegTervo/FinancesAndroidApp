package com.example.finances.views;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.Database.models.OperationDao;

import java.util.ArrayList;

public class MyEasyTable extends TableLayout {
    private int n;

    public MyEasyTable(Context context) {
        super(context);
    }

    public MyEasyTable(Context context, Object[] values, int id, int n) {
        super(context);
        this.setId(id);
        this.n = n;
        AddRows(values);
    }

    private void AddRows(Object[] values) {
        String[] toAdd = new String[this.n];

        for(int i = 0; i < values.length; i++) {
            if(i % this.n == 0 && i > 0) {
                addRow(toAdd);
                toAdd = new String[this.n];
            }
            toAdd[i%this.n] = values[i].toString();
        }

        addRow(toAdd);
    }

    public void addRow(String[] columns) {
        TableRow row = new TableRow(this.getContext());

        for(String column : columns) {
            TextView rowText = new TextView(this.getContext());
            rowText.setText(column);

            row.addView(rowText);
        }

        addView(row);
    }

    public void Clear() {
        removeAllViews();
    }
}
