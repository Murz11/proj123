package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.sfedu.tripshelperpack.models.User;
import ru.sfedu.tripshelperpack.service.UserService;
import ru.sfedu.tripshelperpack.utils.ConfigurationUtil;
import ru.sfedu.tripshelperpack.utils.DatabaseUtil;

import java.sql.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = new UserService();

    @BeforeEach
    void cleanDatabase() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    void testCRUDOperations() throws SQLException {


        // Create
        User user = new User(1L, "John Doe", "john.doe@example.com", 101L);
        userService.createUser(user);

        // Read
        User retrievedUser = userService.getUser(1L);
        assertNotNull(retrievedUser);
        assertEquals("John Doe", retrievedUser.getName());

        // Update
        retrievedUser.setName("Jane Doe");
        retrievedUser.setTrip(202L);
        userService.updateUser(retrievedUser);

        User updatedUser = userService.getUser(1L);
        assertEquals("Jane Doe", updatedUser.getName());

        // Delete
        //userService.deleteUser(1L);
        //assertNull(userService.getUser(1L));
    }
}
