package ru.sfedu.tripshelperpack.dao;

import ru.sfedu.tripshelperpack.models.Transport;
import ru.sfedu.tripshelperpack.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportDAO {

    public Transport getTransportById(Long id) {
        String query = "SELECT * FROM transport WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Transport(
                        resultSet.getLong("id"),
                        resultSet.getString("type"),
                        resultSet.getString("schedule"),
                        resultSet.getString("route")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transport> getAllTransports() {
        String query = "SELECT * FROM transport";
        List<Transport> transports = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                transports.add(new Transport(
                        resultSet.getLong("id"),
                        resultSet.getString("type"),
                        resultSet.getString("schedule"),
                        resultSet.getString("route")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public void addTransport(Transport transport) {
        String query = "INSERT INTO transport (type, schedule, route) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, transport.getType());
            statement.setString(2, transport.getSchedule());
            statement.setString(3, transport.getRoute());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    transport.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransport(Transport transport) {
        String query = "UPDATE transport SET type = ?, schedule = ?, route = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, transport.getType());
            statement.setString(2, transport.getSchedule());
            statement.setString(3, transport.getRoute());
            statement.setLong(4, transport.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransport(Long id) {
        String query = "DELETE FROM transport WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearTable() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM transport");
        }
    }
}
