package ru.sfedu.tripshelperpack.models;

import java.util.List;

// Модель Trip
public class Trip {
    private Long id;
    private String startLocation;
    private String endLocation;
    private List<Attraction> selectedAttractions;
    private String tripSchedule;

    // Конструкторы
    public Trip() {
    }

    public Trip(Long id, String startLocation, String endLocation, List<Attraction> selectedAttractions, String tripSchedule) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.selectedAttractions = selectedAttractions;
        this.tripSchedule = tripSchedule;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public List<Attraction> getSelectedAttractions() {
        return selectedAttractions;
    }

    public void setSelectedAttractions(List<Attraction> selectedAttractions) {
        this.selectedAttractions = selectedAttractions;
    }

    public String getTripSchedule() {
        return tripSchedule;
    }

    public void setTripSchedule(String tripSchedule) {
        this.tripSchedule = tripSchedule;
    }
}
