package com.example.finances.domain.models.api.CoinMarketCap;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote implements Serializable
{

    @SerializedName("EUR")
    @Expose
    private EUR eur;
    private final static long serialVersionUID = -5780538494495942860L;

    public EUR getEUR() {
        return eur;
    }

    public void setEUR(EUR eur) {
        this.eur = eur;
    }

}