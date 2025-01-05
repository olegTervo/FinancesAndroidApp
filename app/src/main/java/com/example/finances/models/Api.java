package com.example.finances.models;

import com.example.finances.Database.models.ApiDao;
import com.example.finances.enums.ApiType;

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

    public Api(ApiDao dao) {
        this.id = dao.Id;
        this.name = dao.Name;
        this.link = dao.Link;
        this.key = dao.Key;
    }

    public ApiType getType() {
        return ApiType.fromInt(this.id);
    }
}
