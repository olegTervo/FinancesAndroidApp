package com.example.finances;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import retrofit2.Callback;

import com.example.finances.domain.enums.CurrencyType;
import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.interfaces.api.IApiCallback;
import com.example.finances.domain.models.Api;
import com.example.finances.domain.models.ApiInvestment;
import com.example.finances.domain.models.Investment;
import com.example.finances.frameworks_and_drivers.api_gateway.CoinMarketCapApiClient;
import com.example.finances.frameworks_and_drivers.database.price.PriceDao;
import com.example.finances.interface_adapters.api.CoinMarketCapApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;

public class CoinMarketCapApiUnitTest {
    @Mock
    private CoinMarketCapApiClient apiGateway;
    @Mock
    private PriceDao priceDao;

    private CoinMarketCapApi api;
    private boolean passed;

    @Before
    public void prepare() {
        MockitoAnnotations.openMocks(this);
        api = new CoinMarketCapApi(apiGateway, priceDao);
        passed = false;

        doAnswer(invocation -> passed = true)
                .when(apiGateway).getCoins(
                        any(Callback.class),
                        any(CurrencyType.class));
    }

    @Test
    public void test_sync() {
        IApiCallback syncCallback = new IApiCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        };

        ArrayList<ApiInvestment> toSync = new ArrayList<>();
        ApiInvestment toAdd = new ApiInvestment(
                new Investment(1,
                    "BTC",
                    (float) 0.0001,
                    LocalDate.now(),
                    LocalDate.now(),
                    InvestmentType.ApiLinked),
                new Api(1, "TestApi", "", ""),
                ""
            );
        toSync.add(toAdd);

        api.syncPricesAsync(toSync, syncCallback);

        int tries = 0;
        while (!passed && tries < 5) {
            try {
                Thread.sleep(1000); // Waits for 1 second
                tries++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(passed);
    }
}
