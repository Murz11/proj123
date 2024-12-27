package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.tripshelperpack.models.Attraction;
import ru.sfedu.tripshelperpack.service.AttractionService;
import ru.sfedu.tripshelperpack.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AttractionServiceTest {
    private AttractionService attractionService;

    @BeforeEach
    void setUp() {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM attractions");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        attractionService = new AttractionService();
    }

    @Test
    void testAddAttraction() {
        try {
            // Добавляем достопримечательность
            Attraction attraction = new Attraction(1L, "Eiffel Tower", "Paris, France", 4.8);
            attractionService.addAttraction(attraction);

            // Проверяем, что достопримечательность была добавлена
            Attraction retrievedAttraction = attractionService.getAttractionById(1L);
            assertNotNull(retrievedAttraction);
            assertEquals("Eiffel Tower", retrievedAttraction.getName());
            assertEquals("Paris, France", retrievedAttraction.getLocation());
            assertEquals(4.8, retrievedAttraction.getRating());
        } catch (SQLException e) {
            fail("Ошибка выполнения теста: " + e.getMessage());
        }
    }

    @Test
    void testGetAttractionById_NotFound() {
        try {
            Attraction attraction = attractionService.getAttractionById(12L);
            assertNull(attraction);
        } catch (SQLException e) {
            fail("Ошибка выполнения теста: " + e.getMessage());
        }
    }

    @Test
    void testGetAllAttractions() {
        try {
            // Добавляем несколько достопримечательностей
            Attraction attraction1 = new Attraction(1L, "Eiffel Tower", "Paris, France", 4.8);
            Attraction attraction2 = new Attraction(2L, "Louvre Museum", "Paris, France", 4.7);
            attractionService.addAttraction(attraction1);
            attractionService.addAttraction(attraction2);

            // Проверяем, что они были добавлены
            assertEquals(2, attractionService.getAllAttractions().size());
        } catch (SQLException e) {
            fail("Ошибка выполнения теста: " + e.getMessage());
        }
    }
}
