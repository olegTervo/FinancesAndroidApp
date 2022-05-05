package com.example.finances;

import static com.example.finances.MainActivity.log;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.enums.VariableType;
import com.example.finances.views.MyEasyTable;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int DailyGrowth;
    private int Target;

    private MyEasyTable DataTable;
    private ArrayList<DailyGrowthDao> Values;

    public DatabaseActivity() {
        this.db = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        getData();
        setButtons();
        setData();
    }

    private void getData() {
        this.DailyGrowth = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.DailyGrowth));
        this.Target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target));
        this.DataTable = new MyEasyTable(this);

        DailyGrowthHelper.sync(db);
        this.Values = DailyGrowthHelper.getValues(db);
    }

    private void setData() {
        ConstraintLayout TableView = findViewById(R.id.table);
        int tableId = 1100008;

        if(TableView.getViewById(tableId) != null)
            TableView.removeView(findViewById(tableId));

        this.DataTable = new MyEasyTable(this, this.Values, tableId);
        TableView.addView(this.DataTable);
    }

    private void setButtons() {
        Button empty = findViewById(R.id.emptyButton);

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
                                    log(DatabaseActivity.this, "Clearing database...");
                                    DailyGrowthHelper.delete(db);
                                    log(DatabaseActivity.this, "Cleared!");
                                }})

                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
                catch (Exception e) {
                    log(DatabaseActivity.this, "Failed to clear database" + e.getMessage());
                }
            }
        });
    }
}
