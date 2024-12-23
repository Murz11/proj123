package ru.sfedu.tripshelperpack.models;

// Модель Attraction
public class Attraction {
    private Long id;
    private String name;
    private String location;
    private Double rating;

    // Конструкторы
    public Attraction() {
    }

    public Attraction(Long id, String name, String location, Double rating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    // Геттеры и сеттеры
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
