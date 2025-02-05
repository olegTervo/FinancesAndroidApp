package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentHelper;
import com.example.finances.frameworks_and_drivers.database.price.PriceHelper;
import com.example.finances.frameworks_and_drivers.database.value_date.ValueDateHelper;
import com.example.finances.frameworks_and_drivers.database.investment.InvestmentDao;
import com.example.finances.R;
import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ValueDateType;

public class EditInvestmentsActivity extends BaseActivity {
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_investments);

        this.db = new DatabaseHelper(this);
        setListeners();
    }

    private void setListeners() {
        Button delete = findViewById(R.id.deleteInvestmentButton);
        Button edit = findViewById(R.id.editInvestmentButton);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean completed = false;
                EditText id = findViewById(R.id.idToDelete);
                int toDelete = Integer.parseInt(id.getText().toString());

                InvestmentDao inv = InvestmentHelper.GetInvestment(db, toDelete);
                float p = PriceHelper.GetPrice(db, toDelete, PriceType.Investment, 0);
                completed = ValueDateHelper.increaseTopValue(db, -(inv.Amount*p), ValueDateType.Investments);

                completed = completed && InvestmentHelper.DeleteInvestment(db, toDelete);

                if (completed) {
                    ShowConfirmation(view, "Investment deleted!", 3000);
                    id.setText("");
                }
                else
                    ShowError(view, "Investment deletion failed!");
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText id = findViewById(R.id.idToEdit);
                EditText val = findViewById(R.id.valToSet);

                try {
                    boolean updated = false;
                    int toEdit = Integer.parseInt(id.getText().toString());
                    float amount = Float.parseFloat(val.getText().toString());
                    InvestmentDao inv = InvestmentHelper.GetInvestment(db, toEdit);

                    if (inv != null) {
                        inv.Amount = amount;
                        updated = InvestmentHelper.UpdateInvestment(db, inv);
                    }

                    if(updated) {
                        ShowConfirmation(view, "Investment updated!", 3000);
                        id.setText("");
                        val.setText("");
                    }
                    else
                        ShowError(view, "Something went wrong...");

                }
                catch (Exception e) {
                    Log("here");
                    ShowError(view, e.getMessage());
                }
            }
        });
    }
}