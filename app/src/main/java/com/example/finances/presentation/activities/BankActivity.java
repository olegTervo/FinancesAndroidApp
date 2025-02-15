package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.finances.domain.models.Loan;
import com.example.finances.domain.services.BankService;
import com.example.finances.domain.services.VariablesService;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.presentation.views.MyEasyTable;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BankActivity extends BaseActivity {
    private DatabaseHelper db;

    @Inject
    BankService bankService;
    @Inject
    VariablesService variablesService;

    private int bankMoney;
    private ArrayList<Loan> loans;

    private MyEasyTable dataTable;

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
        this.bankMoney = bankService.GetBankMoney();
        this.loans = bankService.GetUnpaidLoans();
    }

    private void setData() {
        TextView bankMoney = findViewById(R.id.BankMoney);
        bankMoney.setText(this.bankMoney + "€");

        ConstraintLayout TableView = findViewById(R.id.ValuesView);
        int tableId = 1101001;

        if(TableView.getViewById(tableId) != null)
            TableView.removeView(findViewById(tableId));

        this.dataTable = new MyEasyTable(this, this.loans.toArray(), tableId, 3);
        this.dataTable.addRow(new String[] {"Loans: " + this.loans.size()});
        TableView.addView(this.dataTable);
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
                        if(BankActivity.this.loans.size()>0){
                            Loan loan = BankActivity.this.loans.get(BankActivity.this.loans.size()-1);

                            if(loan.getUnpaid() < value){
                                added = bankService.PayLoan(loan.id, loan.getUnpaid());
                                BankActivity.this.loans.remove(BankActivity.this.loans.size()-1);
                                value -= loan.getUnpaid();
                                variablesService.increaseTopValue( loan.getUnpaid()*(-1), ValueDateType.DailyGrowth);
                            }
                            else {
                                added = bankService.PayLoan(loan.id, value);
                                variablesService.increaseTopValue(value*(-1), ValueDateType.DailyGrowth);
                                value = 0;
                            }
                        }
                        else {
                            added = bankService.AddMoneyToBank(value) != -1;
                            variablesService.increaseTopValue(value*(-1), ValueDateType.DailyGrowth);
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
                    boolean added = bankService.TakeLoan(value);

                    if(!added)
                        Toast.makeText(BankActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(BankActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                        variablesService.increaseTopValue(value, ValueDateType.DailyGrowth);
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