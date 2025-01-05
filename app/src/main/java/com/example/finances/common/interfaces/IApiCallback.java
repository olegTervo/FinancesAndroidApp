package com.example.finances.common.interfaces;

public interface IApiCallback {
    void onSuccess();
    void onFailure(Throwable t);
}
