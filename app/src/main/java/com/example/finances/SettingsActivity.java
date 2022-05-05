package com.example.finances;

import static com.example.finances.MainActivity.log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.enums.VariableType;

public class SettingsActivity extends AppCompatActivity {
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
        this.DailyGrowth = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.DailyGrowth));
        this.Target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target));
        this.Actives = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Actives));
    }

    private void setButtons() {
        Button submit = findViewById(R.id.submitById);
        Button submitDailyGrowth = findViewById(R.id.submitDailyGrowth);
        Button submitTarget = findViewById(R.id.submitTarget);
        Button submitActives = findViewById(R.id.submitActives);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView id = findViewById(R.id.settingsIdToChange);
                TextView value = findViewById(R.id.settingsValueToChange);

                String idText = id.getText().toString();
                String valueText = value.getText().toString();

                id.setText("");
                value.setText("");

                try {
                    boolean added = DailyGrowthHelper.update(db, Integer.parseInt(idText), Integer.parseInt(valueText));

                    if(!added)
                        log(SettingsActivity.this, "Failed to insert into database, returned false");
                    else
                        log(SettingsActivity.this, "Saved successfully!");
                }
                catch (Exception e) {
                    log(SettingsActivity.this, "Failed to insert into database" + e.getMessage());
                }
            }
        });

        submitDailyGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dailyGrowth = findViewById(R.id.dailyGrowth);

                try {
                    boolean changed = VariablesHelper.setVariable(db, VariableType.toInt(VariableType.DailyGrowth), Integer.parseInt(dailyGrowth.getText().toString()));

                    if(!changed)
                        log(SettingsActivity.this, "Failed to change Variable, returned false");
                    else
                        log(SettingsActivity.this, "Saved successfully!");
                }
                catch (Exception e) {
                    log(SettingsActivity.this, "Failed to insert into database" + e.getMessage());
                }
            }
        });

        submitTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView target = findViewById(R.id.target);

                try {
                    boolean changed = VariablesHelper.setVariable(db, VariableType.toInt(VariableType.Target), Integer.parseInt(target.getText().toString()));

                    if(!changed)
                        log(SettingsActivity.this, "Failed to change Variable, returned false");
                    else
                        log(SettingsActivity.this, "Saved successfully!");
                }
                catch (Exception e) {
                    log(SettingsActivity.this, "Failed to insert into database");
                }
            }
        });

        submitActives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView actives = findViewById(R.id.actives);

                try {
                    boolean changed = VariablesHelper.setVariable(db, VariableType.toInt(VariableType.Actives), Integer.parseInt(actives.getText().toString()));

                    if(!changed)
                        log(SettingsActivity.this, "Failed to change Variable, returned false");
                    else
                        log(SettingsActivity.this, "Saved successfully!");
                }
                catch (Exception e) {
                    log(SettingsActivity.this, "Failed to insert into database" + e.getMessage());
                }
            }
        });
    }
}