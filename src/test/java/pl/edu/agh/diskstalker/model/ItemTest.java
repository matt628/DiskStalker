package pl.edu.agh.diskstalker.model;

import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.executor.QueryExecutor;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemTest {

    @BeforeAll
    public static void init() {
        ConnectionProvider.init("jdbc:sqlite:disk_stalker_test.db");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        QueryExecutor.delete("DELETE FROM Roots");
        QueryExecutor.delete("DELETE FROM Items");
    }

    @AfterAll
    public static void cleanUp() throws SQLException {
        ConnectionProvider.close();
    }

    @Test
    public void createItemTest() {
        // When
        var root = getMockedRoot();

        var item1 = Item.create("folder1", "/home", null, "234", root);
        var item2 = Item.create("file1", "/home/folder1", "jpg", "42", root);
        var redundantItem = Item.create("folder1", "/home", null, "234", root);

        // Then
        checkItem(item1);
        checkItem(item2);

        assertNotEquals(item1.get().getId(), item2.get().getId());
        assertFalse(redundantItem.isPresent());
    }

    @Test
    public void findItemTest() {
        // When
        var root = getMockedRoot();

        var item = Item.create("folder1", "/home/temp", null, "234", root);
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
        var root = getMockedRoot();

        var item = Item.create("folder1", "/home/temp", null, "234", root);
        var foundItemById = Item.findByLocation(item.get().getName(), item.get().getPath());
        var nonExistingItem = Item.findByLocation(null, null);

        // Then
        checkItem(foundItemById);

        Assertions.assertEquals(item.get(), foundItemById.get());
        Assertions.assertFalse(nonExistingItem.isPresent());
    }

    private Root getMockedRoot() {
        return Root.create("some", "/home", "234342", "4545323").get();
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
