package ru.sfedu.tripshelperpack.dao;

import ru.sfedu.tripshelperpack.models.Attraction;
import ru.sfedu.tripshelperpack.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttractionDAO {

    // Добавление новой достопримечательности
    public void insertAttraction(Attraction attraction) throws SQLException {
        String query = "INSERT INTO attractions (id, name, location, rating) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, attraction.getId());
            stmt.setString(2, attraction.getName());
            stmt.setString(3, attraction.getLocation());
            stmt.setDouble(4, attraction.getRating());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при добавлении достопримечательности: " + e.getMessage(), e);
        }
    }

    // Обновление достопримечательности
    public void updateAttraction(Attraction attraction) throws SQLException {
        String query = "UPDATE attractions SET name = ?, location = ?, rating = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, attraction.getName());
            stmt.setString(2, attraction.getLocation());
            stmt.setDouble(3, attraction.getRating());
            stmt.setLong(4, attraction.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при обновлении достопримечательности: " + e.getMessage(), e);
        }
    }

    // Получение достопримечательности по ID
    public Attraction getAttractionById(Long id) throws SQLException {
        String query = "SELECT * FROM attractions WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return new Attraction(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("location"),
                        result.getDouble("rating")
                );
            }
            return null;
        }
    }

    // Получение всех достопримечательностей
    public List<Attraction> getAllAttractions() throws SQLException {
        String query = "SELECT FROM attractions";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(query)) {

            List<Attraction> attractions = new ArrayList<>();
            while (result.next()) {
                attractions.add(new Attraction(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("location"),
                        result.getDouble("rating")
                ));
            }
            return attractions;
        }
    }
    public void deleteAttractionById(long id) throws SQLException {
        String sql = "DELETE FROM attractions WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public void clearTable() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM attractions");
        }
    }
}
