package com.example.finances.domain.interfaces.api;

public interface IApiCallback {
    void onSuccess();
    void onFailure(Throwable t);
}
