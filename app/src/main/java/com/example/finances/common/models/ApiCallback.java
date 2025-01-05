package com.example.finances.common.models;

import com.example.finances.common.interfaces.IApiCallback;

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
