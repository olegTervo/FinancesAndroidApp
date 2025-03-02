package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.finances.domain.interfaces.IApiRepository;
import com.example.finances.domain.models.Api;
import com.example.finances.domain.services.VariablesService;
import com.example.finances.frameworks_and_drivers.api_gateway.ApiClient;
import com.example.finances.frameworks_and_drivers.api_gateway.ApiInterface;
import com.example.finances.domain.models.api.CoinListDto;
import com.example.finances.domain.models.api.CoinMarketCap.Datum;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;
import com.example.finances.domain.enums.ApiType;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.Investment;
import com.example.finances.domain.services.InvestmentsService;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class AddApiInvestmentActivity extends BaseActivity {
    private DatabaseHelper db;

    @Inject
    InvestmentsService investmentService;
    @Inject
    VariablesService variablesService;
    // TODO think
    @Inject
    IApiRepository apiRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_api_investment);

        this.db = new DatabaseHelper(this);
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

                Investment newInvestment = investmentService.createInvestment(name.getText().toString(), Float.parseFloat(amount.getText().toString()), 0);
                boolean passed = apiRepository.CreateInvestmentApi(ApiType.CoinMarketCap, newInvestment.getId(), apiSpecificName.getText().toString());
                float last = 0;

                if (newInvestment.getLastPrice() != null)
                    last = newInvestment.getLastPrice().GetPrice();

                float ch = newInvestment.getAmount() * last;
                passed = passed && variablesService.increaseTopValue(ch, ValueDateType.Investments);

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
        Api coiMarketApi = apiRepository.GetApi(ApiType.CoinMarketCap);
        ApiInterface currenciesApi = ApiClient.getClient(coiMarketApi.getLink()).create(ApiInterface.class);
        String key = coiMarketApi.getKey();
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