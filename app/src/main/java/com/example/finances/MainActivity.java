package com.example.finances;

import static com.example.finances.FullPriceShopActivity.FullPriceShopName;
import static java.time.temporal.ChronoUnit.DAYS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finances.Database.helpers.AccountHelper;
import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.ShopHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.enums.VariableType;
import com.example.finances.views.LinearGraph;
import com.example.finances.views.MyEasyTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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

        if(values.size() > 30) values = new ArrayList<>(values.subList(0, 30));

        int target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target)) - VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Actives));
        LinearGraph graph = new LinearGraph(this, values, graphId, target);
        SetListeners(graph);

        if(GraphView.getViewById(graphId) != null)
            GraphView.removeView(findViewById(graphId));

        GraphView.addView(graph);
    }

    public void drawGraph(float x, float y) {
        int graphId = 1100009;
        ConstraintLayout GraphView = findViewById(R.id.graph);

        DailyGrowthHelper.sync(db);
        ArrayList<DailyGrowthDao> values = DailyGrowthHelper.getValues(db);

        int daysMove = -Math.round(x/30);
        if(daysMove > 100) daysMove = 100;
        if(daysMove < -100) daysMove = -100;

        int valueChange = Math.round(y);
        if(valueChange > 1000) valueChange = 1000;
        if(valueChange < -1000) valueChange = -1000;
        final int valueMove = valueChange;

        TextView output = findViewById(R.id.Output);
        output.setText("d:" + daysMove + "; v:" + valueChange);

        int startIndex = Math.min(daysMove, values.size() - 1);
        startIndex = Math.max(startIndex, 0);
        int endIndex = Math.min(daysMove + 30, values.size() - 1);
        endIndex = Math.max(endIndex, 0);

        values = new ArrayList<>(values.subList(startIndex, endIndex));
        values.forEach(v -> v.value += valueMove);

        int target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target)) - VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Actives));
        LinearGraph graph = new LinearGraph(this, values, graphId, target, x, y);
        SetListeners(graph);

        if(GraphView.getViewById(graphId) != null)
            GraphView.removeView(findViewById(graphId));

        GraphView.addView(graph);
    }

    private void SetListeners(LinearGraph graph) {
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView output = findViewById(R.id.Output);
                output.setText("clicked!" + LocalTime.now());
            }
        });

        graph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((LinearGraph) view).setDragPoints(x, y);
                        return true;

                    case MotionEvent.ACTION_UP:
                        drawGraph(((LinearGraph) view).currentX - x, ((LinearGraph) view).currentY - y);
                        return true;
                }

                return true;
            }
        });
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
        int bank = AccountHelper.GetMoney(db, 1);
        int shop = AccountHelper.GetMoney(db,  ShopHelper.GetShopAccountNumber(db, FullPriceShopName));
        VariablesHelper.setVariable(db, VariableType.toInt(VariableType.Balance), this.Balance);

        addRow(new String[] {"\t+: " + this.Balance,                          "\t\t- : " + this.Actives,                "\t\t€ : " + bank}, valueTable);
        addRow(new String[] {"\tLast: " + DailyGrowthHelper.getTopValue(db),  "\t\tTarget: " + this.Target,             "\t\t€+ : " + (this.Balance + bank)}, valueTable);
        addRow(new String[] {"\tUse: " + this.DailyGrowth + "/day",           "\t\tDays left: " + daysToIncome,         "\t\t$€+ : " + (this.Balance + bank + shop)}, valueTable);
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