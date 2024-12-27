package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.tripshelperpack.models.Attraction;
import ru.sfedu.tripshelperpack.models.Trip;
import ru.sfedu.tripshelperpack.service.TripService;
import ru.sfedu.tripshelperpack.service.AttractionService;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TripServiceTest {
    private TripService tripService;
    private AttractionService attractionService;

    @BeforeEach
    void setUp() {
        tripService = new TripService();
        attractionService = new AttractionService();
    }

    @Test
    void testAddTripWithAttractions() {
        try {
            // Добавляем достопримечательности
            Attraction attraction1 = new Attraction(1L, "Eiffel Tower", "Paris, France", 4.8);
            Attraction attraction2 = new Attraction(2L, "Louvre Museum", "Paris, France", 4.7);
            attractionService.addAttraction(attraction1);
            attractionService.addAttraction(attraction2);

            // Создаем путешествие
            Trip trip = new Trip(1L, "London", "Paris", Arrays.asList(attraction1, attraction2), "2024-12-27");

            // Добавляем путешествие
            tripService.addTrip(trip);

            // Проверяем добавление
            Trip retrievedTrip = tripService.getTripById(1L);
            assertNotNull(retrievedTrip);
            assertEquals("London", retrievedTrip.getStartLocation());
            assertEquals("Paris", retrievedTrip.getEndLocation());
            assertEquals(2, retrievedTrip.getSelectedAttractions().size());
        } catch (SQLException e) {
            fail("Ошибка выполнения теста: " + e.getMessage());
        }
    }

    @Test
    void testGetTripById_NotFound() {
        try {
            Trip trip = tripService.getTripById(999L);
            assertNull(trip);
        } catch (SQLException e) {
            fail("Ошибка выполнения теста: " + e.getMessage());
        }
    }
}
