package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finances.models.Grouth;
import com.example.finances.views.LinearGraph;
import com.example.finances.views.MyEasyTable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MyEasyTable DataTable;
    private DataBaseHelper db;
    private SettingsActivity settingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHelper(this);
        DataTable = new MyEasyTable(this);

        drawGraph();
        MakeButtonHandlers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        drawGraph();
    }

    private void MakeButtonHandlers() {
        Button submit = findViewById(R.id.button2);
        Button settings = findViewById(R.id.openSettingsButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.editTextTextPersonName);
                String text = textView.getText().toString();
                textView.setText("");
                DataTable.addRow(text);

                try {
                    boolean added = db.add(Integer.parseInt(text));

                    if(!added)
                        Toast.makeText(MainActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();

                    drawGraph();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setContentView(R.layout.activity_settings);

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void drawGraph() {
        ConstraintLayout GraphView = findViewById(R.id.graph);
        ConstraintLayout TableView = findViewById(R.id.table);

        ArrayList<Grouth> values = db.getValues();

        int tableId = 1100008;
        int graphId = 1100009;

        LinearGraph graph = new LinearGraph(this, values, graphId);
        MyEasyTable table = new MyEasyTable(this, values, tableId);

        if(GraphView.getViewById(graphId) != null)
            GraphView.removeView(findViewById(graphId));

        GraphView.addView(graph);

        if(TableView.getViewById(tableId) != null)
            TableView.removeView(findViewById(tableId));

        TableView.addView(table);

        /*
        String valuesToString = "" ;
        for(int v : values) valuesToString += v + ", ";
        Toast.makeText(MainActivity.this, "Values: " + valuesToString, Toast.LENGTH_LONG).show();
        */
    }

    public static void log(Context context, String text) {
        Toast.makeText(context, "" + text, Toast.LENGTH_LONG).show();
    }
}