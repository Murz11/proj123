package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.Test;
import ru.sfedu.tripshelperpack.api.DataProviderXML;
import ru.sfedu.tripshelperpack.models.User;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataProviderXMLTest {

    private final String filePath = "users_test.xml";

    @Test
    void testSaveAndGetUser() throws Exception {
        DataProviderXML<User> dataProvider = new DataProviderXML<>(filePath, User.class);

        User user = new User(1L, "John Doe", "johndoe@example.com", 100L);
        dataProvider.saveRecord(user);

        User retrievedUser = dataProvider.getRecordById(1L);
        assertNotNull(retrievedUser);
        assertEquals(1L, retrievedUser.getId());
        assertEquals("John Doe", retrievedUser.getName());
        assertEquals("johndoe@example.com", retrievedUser.getEmail());
        assertEquals(100L, retrievedUser.getTrip());

        new File(filePath).delete();
    }

    @Test
    void testDeleteUser() throws Exception {
        DataProviderXML<User> dataProvider = new DataProviderXML<>(filePath, User.class);

        User user = new User(1L, "John Doe", "johndoe@example.com", 100L);
        dataProvider.saveRecord(user);

        dataProvider.deleteRecord(1L);

        assertThrows(Exception.class, () -> dataProvider.getRecordById(1L));

        new File(filePath).delete();
    }

    @Test
    void testInitDataSourceWithNoFile() throws Exception {
        DataProviderXML<User> dataProvider = new DataProviderXML<>(filePath, User.class);

        List<User> users = dataProvider.initDataSource();

        assertNotNull(users);
        assertTrue(users.isEmpty());

        new File(filePath).delete();
    }

    @Test
    void testSaveMultipleUsers() throws Exception {
        DataProviderXML<User> dataProvider = new DataProviderXML<>(filePath, User.class);

        User user1 = new User(1L, "Alice Smith", "alice@example.com", 101L);
        User user2 = new User(2L, "Bob Johnson", "bob@example.com", 102L);
        dataProvider.saveRecord(user1);
        dataProvider.saveRecord(user2);

        User retrievedUser1 = dataProvider.getRecordById(1L);
        User retrievedUser2 = dataProvider.getRecordById(2L);

        assertNotNull(retrievedUser1);
        assertEquals("Alice Smith", retrievedUser1.getName());

        assertNotNull(retrievedUser2);
        assertEquals("Bob Johnson", retrievedUser2.getName());

        // Удаляем тестовый файл
        new File(filePath).delete();
    }
}