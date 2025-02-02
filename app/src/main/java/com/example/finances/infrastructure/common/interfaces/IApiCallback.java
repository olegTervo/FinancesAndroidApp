package com.example.finances.infrastructure.common.interfaces;

public interface IApiCallback {
    void onSuccess();
    void onFailure(Throwable t);
}
