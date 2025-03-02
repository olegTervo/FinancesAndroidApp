package com.example.finances.domain.services;

import com.example.finances.domain.interfaces.IApiRepository;
import com.example.finances.domain.interfaces.IInvestmentRepository;
import com.example.finances.domain.interfaces.IPriceRepository;
import com.example.finances.domain.interfaces.IValueDateRepository;
import com.example.finances.domain.interfaces.api.ICoinMarketCapApi;
import com.example.finances.domain.models.Api;
import com.example.finances.domain.models.Price;
import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.domain.interfaces.api.IApiCallback;
import com.example.finances.domain.enums.InvestmentType;
import com.example.finances.domain.enums.PriceType;
import com.example.finances.domain.enums.ValueDateType;
import com.example.finances.domain.models.ApiInvestment;
import com.example.finances.domain.models.HistoryPrice;
import com.example.finances.domain.models.Investment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InvestmentsService {
    private ICoinMarketCapApi coinMarkerCapApi;

    private IInvestmentRepository investmentRepository;
    private IApiRepository apiRepository;
    private IPriceRepository priceRepository;
    private IValueDateRepository valueDateRepository;
    private DatabaseHelper db;

    @Inject
    public InvestmentsService(
            ICoinMarketCapApi coinMarkerCupApi,
            IInvestmentRepository investmentRepository,
            IApiRepository apiRepository,
            IPriceRepository priceRepository,
            IValueDateRepository valueDateRepository,
            DatabaseHelper db)
    {
        this.coinMarkerCapApi = coinMarkerCupApi;
        this.investmentRepository = investmentRepository;
        this.apiRepository = apiRepository;
        this.priceRepository = priceRepository;
        this.valueDateRepository = valueDateRepository;
        this.db = db;
    }

    public void sync(IApiCallback callback) {
        List<ApiInvestment> toSync = getInvestments()
                .stream()
                .filter(i -> i instanceof ApiInvestment
                        //&& (i.getLastPrice() == null
                        //    || i.getLastPrice().GetModified().getDayOfYear() < LocalDate.now().getDayOfYear()
                        //    || i.getLastPrice().GetModified().getYear() < LocalDate.now().getYear())
                )
                .map(i -> (ApiInvestment) i).collect(Collectors.toList());

        if(!toSync.isEmpty()) {
            IApiCallback c = new IApiCallback() {
                @Override
                public void onSuccess() {
                    processInvestments();
                    callback.onSuccess();
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }
            };

            this.coinMarkerCapApi.syncPricesAsync(toSync, c);
        }
        else {
            callback.onSuccess();
        }
    }

    public Investment getInvestment(long id) {
        Investment result = investmentRepository.GetInvestment(id);
        List<Price> history = priceRepository.GetPrices(result.getId(), PriceType.Investment, 0);
        result.setHistory(history);

        return result;
    }

    public Investment createInvestment(String name, float amount, float price) {
        Investment newInvestment = investmentRepository.CreateInvestment(name, amount);
        priceRepository.CreatePrice(newInvestment.getId(), PriceType.Investment, 0, price);

        return newInvestment;
    }

    public Investment updateInvestment(Investment updated) {
        return investmentRepository.UpdateInvestment(updated);
    }

    public boolean deleteInvestment(long id) {
        return investmentRepository.DeleteInvestment(id);
    }


    public void processInvestments() {
        List<Investment> list = getInvestments();
        HistoryPrice last = (HistoryPrice) valueDateRepository.getFirst(ValueDateType.Investments);

        if (last == null
                || last.date.getDayOfYear() < LocalDate.now().getDayOfYear()
                || last.date.getYear() < LocalDate.now().getYear()) {
            float toAdd = 0;

            for (Investment investment : list) {
                if (investment.getLastPrice() != null)
                    toAdd += investment.getLastPrice().GetPrice()*investment.getAmount();
            }

            valueDateRepository.create(toAdd, LocalDate.now(), ValueDateType.Investments);
        }
        else {
            float toAdd = 0;

            for (Investment investment : list) {
                if (investment.getLastPrice() != null)
                    toAdd += investment.getLastPrice().GetPrice()*investment.getAmount();
            }
            toAdd -= last.value;

            valueDateRepository.increaseTopValue(toAdd, ValueDateType.Investments);
        }
    }

    public List<Investment> getInvestments() {
        List<Investment> investments = new ArrayList<>();
        List<Investment> apiInvestments = new ArrayList<>();

        investments.addAll(investmentRepository.SearchInvestments(InvestmentType.Manual));
        apiInvestments.addAll(investmentRepository.SearchInvestments(InvestmentType.ApiLinked));

        for (Investment investment : apiInvestments) {
            if (investment instanceof ApiInvestment) {
                Api api = apiRepository.GetApi(((ApiInvestment) investment).getApiId());
                ApiInvestment toAdd = new ApiInvestment(investment, api, ((ApiInvestment) investment).getApiSpecificInvestmentName());
                investments.add(toAdd);
            }
        }

        for (Investment investment : investments) {
            investment.setHistory(priceRepository.GetPrices(investment.getId(), PriceType.Investment, 0));
        }

        return investments;
    }
}
