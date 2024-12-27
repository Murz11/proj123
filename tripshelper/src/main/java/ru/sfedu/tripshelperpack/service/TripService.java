package ru.sfedu.tripshelperpack.service;

import ru.sfedu.tripshelperpack.dao.TripDAO;
import ru.sfedu.tripshelperpack.models.Trip;

import java.sql.SQLException;
import java.util.List;

public class TripService {
    private final TripDAO tripDAO = new TripDAO();

    public void addTrip(Trip trip) throws SQLException {
        tripDAO.addTrip(trip);
    }

    public List<Trip> getAllTrips() throws SQLException {
        return tripDAO.getAllTrips();
    }

    public Trip getTripById(Long id) throws SQLException {
        return tripDAO.getTripById(id);
    }

    public void deleteTrip(Long id) throws SQLException {
        tripDAO.deleteTripById(id);
    }

    public void clearTable() throws SQLException {
        tripDAO.clearTable();
    }

    public void updateTrip(Trip trip) throws SQLException {
        tripDAO.updateTrip(trip);
    }
}
