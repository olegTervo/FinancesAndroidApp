package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.common.services.EventService;
import com.example.finances.models.Investment;
import com.example.finances.services.InvestmentsService;
import com.example.finances.views.MyEasyTable;

public class ListInvestmentActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private InvestmentsService service;
    private Object[] investments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_investment);

        this.db = new DatabaseHelper(this);
        this.service = new InvestmentsService(db, new EventService());
        this.investments = service.getInvestments(true).toArray();
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