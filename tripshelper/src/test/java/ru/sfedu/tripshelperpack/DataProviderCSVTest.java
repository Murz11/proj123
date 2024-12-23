package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.*;
import ru.sfedu.tripshelperpack.models.User;
import ru.sfedu.tripshelperpack.api.DataProviderCSV;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataProviderCSVTest {
    private static final String CSV_FILE_PATH = "test_users.csv";
    private DataProviderCSV<User> dataProvider;

    @BeforeEach
    void setUp() {
        dataProvider = new DataProviderCSV<>(CSV_FILE_PATH, User.class);
        File file = new File(CSV_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveRecord() throws Exception {
        User user = new User(1L, "John Doe", "john.doe@example.com", 101L);
        dataProvider.saveRecord(user);


        List<User> users = dataProvider.initDataSource();



        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());
    }

    @Test
    void testDeleteRecord() throws Exception {
        User user1 = new User(1L, "John Doe", "john.doe@example.com", 101L);
        User user2 = new User(2L, "Jane Doe", "jane.doe@example.com", 102L);
        dataProvider.saveRecord(user1);
        dataProvider.saveRecord(user2);

        dataProvider.deleteRecord(1L);
        List<User> users = dataProvider.initDataSource();
        assertEquals(1, users.size());
        assertEquals("Jane Doe", users.get(0).getName());
    }

    @Test
    void testGetRecordById() throws Exception {
        User user1 = new User(1L, "John Doe", "john.doe@example.com", 101L);
        User user2 = new User(2L, "Jane Doe", "jane.doe@example.com", 102L);
        dataProvider.saveRecord(user1);
        dataProvider.saveRecord(user2);

        User foundUser = dataProvider.getRecordById(2L);
        assertNotNull(foundUser);
        assertEquals("Jane Doe", foundUser.getName());
    }

    @Test
    void testInitDataSource() throws Exception {
        User user1 = new User(1L, "John Doe", "john.doe@example.com", 101L);
        User user2 = new User(2L, "Jane Doe", "jane.doe@example.com", 102L);
        dataProvider.saveRecord(user1);
        dataProvider.saveRecord(user2);

        List<User> users = dataProvider.initDataSource();
        assertEquals(2, users.size());
    }
}
