package pl.edu.agh.diskstalker;

import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.executor.QueryExecutor;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// TODO Separate this class to ItemTest and RootTest
public class DatabaseTest {

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
    public void createRootTest() {
        // When
        var root1 = Root.create("some", "/home", "234342", "4545323");
        var root2 = Root.create("other", "/home/loc", "3454334", "45434334");
        var redundantRoot = Root.create("other", "/home/loc", "3454334", "45434334");

        // Then
        checkRoot(root1);
        checkRoot(root2);

        assertNotEquals(root1.get().getId(), root2.get().getId());
        assertFalse(redundantRoot.isPresent());
    }

    @Test
    public void findRootTest() {
        // When
        var root = Root.create("some", "/home", "234342", "4545323");
        var foundRootById = Root.findById(root.get().getId());
        var nonExistingRoot = Root.findById(Integer.MAX_VALUE);

        // Then
        checkRoot(foundRootById);

        Assertions.assertEquals(root.get(), foundRootById.get());
        Assertions.assertFalse(nonExistingRoot.isPresent());
    }

    @Test
    public void findRootByLocationTest() {
        // When
        var root = Root.create("some", "/home", "234342", "4545323");
        var foundRootByLocation = Root.findByLocation(root.get().getName(), root.get().getPath());
        var nonExistingRoot = Root.findByLocation(null, null);

        // Then
        checkRoot(foundRootByLocation);

        Assertions.assertEquals(root.get(), foundRootByLocation.get());
        Assertions.assertFalse(nonExistingRoot.isPresent());
    }

    @Test
    public void createItemTest() {
        // When
        var root = Root.create("some", "/home", "234342", "4545323");

        var item1 = Item.create("folder1", "/home", null, "234", root.get());
        var item2 = Item.create("file1", "/home/folder1", "jpg", "42", root.get());
        var redundantItem = Item.create("folder1", "/home", null, "234", root.get());

        // Then
        checkItem(item1);
        checkItem(item2);

        assertNotEquals(item1.get().getId(), item2.get().getId());
        assertFalse(redundantItem.isPresent());
    }

    @Test
    public void findItemTest() {
        // When
        var root = Root.create("some", "/home", "234342", "4545323");

        var item = Item.create("folder1", "/home/temp", null, "234", root.get());
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
        var root = Root.create("some", "/home", "234342", "4545323");

        var item = Item.create("folder1", "/home/temp", null, "234", root.get());
        var foundItemById = Item.findByLocation(item.get().getName(), item.get().getPath());
        var nonExistingItem = Item.findByLocation(null, null);

        // Then
        checkItem(foundItemById);

        Assertions.assertEquals(item.get(), foundItemById.get());
        Assertions.assertFalse(nonExistingItem.isPresent());
    }

    private void checkRoot(final Optional<Root> root) {
        assertTrue(root.isPresent());
        root.ifPresent(r -> {
            assertTrue(r.getId() > 0);
            assertNotNull(r.getName());
            assertNotNull(r.getPath());
            assertNotNull(r.getSize());
            assertNotNull(r.getMaxSize());
        });
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
