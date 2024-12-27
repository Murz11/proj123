package ru.sfedu.tripshelperpack.dao;

import ru.sfedu.tripshelperpack.models.Attraction;
import ru.sfedu.tripshelperpack.models.Trip;
import ru.sfedu.tripshelperpack.models.User;
import ru.sfedu.tripshelperpack.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    // Перенести sql запросы в константы
    // Починить работу с достопримечательностями
    private static final String INSERT_TRIP_SQL = "INSERT INTO trips (id, start_location, end_location, trip_schedule) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_TRIP_SQL = "UPDATE trips SET start_location = ?, end_location = ?, trip_schedule = ? WHERE id = ?";
    private static final String DELETE_TRIP_ATTRACTION_SQL_BY_TRIP_ID = "DELETE FROM trip_attractions WHERE trip_id = ?";
    private static final String INSERT_TRIP_ATTRACTION_SQL = "INSERT INTO trip_attractions (trip_id, attraction_id) VALUES (?, ?)";
    private static final String GET_TRIP_BY_ID_SQL = "SELECT * FROM trips WHERE id = ?";
    private static final String GET_ATTRACTIONS_FOR_TRIP_SQL = """
        SELECT a.id, a.name, a.location, a.rating
        FROM attractions a
        JOIN trip_attractions ta ON a.id = ta.attraction_id
        WHERE ta.trip_id = ?
    """;

    public void addTrip(Trip trip) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false); // Начинаем транзакцию

            // Добавляем путешествие
            try (PreparedStatement tripStatement = connection.prepareStatement(INSERT_TRIP_SQL)) {
                tripStatement.setLong(1, trip.getId());
                tripStatement.setString(2, trip.getStartLocation());
                tripStatement.setString(3, trip.getEndLocation());
                tripStatement.setString(4, trip.getTripSchedule());
                tripStatement.executeUpdate();
            }

            // Добавляем связи путешествия и достопримечательностей
            try (PreparedStatement tripAttractionStatement = connection.prepareStatement(INSERT_TRIP_ATTRACTION_SQL)) {
                for (Attraction attraction : trip.getSelectedAttractions()) {
                    tripAttractionStatement.setLong(1, trip.getId());
                    tripAttractionStatement.setLong(2, attraction.getId());
                    tripAttractionStatement.addBatch();
                }
                tripAttractionStatement.executeBatch();
            }

            connection.commit(); // Фиксируем транзакцию
        } catch (SQLException e) {
            throw new SQLException("Ошибка при добавлении путешествия: " + e.getMessage(), e);
        }
    }

    // Обновляем путешествие
    public void updateTrip(Trip trip) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false); // Начинаем транзакцию

            // Добавляем путешествие
            try (PreparedStatement tripStatement = connection.prepareStatement(UPDATE_TRIP_SQL)) {
                tripStatement.setString(1, trip.getStartLocation());
                tripStatement.setString(2, trip.getEndLocation());
                tripStatement.setString(3, trip.getTripSchedule());
                tripStatement.setLong(4, trip.getId());
                tripStatement.executeUpdate();
            }

            // Удаляем все достопримечательности у путешествия
            try (PreparedStatement tripAttractionStatement = connection.prepareStatement(DELETE_TRIP_ATTRACTION_SQL_BY_TRIP_ID)) {
                tripAttractionStatement.setLong(1, trip.getId());
                tripAttractionStatement.executeUpdate();
            }

            // Добавляем связи путешествия и достопримечательностей заново
            try (PreparedStatement tripAttractionStatement = connection.prepareStatement(INSERT_TRIP_ATTRACTION_SQL)) {
                for (Attraction attraction : trip.getSelectedAttractions()) {
                    tripAttractionStatement.setLong(1, trip.getId());
                    tripAttractionStatement.setLong(2, attraction.getId());
                    tripAttractionStatement.addBatch();
                }
                tripAttractionStatement.executeBatch();
            }

            connection.commit(); // Фиксируем транзакцию
        } catch (SQLException e) {
            throw new SQLException("Ошибка при обновлении путешествия: " + e.getMessage(), e);
        }
    }
    public List<Trip> getAllTrips() throws SQLException {
        List<Trip> trips = new ArrayList<>();
        Trip trip = null;
        String sql = "SELECT * FROM trips";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trip = new Trip();
                trip.setId(rs.getLong("id"));
                trip.setStartLocation(rs.getString("start_location"));
                trip.setEndLocation(rs.getString("end_location"));
                trip.setTripSchedule(rs.getString("trip_schedule"));

                // Получаем достопримечательности для этого путешествия
                try (PreparedStatement attractionStatement = connection.prepareStatement(GET_ATTRACTIONS_FOR_TRIP_SQL)) {
                    attractionStatement.setLong(1, rs.getLong("id"));
                    ResultSet attractionResultSet = attractionStatement.executeQuery();
                    List<Attraction> attractions = new ArrayList<>();
                    while (attractionResultSet.next()) {
                        Attraction attraction = new Attraction(
                                attractionResultSet.getLong("id"),
                                attractionResultSet.getString("name"),
                                attractionResultSet.getString("location"),
                                attractionResultSet.getDouble("rating")
                        );
                        attractions.add(attraction);
                    }
                    trip.setSelectedAttractions(attractions);
                }
                trips.add(trip);
            }
        }
        return trips;
    }

    public void deleteTripById(long id) throws SQLException {
        String sql = "DELETE FROM trips WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    public Trip getTripById(Long id) throws SQLException {
        Trip trip = null;
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement tripStatement = connection.prepareStatement(GET_TRIP_BY_ID_SQL)) {

            tripStatement.setLong(1, id);
            ResultSet tripResultSet = tripStatement.executeQuery();
            if (tripResultSet.next()) {
                trip = new Trip();
                trip.setId(tripResultSet.getLong("id"));
                trip.setStartLocation(tripResultSet.getString("start_location"));
                trip.setEndLocation(tripResultSet.getString("end_location"));
                trip.setTripSchedule(tripResultSet.getString("trip_schedule"));

                // Получаем достопримечательности для этого путешествия
                try (PreparedStatement attractionStatement = connection.prepareStatement(GET_ATTRACTIONS_FOR_TRIP_SQL)) {
                    attractionStatement.setLong(1, id);
                    ResultSet attractionResultSet = attractionStatement.executeQuery();
                    List<Attraction> attractions = new ArrayList<>();
                    while (attractionResultSet.next()) {
                        Attraction attraction = new Attraction(
                                attractionResultSet.getLong("id"),
                                attractionResultSet.getString("name"),
                                attractionResultSet.getString("location"),
                                attractionResultSet.getDouble("rating")
                        );
                        attractions.add(attraction);
                    }
                    trip.setSelectedAttractions(attractions);
                }
            }
        }
        return trip;
    }

    public void clearTable() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM trips");
        }
    }
}
