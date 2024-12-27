package ru.sfedu.tripshelperpack.service;

import ru.sfedu.tripshelperpack.dao.TransportDAO;
import ru.sfedu.tripshelperpack.models.Transport;

import java.sql.SQLException;
import java.util.List;

public class TransportService {

    private final TransportDAO transportDAO;

    public TransportService() {
        this.transportDAO = new TransportDAO();
    }

    public Transport getTransportById(Long id) {
        Transport transport = transportDAO.getTransportById(id);
        if (transport == null) {
            System.out.println("Транспорт с ID " + id + " не найден.");
        }
        return transport;
    }

    public List<Transport> getAllTransports() {
        List<Transport> transports = transportDAO.getAllTransports();
        if (transports.isEmpty()) {
            System.out.println("Список транспорта пуст.");
        }
        return transports;
    }

    public void addTransport(Transport transport) {
        transportDAO.addTransport(transport);
        System.out.println("Транспорт успешно добавлен!");
    }

    public void updateTransport(Transport transport) {
        if (transportDAO.getTransportById(transport.getId()) != null) {
            transportDAO.updateTransport(transport);
            System.out.println("Транспорт с ID " + transport.getId() + " успешно обновлён.");
        } else {
            System.out.println("Транспорт с ID " + transport.getId() + " не найден.");
        }
    }

    public void deleteTransport(Long id) {
        if (transportDAO.getTransportById(id) != null) {
            transportDAO.deleteTransport(id);
            System.out.println("Транспорт с ID " + id + " успешно удалён.");
        } else {
            System.out.println("Транспорт с ID " + id + " не найден.");
        }
    }

    public void clearTable() throws SQLException {
        transportDAO.clearTable();
    }
}
