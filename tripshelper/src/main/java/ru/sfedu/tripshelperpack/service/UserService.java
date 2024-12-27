package ru.sfedu.tripshelperpack.service;

import ru.sfedu.tripshelperpack.dao.UserDAO;
import ru.sfedu.tripshelperpack.models.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void createUser(User user) throws SQLException {
        userDAO.insertUser(user);
    }

    public void deleteUser(long id) throws SQLException {
        userDAO.deleteUser(id);
    }

    public User getUser(long id) throws SQLException {
        return userDAO.getUserById(id);
    }
    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public void clearTable() throws SQLException {
        userDAO.clearTable();
    }
}
