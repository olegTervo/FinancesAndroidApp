package com.example.finances;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class LogActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        readLastLog();
    }

    private void readLastLog() {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> lines = new ArrayList<>();

        try {
            File log = getLogFile();
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(log));

            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            Log("File write failed: " + e.toString());
        }
        finally {
            for(int i = lines.size()-1; i >= 0; i--)
                builder.append(lines.get(i)).append("\n");

            TextView log = findViewById(R.id.LogText);
            log.setText(builder.toString());
        }
    }
}