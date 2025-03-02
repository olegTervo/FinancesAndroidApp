package com.example.finances;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.finances.domain.enums.CurrencyType;
import com.example.finances.domain.models.Api;
import com.example.finances.domain.models.api.CoinListDto;
import com.example.finances.frameworks_and_drivers.api_gateway.ApiInterface;
import com.example.finances.frameworks_and_drivers.api_gateway.CoinMarketCapApiClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinMarketCapApiClientUnitTest {

    private Api coinMarketCap;
    @Mock
    private ApiInterface coinMarketCapInterface;
    @Mock
    private Call<CoinListDto> mockCall;
    @Mock
    private Callback<CoinListDto> callback;

    private CoinMarketCapApiClient apiGateway;

    @Before
    public void prepare() {
        MockitoAnnotations.openMocks(this);
        coinMarketCap = new Api(1, "test", "", "test-api-key");
        apiGateway = new CoinMarketCapApiClient(coinMarketCap, coinMarketCapInterface);
    }

    @Test
    public void testGetCoins_Success() {
        // Arrange: Mock API response
        CoinListDto mockResponse = new CoinListDto(); // Populate with test data if needed
        when(coinMarketCapInterface.getCoins(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockCall);

        // Capture the callback passed to enqueue()
        ArgumentCaptor<Callback<CoinListDto>> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        // Act: Call the method
        apiGateway.getCoins(callbackCaptor.capture(), any(CurrencyType.class));

        // Simulate API success response
        callbackCaptor.getValue().onResponse(mockCall, Response.success(mockResponse));

        // Assert: Verify API was called with correct arguments
        verify(coinMarketCapInterface).getCoins(eq("test-api-key"), eq("1"), eq("300"), eq("EUR"));
    }

    @Test
    public void testGetCoins_Failure() {
        // Arrange: Simulate network request setup
        when(coinMarketCapInterface.getCoins(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockCall);

        // Capture the callback passed to enqueue()
        ArgumentCaptor<Callback<CoinListDto>> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        // Act: Call the method
        apiGateway.getCoins(callbackCaptor.capture(), any(CurrencyType.class));

        // Simulate API failure
        Throwable apiError = new Throwable("API request failed");
        callbackCaptor.getValue().onFailure(mockCall, apiError);

        // Assert: Verify API was called with correct arguments
        verify(coinMarketCapInterface).getCoins(eq("test-api-key"), eq("1"), eq("300"), eq("EUR"));
    }

    @Test
    public void test_get() throws InterruptedException {
        boolean[] success = {false}; // Use an array since lambdas can't modify local variables
        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(i -> { success[0] = true; latch.countDown(); return null; })
                .when(mockCall).enqueue(any(Callback.class));
        when(coinMarketCapInterface.getCoins(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockCall);

        apiGateway.getCoins(callback, CurrencyType.EUR);
        Throwable apiError = new Throwable("API request failed");
        callback.onFailure(mockCall, apiError);


        // ✅ Wait up to 5 seconds for response
        boolean completed = latch.await(5, TimeUnit.SECONDS);

        assertTrue("API call did not complete in time!", completed);
        assertTrue("API call should be successful!", success[0]);
    }
}
