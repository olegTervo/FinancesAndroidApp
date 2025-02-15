package com.example.finances.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.domain.services.InvestmentsService;
import com.example.finances.presentation.views.MyEasyTable;

import javax.inject.Inject;

public class ListInvestmentActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private Object[] investments;

    @Inject
    InvestmentsService investmentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_investment);

        this.db = new DatabaseHelper(this);
        this.investments = investmentService.getInvestments().toArray();
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