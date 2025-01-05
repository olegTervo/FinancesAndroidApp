package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.InvestmentHelper;
import com.example.finances.Database.helpers.PriceHelper;
import com.example.finances.Database.helpers.ValueDateHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.Database.models.InvestmentDao;
import com.example.finances.common.models.EventListenerBase;
import com.example.finances.common.models.InvestmentsSyncedEventListener;
import com.example.finances.common.models.ShowCoolEventListener;
import com.example.finances.common.services.EventService;
import com.example.finances.enums.InvestmentType;
import com.example.finances.enums.PriceType;
import com.example.finances.enums.ValueDateType;
import com.example.finances.models.ApiInvestment;
import com.example.finances.models.HistoryPrice;
import com.example.finances.models.Investment;
import com.example.finances.models.ValueDate;
import com.example.finances.services.InvestmentsService;
import com.example.finances.views.LinearGraph;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvestmentActivity extends BaseActivity {
    private DatabaseHelper db;
    private InvestmentsService investmentService;
    private List<HistoryPrice> investments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);

        this.db = new DatabaseHelper(this);

        InvestmentsSyncedEventListener listener = new InvestmentsSyncedEventListener(this);
        EventService eventHandler = new EventService();
        eventHandler.addListener(listener);

        this.investmentService = new InvestmentsService(this.db, eventHandler);
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Thread.sleep(1000);
        }
        catch (Exception e) {}
        this.investmentService.sync();
    }

    public void ShowCool(){
        //ValueDate res = ValueDateHelper.getFirst(db, ValueDateType.Investments);

        this.investments = ValueDateHelper
                .getValues(db, ValueDateType.Investments)
                .stream()
                .map(v -> (HistoryPrice) v)
                .collect(Collectors.toList());

        Log("COOL " + this.investments.get(0).value);
        refresh();
    }

    public void refresh() {
        this.investments = ValueDateHelper
                .getValues(db, ValueDateType.Investments)
                .stream()
                .map(v -> (HistoryPrice) v)
                .collect(Collectors.toList());

        fillGaps();
        String text = "";

        List<Investment> ins = investmentService.getInvestments(false);

        for (HistoryPrice price : investments){
            text = price.toString() + "\n" + text;
        }
        text = ins.get(0).getLastPrice().GetModified() + "\n" + text;

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
                investmentService.sync();
                ShowConfirmation(view, "Updated!", 1000);
            }
        });
    }

}