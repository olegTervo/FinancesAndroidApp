package com.example.finances.interface_adapters.common.models;

import com.example.finances.presentation.activities.InvestmentActivity;

public class InvestmentsSyncedEventListener extends EventListenerBase{
    private InvestmentActivity view;

    public InvestmentsSyncedEventListener(InvestmentActivity view) {
        this.view = view;
    }

    @Override
    public void onEventOccurred() {
    }
}
