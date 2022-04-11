package com.example.finances;

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

public class SettingsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int DailyGrowth;

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
        dailyGrowth.setText("" + this.DailyGrowth);
    }

    private void getData() {
        this.DailyGrowth = VariablesHelper.getVariable(db, 1);
    }

    private void setButtons() {
        Button empty = findViewById(R.id.emptyButton);
        Button submit = findViewById(R.id.submitById);
        Button submitDailyGrowth = findViewById(R.id.submitDailyGrowth);

        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Are you sure?")
                            .setMessage("Do you really want to clear DB?")
                            .setIcon(android.R.drawable.ic_dialog_alert)

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(SettingsActivity.this, "Clearing database...", Toast.LENGTH_LONG).show();
                                    DailyGrowthHelper.delete(db);
                                    Toast.makeText(SettingsActivity.this, "Cleared!", Toast.LENGTH_LONG).show();
                                }})

                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
                catch (Exception e) {
                    Toast.makeText(SettingsActivity.this, "Failed to clear database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

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
                        Toast.makeText(SettingsActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(SettingsActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    Toast.makeText(SettingsActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        submitDailyGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dailyGrouth = findViewById(R.id.dailyGrowth);

                try {
                    boolean changed = VariablesHelper.setVariable(db, 1, Integer.parseInt(dailyGrouth.getText().toString()));

                    if(!changed)
                        Toast.makeText(SettingsActivity.this, "Failed to change Variable, returned false", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(SettingsActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    Toast.makeText(SettingsActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}