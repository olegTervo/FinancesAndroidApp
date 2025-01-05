package com.example.finances.common.models;

import com.example.finances.InvestmentActivity;

public class ShowCoolEventListener extends EventListenerBase{
    private InvestmentActivity view;

    public ShowCoolEventListener(InvestmentActivity view) {
        this.view = view;
    }

    @Override
    public void onEventOccurred() {
        view.ShowCool();
    }
}
