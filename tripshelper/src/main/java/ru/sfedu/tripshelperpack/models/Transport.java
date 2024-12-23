package ru.sfedu.tripshelperpack.models;

// Модель Transport
public class Transport {
    private Long id;
    private String type;
    private String schedule;
    private String route;

    // Конструкторы
    public Transport() {
    }

    public Transport(Long id, String type, String schedule, String route) {
        this.id = id;
        this.type = type;
        this.schedule = schedule;
        this.route = route;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
