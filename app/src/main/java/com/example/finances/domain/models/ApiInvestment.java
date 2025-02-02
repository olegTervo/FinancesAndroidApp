package com.example.finances.domain.models;

import com.example.finances.domain.enums.InvestmentType;

import java.time.LocalDate;

public class ApiInvestment extends Investment {
    private String apiSpecificInvestmentName;
    private Api api;

    public ApiInvestment(Investment investment, Api api, String name) {
        super(investment);
        this.api = api;
        this.apiSpecificInvestmentName = name;
    }

    public ApiInvestment(
            int investmentId,
            String investmentName,
            int amount,
            LocalDate openDate,
            LocalDate modified,
            InvestmentType type,

            int apiId,
            String apiName,
            String link,
            String key,

            String name) {

        super(investmentId, investmentName, amount, openDate, modified, type);
        this.api = new Api(apiId, apiName, link, key);
        this.apiSpecificInvestmentName = name;

    }

    public int getId() {
        return super.getId();
    }

    public Api getApi() {
        return this.api;
    }

    public String getApiSpecificInvestmentName() {
        return this.apiSpecificInvestmentName;
    }
}
