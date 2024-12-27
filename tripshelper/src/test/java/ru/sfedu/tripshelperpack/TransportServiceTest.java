package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sfedu.tripshelperpack.models.Transport;
import ru.sfedu.tripshelperpack.service.TransportService;
import ru.sfedu.tripshelperpack.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportServiceTest {

    private TransportService transportService;

    @BeforeEach
    void setUp() throws Exception {
        // Инициализация сервиса
        transportService = new TransportService();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM transport");
        }
    }


    @Test
    void testAddTransport() {
        Transport transport = new Transport(null, "Bus", "Daily", "Route A");
        transportService.addTransport(transport);

        // Проверяем, что транспорт добавился
        assertNotNull(transport.getId());
        Transport result = transportService.getTransportById(transport.getId());
        assertNotNull(result);
        assertEquals("Bus", result.getType());
        assertEquals("Daily", result.getSchedule());
        assertEquals("Route A", result.getRoute());
    }

    @Test
    void testGetTransportById() {
        Transport transport = new Transport(null, "Train", "Morning", "Route B");
        transportService.addTransport(transport);

        // Проверяем, что можно получить транспорт по ID
        Transport result = transportService.getTransportById(transport.getId());
        assertNotNull(result);
        assertEquals("Train", result.getType());
        assertEquals("Morning", result.getSchedule());
        assertEquals("Route B", result.getRoute());
    }

    @Test
    void testGetAllTransports() {
        // Добавляем несколько транспортов
        transportService.addTransport(new Transport(null, "Bus", "Daily", "Route A"));
        transportService.addTransport(new Transport(null, "Train", "Morning", "Route B"));

        // Проверяем, что они возвращаются в списке
        List<Transport> transports = transportService.getAllTransports();
        assertEquals(2, transports.size());
    }

    @Test
    void testUpdateTransport() {
        // Добавляем транспорт
        Transport transport = new Transport(null, "Bus", "Daily", "Route A");
        transportService.addTransport(transport);

        // Обновляем его
        transport.setType("Updated Bus");
        transport.setSchedule("Evening");
        transportService.updateTransport(transport);

        // Проверяем, что данные обновились
        Transport updatedTransport = transportService.getTransportById(transport.getId());
        assertNotNull(updatedTransport);
        assertEquals("Updated Bus", updatedTransport.getType());
        assertEquals("Evening", updatedTransport.getSchedule());
        assertEquals("Route A", updatedTransport.getRoute());
    }

    @Test
    void testDeleteTransport() {
        // Добавляем транспорт
        Transport transport = new Transport(null, "Bus", "Daily", "Route A");
        transportService.addTransport(transport);

        // Удаляем его
        transportService.deleteTransport(transport.getId());

        // Проверяем, что он удалился
        Transport deletedTransport = transportService.getTransportById(transport.getId());
        assertNull(deletedTransport);
    }
}
