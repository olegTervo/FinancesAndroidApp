package com.example.finances.Database.models;

import java.time.LocalDate;

public class ApiDao {
    public int Id;
    public String Name;
    public String Link;
    public String Key;

    public ApiDao(int id, String name, String link, String key) {
        this.Id = id;
        this.Name = name;
        this.Link = link;
        this.Key = key;
    }

    @Override
    public String toString() {
        return Id + " - " + Name;
    }
}
