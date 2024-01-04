package com.example.finances;

import static com.example.finances.FullPriceShopActivity.FullPriceShopName;
import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.finances.Api.ApiClient;
import com.example.finances.Api.ApiInterface;
import com.example.finances.Api.models.CoinListDto;
import com.example.finances.Api.models.CoinMarketCap.Datum;
import com.example.finances.Database.helpers.AccountHelper;
import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.ShopHelper;
import com.example.finances.Database.helpers.VariablesHelper;
import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.enums.CryptocurrencyType;
import com.example.finances.enums.VariableType;
import com.example.finances.views.LinearGraph;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private DatabaseHelper db;
    private ApiInterface currenciesApi;

    private int DailyGrowth;
    private int Target;
    private int Balance;
    private int Actives;
    private double TonPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        TonPrice = 0;
        currenciesApi = ApiClient.getClient("https://pro-api.coinmarketcap.com").create(ApiInterface.class);
        //currenciesApi = ApiClient.getClient("https://sandbox-api.coinmarketcap.com").create(ApiInterface.class);

        MakeButtonHandlers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCoins();
    }

    private void getCoins() {
        if(this.TonPrice != 0) {
            refresh();
            return;
        }

        Call<CoinListDto> call = currenciesApi.getCoins("1", "20", "EUR");
        call.enqueue(new Callback<CoinListDto>() {
            @Override
            public void onResponse(Call<CoinListDto> call, Response<CoinListDto> response) {
                try {
                    CoinListDto resp = response.body();
                    Datum toncoin = resp.getData().stream()
                            .filter(c -> c.getName().equals(CryptocurrencyType.Ton.toString()))
                            .collect(Collectors.toList())
                            .get(0);
                    MainActivity.this.TonPrice = toncoin.getQuote().getEUR().getPrice();

                    TextView output = findViewById(R.id.Output);
                    output.setText(
                            new DecimalFormat("#0.000€")
                                    .format(MainActivity.this.TonPrice)
                    );
                }
                catch (Exception e) {
                    Log(e.getLocalizedMessage());
                }
                finally {
                    refresh();
                }
            }

            @Override
            public void onFailure(Call<CoinListDto> call, Throwable t) {
                Log(t.getLocalizedMessage());
                call.cancel();
                refresh();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    private void refresh() {
        getData();
        setData();
        drawGraph();
    }

    private void MakeButtonHandlers() {
        Button submit = findViewById(R.id.button2);
        ImageButton settings = findViewById(R.id.SettingsButtonMain);
        ImageButton menu = findViewById(R.id.MenuButton);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.editTextTextPersonName);
                String text = textView.getText().toString();
                textView.setText("");

                try {
                    boolean added = DailyGrowthHelper.increaseTopValue(db, Integer.parseInt(text));

                    if(!added)
                        Toast.makeText(MainActivity.this, "Failed to insert into database, returned false", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();

                    refresh();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to insert into database" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void drawGraph() {
        LinearGraph graph = findViewById(R.id.graph);

        DailyGrowthHelper.sync(db);
        ArrayList<DailyGrowthDao> values = DailyGrowthHelper.getValues(db);
        int target = this.Target - this.Actives;
        graph.setValues(values, target, 150, 0);
    }

    private void getData() {
        this.DailyGrowth = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.DailyGrowth));
        this.Target = VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Target));
        this.Actives = (int) Math.round(VariablesHelper.getVariable(db, VariableType.toInt(VariableType.Actives)) * this.TonPrice);
    }

    private void setData() {
        TableLayout valueTable = findViewById(R.id.mainTable);
        valueTable.removeAllViews();

        int daysToIncome = LocalDate.now().getDayOfMonth() < 20
                ? (int) DAYS.between(LocalDate.now(), LocalDate.now().withDayOfMonth(20))
                : (int) DAYS.between(LocalDate.now(), LocalDate.now().plusMonths(1).withDayOfMonth(20));
        this.Balance = DailyGrowthHelper.getTopValue(db) + this.DailyGrowth * daysToIncome;
        int bank = AccountHelper.GetMoney(db, 1);
        int shop = AccountHelper.GetMoney(db,  ShopHelper.GetShopAccountNumber(db, FullPriceShopName));
        VariablesHelper.setVariable(db, VariableType.toInt(VariableType.Balance), this.Balance);

        addRow(new String[] {"\t+: " + this.Balance,                          "\t\t- : " + this.Actives,                "\t\t€ : " + bank}, valueTable);
        addRow(new String[] {"\tLast: " + DailyGrowthHelper.getTopValue(db),  "\t\tTarget: " + this.Target,             "\t\t€+ : " + (this.Balance + bank)}, valueTable);
        addRow(new String[] {"\tUse: " + this.DailyGrowth + "/day",           "\t\tDays left: " + daysToIncome,         "\t\t$€+ : " + (this.Balance + bank + shop)}, valueTable);
    }

    private void addRow(String[] columns, TableLayout table) {
        TableRow row = new TableRow(this);

        for(String column : columns) {
            TextView rowText = new TextView(this);
            rowText.setText(column);

            row.addView(rowText);
        }

        table.addView(row);
    }
}