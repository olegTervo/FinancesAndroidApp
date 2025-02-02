package com.example.finances.infrastructure.common.models;

import com.example.finances.presentation.activities.InvestmentActivity;

public class ShowCoolEventListener extends EventListenerBase{
    private InvestmentActivity view;

    public ShowCoolEventListener(InvestmentActivity view) {
        this.view = view;
    }

    @Override
    public void onEventOccurred() {
    }
}
