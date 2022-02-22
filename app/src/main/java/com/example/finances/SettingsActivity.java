package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private DataBaseHelper db;

    public SettingsActivity() {
        this.db = new DataBaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button empty = findViewById(R.id.emptyButton);
        Button submit = findViewById(R.id.submitNewValueButton);

        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(SettingsActivity.this, "Cleaning database...", Toast.LENGTH_LONG).show();
                    //db.delete();
                }
                catch (Exception e) {
                    Toast.makeText(SettingsActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                //((TextView) findViewById(R.id.editTextTextPersonName)).setText("");
                Toast.makeText(SettingsActivity.this, "Cleared!", Toast.LENGTH_LONG).show();
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
                    boolean added = db.set(Integer.parseInt(idText), Integer.parseInt(valueText));

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
    }
}