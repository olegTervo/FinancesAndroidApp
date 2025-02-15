package com.example.finances.domain.models;

import com.example.finances.frameworks_and_drivers.database.api.ApiDao;
import com.example.finances.domain.enums.ApiType;

public class Api {
    private int id;
    private String name;
    private String link;
    private String key;

    public Api(int id, String name, String link, String key) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.key = key;
    }

    public Api(Api other) {
        this.id = other.getId();
        this.name = other.getName();
        this.link = other.getLink();
        this.key = other.getKey();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return this.key;
    }

    public ApiType getType() {
        return ApiType.fromInt(this.id);
    }

    public String getLink() {
        return this.link;
    }
}
