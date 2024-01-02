package com.example.finances;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.OperationHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.Database.models.OperationDao;
import com.example.finances.enums.VariableType;
import com.example.finances.views.MyEasyTable;

import java.util.ArrayList;

public class DatabaseActivity extends BaseActivity {
    private DatabaseHelper db;
    private int DailyGrowth;
    private int Target;

    private ArrayList<MyEasyTable> DataTable;
    private ArrayList<DailyGrowthDao> Values;
    private ArrayList<OperationDao> BankOperations;

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
        this.DataTable = new ArrayList<MyEasyTable>();

        DailyGrowthHelper.sync(db);
        this.Values = DailyGrowthHelper.getValues(db);
        this.BankOperations = OperationHelper.GetAccountOperations(db, 1);
    }

    private void setData() {
        ConstraintLayout BankOperationsTableView = findViewById(R.id.table);
        ConstraintLayout DailyGrowthTableView = findViewById(R.id.table1);
        int tableId = 1101008;

        if(BankOperationsTableView.getViewById(tableId) != null)
            BankOperationsTableView.removeView(findViewById(tableId));

        this.DataTable.add(new MyEasyTable(this, this.BankOperations.toArray(), tableId, 1));
        BankOperationsTableView.addView(this.DataTable.get(0));

        if(DailyGrowthTableView.getViewById(tableId+1) != null)
            DailyGrowthTableView.removeView(findViewById(tableId+1));

        this.DataTable.add(new MyEasyTable(this, this.Values.toArray(), tableId+1, 3));
        DailyGrowthTableView.addView(this.DataTable.get(1));
    }

    private void setButtons() {
        Button empty = findViewById(R.id.emptyButton);
        Button submit = findViewById(R.id.submitById);

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
                                    Log("Clearing database...");
                                    DailyGrowthHelper.delete(db);
                                    Log("Cleared!");
                                }})

                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
                catch (Exception e) {
                    Log("Failed to clear database" + e.getMessage());
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
                        Log("Failed to insert into database, returned false");
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
