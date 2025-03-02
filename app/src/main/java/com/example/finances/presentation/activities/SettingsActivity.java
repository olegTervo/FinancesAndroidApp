package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finances.domain.services.VariablesService;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.domain.enums.VariableType;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends BaseActivity {

    @Inject
    VariablesService variablesService;

    private DatabaseHelper db;
    private int DailyGrowth;
    private int Target;
    private int Actives;

    public SettingsActivity() {
        this.db = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getData();
        setButtons();
        setData();
    }

    private void setData() {
        EditText dailyGrowth = findViewById(R.id.dailyGrowth);
        EditText target = findViewById(R.id.target);
        EditText actives = findViewById(R.id.actives);

        dailyGrowth.setText("" + this.DailyGrowth);
        target.setText("" + this.Target);
        actives.setText("" + this.Actives);
    }

    private void getData() {
        this.DailyGrowth = variablesService.getVariable(VariableType.toInt(VariableType.DailyGrowth));
        this.Target = variablesService.getVariable(VariableType.toInt(VariableType.Target));
        this.Actives = variablesService.getVariable(VariableType.toInt(VariableType.Actives));
    }

    private void setButtons() {
        Button submitDailyGrowth = findViewById(R.id.submitDailyGrowth);
        Button submitTarget = findViewById(R.id.submitTarget);
        Button submitActives = findViewById(R.id.submitActives);

        submitDailyGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dailyGrowth = findViewById(R.id.dailyGrowth);

                try {
                    boolean changed = variablesService.setVariable(VariableType.toInt(VariableType.DailyGrowth), Integer.parseInt(dailyGrowth.getText().toString()));

                    if(!changed)
                        Log("Failed to change Variable, returned false");
                    else
                        Log("Saved successfully!");
                }
                catch (Exception e) {
                    Log("Failed to insert into database" + e.getMessage());
                }
            }
        });

        submitTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView target = findViewById(R.id.target);

                try {
                    boolean changed = variablesService.setVariable(VariableType.toInt(VariableType.Target), Integer.parseInt(target.getText().toString()));

                    if(!changed)
                        Log("Failed to change Variable, returned false");
                    else
                        Log("Saved successfully!");
                }
                catch (Exception e) {
                    Log("Failed to insert into database");
                }
            }
        });

        submitActives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView actives = findViewById(R.id.actives);

                try {
                    boolean changed = variablesService.setVariable(VariableType.toInt(VariableType.Actives), Integer.parseInt(actives.getText().toString()));

                    if(!changed)
                        Log("Failed to change Variable, returned false");
                    else
                        Log("Saved successfully!");
                }
                catch (Exception e) {
                    Log("Failed to insert into database" + e.getMessage());
                }
            }
        });
    }
}