package ru.sfedu.tripshelperpack.service;

import ru.sfedu.tripshelperpack.dao.AttractionDAO;
import ru.sfedu.tripshelperpack.models.Attraction;

import java.sql.SQLException;
import java.util.List;

public class AttractionService {
    private final AttractionDAO attractionDAO;

    public AttractionService() {
        this.attractionDAO = new AttractionDAO();
    }

    // Добавление новой достопримечательности
    public void addAttraction(Attraction attraction) throws SQLException {
        try {
            attractionDAO.insertAttraction(attraction);
            System.out.println("Достопримечательность успешно добавлена!");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении достопримечательности: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAttraction(Long id) throws SQLException {
        attractionDAO.deleteAttractionById(id);
    }
    // Получение достопримечательности по ID
    public Attraction getAttractionById(Long attractionId) throws SQLException {
        try {
            Attraction attraction = attractionDAO.getAttractionById(attractionId);
            if (attraction == null) {
                System.out.println("Достопримечательность с ID " + attractionId + " не найдена.");
            }
            return attraction;
        } catch (SQLException e) {
            System.err.println("Ошибка при получении достопримечательности: " + e.getMessage());
            throw e;
        }
    }

    // Получение списка всех достопримечательностей
    public List<Attraction> getAllAttractions() throws SQLException {
        try {
            return attractionDAO.getAllAttractions();
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка достопримечательностей: " + e.getMessage());
            throw e;
        }
    }

    public void updateAttraction(Attraction attraction) throws SQLException {
        try {
            attractionDAO.updateAttraction(attraction);
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении списка достопримечательностей: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        attractionDAO.clearTable();
    }
}
