package com.example.finances.domain.interfaces;

public interface IShopRepository {
    long GetShopId(String name);
    long OpenShop(String name, int accountNumber);
}
