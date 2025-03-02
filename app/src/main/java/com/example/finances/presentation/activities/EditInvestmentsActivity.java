package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finances.domain.models.Investment;
import com.example.finances.domain.services.InvestmentsService;
import com.example.finances.domain.services.VariablesService;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.domain.enums.ValueDateType;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditInvestmentsActivity extends BaseActivity {
    private DatabaseHelper db;

    @Inject
    InvestmentsService investmentService;
    @Inject
    VariablesService variablesService;

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
                long toDelete = Long.parseLong(id.getText().toString());

                Investment inv = investmentService.getInvestment(toDelete);
                //float p = PriceHelper.GetPrice(db, toDelete, PriceType.Investment, 0); TODO check
                if (inv.getLastPrice() != null)
                    completed = variablesService.increaseTopValue(-(inv.getAmount()*inv.getLastPrice().GetPrice()), ValueDateType.Investments);
                else
                    completed = true;

                completed = completed && investmentService.deleteInvestment(toDelete);

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
                    Investment inv = investmentService.getInvestment(toEdit);

                    if (inv != null) {
                        inv.setAmount(amount);
                        updated = investmentService.updateInvestment(inv) != null;
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