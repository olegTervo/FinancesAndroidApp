package com.example.finances.frameworks_and_drivers.database.api;

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
