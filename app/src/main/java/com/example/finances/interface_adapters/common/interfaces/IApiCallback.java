package com.example.finances.interface_adapters.common.interfaces;

public interface IApiCallback {
    void onSuccess();
    void onFailure(Throwable t);
}
