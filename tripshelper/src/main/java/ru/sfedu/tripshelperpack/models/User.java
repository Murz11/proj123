package ru.sfedu.tripshelperpack.models;

import com.opencsv.bean.CsvBindByName;

public class User {
    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "NAME")
    private String name;

    @CsvBindByName(column = "EMAIL")
    private String email;

    @CsvBindByName(column = "TRIPID")
    private Long trip;

    public User() {
    }

    public User(Long id, String name, String email, Long trip) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.trip = trip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTrip() {
        return trip;
    }

    public void setTrip(Long trip) {
        this.trip = trip;
    }
}
