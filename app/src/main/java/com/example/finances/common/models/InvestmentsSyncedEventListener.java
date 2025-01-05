package com.example.finances.common.models;

import android.view.View;

import com.example.finances.InvestmentActivity;

public class InvestmentsSyncedEventListener extends EventListenerBase{
    private InvestmentActivity view;

    public InvestmentsSyncedEventListener(InvestmentActivity view) {
        this.view = view;
    }

    @Override
    public void onEventOccurred() {
        //view.refresh();
        view.ShowCool();
    }
}
