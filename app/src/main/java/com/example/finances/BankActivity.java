package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finances.Database.helpers.AccountHelper;
import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.LoanHelper;

public class BankActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int BankMoney;

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
    }

    private void setData() {
        TextView bankMoney = findViewById(R.id.BankMoney);
        bankMoney.setText(this.BankMoney + "€");
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
                    boolean added = AccountHelper.PutMoney(db, 1, value);

                    if(!added)
                        Toast.makeText(BankActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(BankActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                        DailyGrowthHelper.increaseTopValue(db, value*(-1));
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
                    boolean added = LoanHelper.TakeLoan(db, 1, value);

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
    }
}