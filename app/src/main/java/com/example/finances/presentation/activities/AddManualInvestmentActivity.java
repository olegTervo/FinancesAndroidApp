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
public class AddManualInvestmentActivity extends BaseActivity {
    private DatabaseHelper db;

    @Inject
    InvestmentsService investmentService;
    @Inject
    VariablesService variablesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_investment);

        this.db = new DatabaseHelper(this);
        setListeners();
    }

    private void setListeners() {
        Button submit = findViewById(R.id.storeInvestmentButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = findViewById(R.id.investmentNameInput);
                EditText amount = findViewById(R.id.investmentAmountInput);
                EditText price = findViewById(R.id.investmentPriceInput);

                if (name.getText().length() == 0 || amount.getText().length() == 0 || price.getText().length() == 0){
                    ShowError(view, "Empty fields");
                    return;
                }

                Investment newInvestment = investmentService.createInvestment(
                        name.getText().toString(),
                        Float.parseFloat(amount.getText().toString()),
                        Float.parseFloat(price.getText().toString()));

                boolean passed = variablesService.increaseTopValue(Float.parseFloat(amount.getText().toString()) * Float.parseFloat(price.getText().toString()), ValueDateType.Investments);

                if (passed) {
                    ShowConfirmation(view, "Investment saved!", 1000);
                    name.setText("");
                    amount.setText("");
                    price.setText("");
                }
                else
                    ShowError(view, "Failed to save investment...");
            }
        });
    }
}