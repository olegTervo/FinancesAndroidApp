package com.example.finances.domain.models.api;

import java.io.Serializable;
import java.util.List;

import com.example.finances.domain.models.api.CoinMarketCap.Datum;
import com.example.finances.domain.models.api.CoinMarketCap.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinListDto implements Serializable
{

    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = -4369048252305703014L;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
