package com.example.finances.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finances.domain.services.VariablesService;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.interface_adapters.common.interfaces.IApiCallback;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.HistoryPrice;
import com.example.finances.domain.models.ValueDate;
import com.example.finances.domain.services.InvestmentsService;
import com.example.finances.presentation.views.LinearGraph;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class InvestmentActivity extends BaseActivity {
    private List<HistoryPrice> investments;

    @Inject
    InvestmentsService investmentService;
    @Inject
    VariablesService variablesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.investmentService.sync(getCallback());
    }

    public void refresh() {
        this.investments = variablesService
                .getValues(ValueDateType.Investments)
                .stream()
                .map(v -> (HistoryPrice) v)
                .collect(Collectors.toList());

        fillGaps();
        String text = "";

        for (HistoryPrice price : investments){
            text = price.toString() + "\n" + text;
        }

        TextView info = findViewById(R.id.investmentsInfo);
        info.setText(text);

        drawGraph();
    }

    private void fillGaps() {
        HistoryPrice last = this.investments.get(investments.size()-1);
        this.investments.add(new HistoryPrice(0, 0, last.date.minusDays(1)));

        for (LocalDate i = last.date; i.compareTo(LocalDate.now()) < 0; i = i.plusDays(1)) {
            boolean found = false;

            for (HistoryPrice price : investments){
                if (price.date.compareTo(i) == 0) {
                    found = true;
                    last = price;
                    break;
                }
            }

            if (!found) {
                this.investments.add(new HistoryPrice(0, last.value, i));
            }
        }

        this.investments.sort((a, b) -> a.date.compareTo(b.date));
    }

    private void drawGraph() {
        LinearGraph graph = findViewById(R.id.investmentGraph);

        if (this.investments.isEmpty())
            graph.setValues(new ArrayList<ValueDate>(this.investments), 0, 150, 0);
        else
            graph.setValues(new ArrayList<ValueDate>(this.investments), 0, 150, -this.investments.get(investments.size()-1).value);
    }

    private void setListeners() {
        Button addManual = findViewById(R.id.addManual);
        Button add = findViewById(R.id.addApi);
        Button list = findViewById(R.id.listInvestments);
        Button edit = findViewById(R.id.editInvestments);

        ImageView update = findViewById(R.id.updateInvestmentsImage);

        addManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvestmentActivity.this, AddManualInvestmentActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvestmentActivity.this, AddApiInvestmentActivity.class);
                startActivity(intent);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvestmentActivity.this, ListInvestmentActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvestmentActivity.this, EditInvestmentsActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                investmentService.sync(getCallback());
            }
        });
    }
    private IApiCallback getCallback() {
        return new IApiCallback() {
            @Override
            public void onSuccess() {
                ShowConfirmation(findViewById(R.id.investmentGraph), "Syncronized!", 1000);
                refresh();
            }

            @Override
            public void onFailure(Throwable t) {
                ShowError(findViewById(R.id.investmentGraph), "Callback failed");
                LogToFile("ERROR: " + t.getMessage());
                refresh();
            }
        };
    }
}