package com.example.finances.domain.models.api;

import com.example.finances.domain.interfaces.api.IApiCallback;

public class ApiCallback implements IApiCallback {

    public ApiCallback() {
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(Throwable t) {
        System.err.println("Call failed");
    }
}
