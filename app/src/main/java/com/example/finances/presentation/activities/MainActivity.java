package com.example.finances.presentation.activities;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.finances.domain.interfaces.IApiRepository;
import com.example.finances.domain.models.DailyGrowth;
import com.example.finances.domain.services.BankService;
import com.example.finances.domain.services.VariablesService;
import com.example.finances.R;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.enums.VariableType;
import com.example.finances.domain.models.HistoryPrice;
import com.example.finances.domain.models.ValueDate;
import com.example.finances.presentation.views.LinearGraph;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {
    @Inject
    IApiRepository apiRepository;
    @Inject
    BankService bankService;
    @Inject
    VariablesService variablesService;

    private int DailyGrowth;
    private int Target;
    private float Balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    boolean added = variablesService.increaseTopValue(Float.parseFloat(text), ValueDateType.DailyGrowth);

                    if(!added)
                        ShowError(view, "Failed to insert into database, returned false");
                    else
                        ShowConfirmation(view, "Saved successfully!", 1000);

                    refresh();
                }
                catch (Exception e) {
                    ShowError(view, "Failed to insert into database" + e.getMessage());
                }
                finally {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
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
        LinearGraph graph = findViewById(R.id.graph);

        variablesService.sync();
        ArrayList<ValueDate> values = new ArrayList<>();
        values.addAll(variablesService.getValues(ValueDateType.DailyGrowth));
        Collections.reverse(values);
        float target = this.Target - variablesService.getFirst(ValueDateType.Investments).value;
        graph.setValues(values, target, 150, -values.get(values.size()-1).value);
    }

    private void getData() {
        this.DailyGrowth = variablesService.getVariable(VariableType.toInt(VariableType.DailyGrowth));
        this.Target = variablesService.getVariable(VariableType.toInt(VariableType.Target));
    }

    private void setData() {
        TableLayout valueTable = findViewById(R.id.mainTable);
        valueTable.removeAllViews();

        DailyGrowth last = (DailyGrowth) variablesService.getFirst(ValueDateType.DailyGrowth);
        float lastVal = 0;
        if (last != null) lastVal = last.value;

        HistoryPrice actives = (HistoryPrice) variablesService.getFirst(ValueDateType.Investments);
        float lastActives = 0;
        if (actives != null) lastActives = actives.value;

        int daysToIncome = LocalDate.now().getDayOfMonth() < 20
                ? (int) DAYS.between(LocalDate.now(), LocalDate.now().withDayOfMonth(20))
                : (int) DAYS.between(LocalDate.now(), LocalDate.now().plusMonths(1).withDayOfMonth(20));
        this.Balance = lastVal + this.DailyGrowth * daysToIncome;
        int bank = bankService.GetBankMoney();
        int shop = bankService.GetShopMoney();
        variablesService.setVariable(VariableType.toInt(VariableType.Balance), Math.round(this.Balance));

        addRow(new String[] {"\t+: " + String.format("%.2f", this.Balance),     "\t\t- : " + String.format("%.2f", lastActives),    "\t\t€ : " + bank}, valueTable);
        addRow(new String[] {"\tLast: " + String.format("%.2f", lastVal),       "\t\tTarget: " + this.Target,                       "\t\t€+ : " + String.format("%.2f", (this.Balance + bank))}, valueTable);
        addRow(new String[] {"\tUse: " + this.DailyGrowth + "/day",             "\t\tDays left: " + daysToIncome,                   "\t\t$€+ : " + String.format("%.2f", (this.Balance + bank + shop))}, valueTable);
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
}