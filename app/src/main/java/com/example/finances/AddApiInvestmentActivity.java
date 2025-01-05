package com.example.finances;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.finances.Api.ApiClient;
import com.example.finances.Api.ApiInterface;
import com.example.finances.Api.models.CoinListDto;
import com.example.finances.Api.models.CoinMarketCap.Datum;
import com.example.finances.Database.helpers.ApiHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.helpers.InvestmentHelper;
import com.example.finances.Database.helpers.PriceHelper;
import com.example.finances.Database.helpers.ValueDateHelper;
import com.example.finances.Database.models.ApiDao;
import com.example.finances.Database.models.InvestmentDao;
import com.example.finances.common.interfaces.IApiCallback;
import com.example.finances.common.models.ApiCallback;
import com.example.finances.common.services.EventService;
import com.example.finances.enums.ApiType;
import com.example.finances.enums.PriceType;
import com.example.finances.enums.ValueDateType;
import com.example.finances.models.ApiInvestment;
import com.example.finances.models.Investment;
import com.example.finances.services.InvestmentsService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddApiInvestmentActivity extends BaseActivity {
    private DatabaseHelper db;
    private InvestmentsService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_api_investment);

        this.db = new DatabaseHelper(this);
        this.service = new InvestmentsService(db);
        setListeners();
    }

    private void setListeners() {
        Button submit = findViewById(R.id.storeInvestmentButton);
        Spinner api = findViewById(R.id.apiSpinner); // TODO: use

        ArrayAdapter<String> items = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, APIES);
        api.setAdapter(items);
        //api.setOnItemSelectedListener(new AccountChangeListener(this));
        api.setSelection(0);

        getNames();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = findViewById(R.id.investmentNameInput);
                EditText amount = findViewById(R.id.investmentAmountInput);
                EditText apiSpecificName = findViewById(R.id.apiSpecificNameInput);

                if (name.getText().length() == 0 || amount.getText().length() == 0 || apiSpecificName.getText().length() == 0){
                    ShowError(view, "Empty fields");
                    return;
                }

                InvestmentDao toCreate = new InvestmentDao(name.getText().toString(), Float.parseFloat(amount.getText().toString()));
                long investmentId = InvestmentHelper.CreateInvestment(db, toCreate);
                boolean passed = ApiHelper.CreateInvestmentApi(db, ApiType.CoinMarketCap, investmentId, apiSpecificName.getText().toString());

                List<Investment> investmentList = service.getInvestments();
                List<Investment> filtered = investmentList.stream().filter(i -> i.getId() == investmentId).collect(Collectors.toList());

                System.out.println(filtered.get(0).toString());

                if (filtered.size() == 1) {
                    float last = 0;

                    if (filtered.get(0).getLastPrice() != null)
                        last = filtered.get(0).getLastPrice().GetPrice();

                    float ch = filtered.get(0).getAmount() * last;
                    passed = passed && ValueDateHelper.increaseTopValue(db, ch, ValueDateType.Investments);
                }

                if (passed) {
                    ShowConfirmation(view, "Investment saved!", 1000);
                    name.setText("");
                    amount.setText("");
                    apiSpecificName.setText("");
                }
                else
                    ShowError(view, "Failed to save investment...");
            }
        });
    }

    private void getNames() {
        ApiDao coiMarketApi = ApiHelper.GetApi(db, ApiType.CoinMarketCap);
        ApiInterface currenciesApi = ApiClient.getClient(coiMarketApi.Link).create(ApiInterface.class);
        String key = coiMarketApi.Key;
        Call<CoinListDto> call = currenciesApi.getCoins(key, "1", "300", "EUR");

        call.enqueue(new Callback<CoinListDto>() {
            @Override
            public void onResponse(Call<CoinListDto> call, Response<CoinListDto> response) {
                try {
                    CoinListDto resp = response.body();
                    List<Datum> coins = resp.getData();

                    Spinner names = findViewById(R.id.apiNameSpinner);
                    List<String> res = coins.stream().map(c -> c.toString()).collect(Collectors.toList());
                    ArrayAdapter<String> namesList = new ArrayAdapter<>(AddApiInvestmentActivity.this, android.R.layout.simple_spinner_dropdown_item, res);

                    names.setAdapter(namesList);
                    //api.setOnItemSelectedListener(new AccountChangeListener(this));
                    names.setSelection(0);
                }
                catch (Exception e) {
                    ShowError(findViewById(R.id.apiNameSpinner), e.toString());
                }
            }

            @Override
            public void onFailure(Call<CoinListDto> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private static final String[] APIES = new String[] {
            "CoinMarketCap"
    };
}