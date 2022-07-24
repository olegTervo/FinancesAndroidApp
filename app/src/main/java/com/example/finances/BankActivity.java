package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finances.Database.helpers.AccountHelper;
import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.LoanHelper;
import com.example.finances.Database.models.LoanDao;
import com.example.finances.views.MyEasyTable;

import java.util.ArrayList;

public class BankActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int BankMoney;
    private MyEasyTable DataTable;

    private ArrayList<LoanDao> Loans;

    public BankActivity() {
        this.db = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        getData();
        setButtons();
        setData();
    }

    private void refresh(){
        getData();
        setData();
    }

    private void getData() {
        if(!AccountHelper.AccountExists(db, 1))
            AccountHelper.Initialize(db);

        this.BankMoney = AccountHelper.GetMoney(db, 1);
        this.Loans = LoanHelper.GetUnpaidLoans(db);
    }

    private void setData() {
        TextView bankMoney = findViewById(R.id.BankMoney);
        bankMoney.setText(this.BankMoney + "€");

        ConstraintLayout TableView = findViewById(R.id.ValuesView);
        int tableId = 1101001;

        if(TableView.getViewById(tableId) != null)
            TableView.removeView(findViewById(tableId));

        this.DataTable = new MyEasyTable(this, this.Loans.toArray(), tableId, 3);
        this.DataTable.addRow(new String[] {"Loans: " + this.Loans.size()});
        TableView.addView(this.DataTable);
    }

    private void setButtons() {
        Button takeLoan = findViewById(R.id.TakeLoan);
        Button putMoney = findViewById(R.id.PutMoney);
        Button loans = findViewById(R.id.Loans);
        Button investments = findViewById(R.id.Investments);

        putMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.editTextNumberDecimal);
                String text = textView.getText().toString();
                textView.setText("");

                try {
                    int value = Integer.parseInt(text);
                    boolean added = true;

                    while (value > 0 && added) {
                        if(BankActivity.this.Loans.size()>0){
                            LoanDao loan = BankActivity.this.Loans.get(BankActivity.this.Loans.size()-1);

                            if(loan.unpaid < value){
                                added = LoanHelper.PayLoan(db, loan.id, loan.unpaid);
                                BankActivity.this.Loans.remove(BankActivity.this.Loans.size()-1);
                                value -= loan.unpaid;
                                added = DailyGrowthHelper.increaseTopValue(db, loan.unpaid*(-1));
                            }
                            else {
                                added = LoanHelper.PayLoan(db, loan.id, value);
                                added = DailyGrowthHelper.increaseTopValue(db, value*(-1));
                                value = 0;
                            }
                        }
                        else {
                            added = AccountHelper.PutMoney(db, 1, value, "Extra money") != -1;
                            added = DailyGrowthHelper.increaseTopValue(db, value*(-1));
                            value = 0;
                        }
                    }

                    if(!added)
                        Toast.makeText(BankActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(BankActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                    }

                    refresh();
                }
                catch (Exception e) {
                    Toast.makeText(BankActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        takeLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.editTextNumberDecimal);
                String text = textView.getText().toString();
                textView.setText("");

                try {
                    int value = Integer.parseInt(text);
                    boolean added = LoanHelper.TakeLoan(db, value);

                    if(!added)
                        Toast.makeText(BankActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(BankActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                        DailyGrowthHelper.increaseTopValue(db, value);
                    }

                    refresh();
                }
                catch (Exception e) {
                    Toast.makeText(BankActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }
}