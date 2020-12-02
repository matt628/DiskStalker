package pl.edu.agh.diskstalker;

import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.executor.QueryExecutor;
import pl.edu.agh.diskstalker.model.Item;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @BeforeAll
    public static void init() {
        ConnectionProvider.init("jdbc:sqlite:disk_stalker_test.db");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        QueryExecutor.delete("DELETE FROM Items");
    }

    @AfterAll
    public static void cleanUp() throws SQLException {
        ConnectionProvider.close();
    }

    @Test
    public void createItemTest() {
        // When
        var item1 = Item.create("folder1", "/home/temp", null, "234");
        var item2 = Item.create("file1", "/home/temp/folder1", "jpg", "42");
        var redundantItem = Item.create("folder1", "/home/temp", null, "234");

        // Then
        checkItem(item1);
        checkItem(item2);

        assertNotEquals(item1.get().getId(), item2.get().getId());
        assertFalse(redundantItem.isPresent());
    }

    @Test
    public void findItemTest() {
        // When
        var item = Item.create("floder1", "/home/temp", null, "234");
        var foundItemById = Item.findById(item.get().getId());
        var nonExistingItem = Item.findById(Integer.MAX_VALUE);

        // Then
        checkItem(foundItemById);

        Assertions.assertEquals(item.get(), foundItemById.get());
        Assertions.assertFalse(nonExistingItem.isPresent());
    }

    @Test
    public void findItemLocationTest() {
        // When
        var item = Item.create("floder1", "/home/temp", null, "234");
        var foundItemById = Item.findByLocation(item.get().getName(), item.get().getPath());
        var nonExistingItem = Item.findByLocation(null, null);

        // Then
        checkItem(foundItemById);

        Assertions.assertEquals(item.get(), foundItemById.get());
        Assertions.assertFalse(nonExistingItem.isPresent());
    }

    private void checkItem(final Optional<Item> item) {
        assertTrue(item.isPresent());
        item.ifPresent(i -> {
            assertTrue(i.getId() > 0);
            assertNotNull(i.getName());
            assertNotNull(i.getPath());
            assertNotNull(i.getSize());
        });
    }
}
