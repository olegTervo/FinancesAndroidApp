package com.example.finances.infrastructure.common.models;

import com.example.finances.infrastructure.common.interfaces.IApiCallback;

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
