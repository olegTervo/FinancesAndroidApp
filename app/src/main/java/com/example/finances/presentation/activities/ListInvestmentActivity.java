package com.example.finances.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.domain.services.InvestmentsService;
import com.example.finances.presentation.views.MyEasyTable;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListInvestmentActivity extends AppCompatActivity {
    private Object[] investments;

    @Inject
    InvestmentsService investmentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_investment);

        this.investments = investmentService
                .getInvestments()
                .stream()
                .sorted((a, b) -> {
                    if (a.getLastPrice() == null)
                        return -1;
                    if (b.getLastPrice() == null)
                        return 1;

                    return Float.compare(b.getLastPrice().GetPrice() * b.getAmount(), a.getLastPrice().GetPrice() * a.getAmount());
                })
                .toArray();
        setData();
    }

    private void setData() {
        ScrollView eventsListView = findViewById(R.id.investmentsList);
        int tableId = 1101003;

        eventsListView.removeAllViews();

        MyEasyTable table = new MyEasyTable(this, this.investments, tableId, 1);
        eventsListView.addView(table);
    }
}