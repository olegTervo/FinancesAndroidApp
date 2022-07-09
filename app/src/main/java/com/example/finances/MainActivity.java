package com.example.finances;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.enums.VariableType;
import com.example.finances.views.LinearGraph;
import com.example.finances.views.MyEasyTable;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;

    private int DailyGrowth;
    private int Target;
    private int Balance;
    private int Actives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        refresh();
        MakeButtonHandlers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    private void refresh() {
        getData();
        setData();
        drawGraph();
    }

    private void MakeButtonHandlers() {
        Button submit = findViewById(R.id.button2);
        ImageButton settings = findViewById(R.id.SettingsButtonMain);
        ImageButton menu = findViewById(R.id.MenuButton);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.editTextTextPersonName);
                String text = textView.getText().toString();
                textView.setText("");

                try {
                    boolean added = DailyGrowthHelper.increaseTopValue(db, Integer.parseInt(text));

                    if(!added)
                        Toast.makeText(MainActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();

                    refresh();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void drawGraph() {
        int graphId = 1100009;
        ConstraintLayout GraphView = findViewById(R.id.graph);

        DailyGrowthHelper.sync(db);
        ArrayList<DailyGrowthDao> values = DailyGrowthHelper.getValues(db);
        int target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target)) - VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Actives));
        LinearGraph graph = new LinearGraph(this, values, graphId, target);

        if(GraphView.getViewById(graphId) != null)
            GraphView.removeView(findViewById(graphId));

        GraphView.addView(graph);
    }

    private void getData() {
        this.DailyGrowth = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.DailyGrowth));
        this.Target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target));
        this.Actives = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Actives));
    }

    private void setData() {
        TableLayout valueTable = findViewById(R.id.mainTable);
        valueTable.removeAllViews();

        int daysToIncome = LocalDate.now().getDayOfMonth() < 20
                ? (int) DAYS.between(LocalDate.now(), LocalDate.now().withDayOfMonth(20))
                : (int) DAYS.between(LocalDate.now(), LocalDate.now().plusMonths(1).withDayOfMonth(20));
        this.Balance = DailyGrowthHelper.getTopValue(db) + this.DailyGrowth * daysToIncome;
        VariablesHelper.setVariable(db, VariableType.toInt(VariableType.Balance), this.Balance);

        addRow(new String[] {"\t+: " + this.Balance,                          "\t\t- : " + this.Actives}, valueTable);
        addRow(new String[] {"\tLast: " + DailyGrowthHelper.getTopValue(db),  "\t\tTarget: " + this.Target}, valueTable);
        addRow(new String[] {"\tUse: " + this.DailyGrowth + "/day",           "\t\tDays left: " + daysToIncome}, valueTable);
    }

    private void addRow(String[] columns, TableLayout table) {
        TableRow row = new TableRow(this);

        for(String column : columns) {
            TextView rowText = new TextView(this);
            rowText.setText(column);

            row.addView(rowText);
        }

        table.addView(row);
    }

    public static void log(Context context, String text) {
        Toast.makeText(context, "" + text, Toast.LENGTH_LONG).show();
    }
}