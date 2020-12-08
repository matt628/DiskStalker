package pl.edu.agh.diskstalker.model;

import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    public void createTest() {
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
    public void findTest() {
        // When
        var root = getMockedRoot();
        var item = Item.create("folder1", "/home", null, "234", root);

        String sql = "SELECT * FROM Items WHERE ItemID = (?) AND Name = (?) AND Path = (?)";
        Object[] args = { item.get().getId(), item.get().getName(), item.get().getPath() };
        var foundItem = Item.find(args, sql);

        // Then
        checkItem(item);
        checkItem(foundItem);

        assertEquals(item, foundItem);
    }

    @Test
    public void findByIdTest() {
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
    public void findByLocationTest() {
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

    @Test
    public void getNameFromPathname() {
        // When
        String pathname1 = "/home";
        String pathname2 = "/home/folder";
        String pathname3 = "/home/folder/picture.jpg";
        String pathname4 = "/home/folder/file.txt";

        String expectedName1 = "home";
        String expectedName2 = "folder";
        String expectedName3 = "picture";
        String expectedName4 = "file";

        // Then
        Assertions.assertEquals(expectedName1, Item.getNameFromPathname(pathname1));
        Assertions.assertEquals(expectedName2, Item.getNameFromPathname(pathname2));
        Assertions.assertEquals(expectedName3, Item.getNameFromPathname(pathname3));
        Assertions.assertEquals(expectedName4, Item.getNameFromPathname(pathname4));
    }

    @Test
    public void getPathFromPathname() {
        // When
        String pathname1 = "/home";
        String pathname2 = "/home/folder";
        String pathname3 = "/home/folder/picture.jpg";
        String pathname4 = "/home/folder/file.txt";

        String expectedPath1 = "/";
        String expectedPath2 = "/home/";
        String expectedPath3 = "/home/folder/";
        String expectedPath4 = "/home/folder/";

        // Then
        Assertions.assertEquals(expectedPath1, Item.getPathFromPathname(pathname1));
        Assertions.assertEquals(expectedPath2, Item.getPathFromPathname(pathname2));
        Assertions.assertEquals(expectedPath3, Item.getPathFromPathname(pathname3));
        Assertions.assertEquals(expectedPath4, Item.getPathFromPathname(pathname4));
    }

    @Test
    public void getChildrenTest() {
        // When
        var root = getMockedRoot();

        var item1 = Item.create("folder1", "/home/", null, "234", root);
        var item2 = Item.create("folder2", "/home/", null, "245", root);
        var item3 = Item.create("file", "/home/", "jpg", "542", root);
        var item4 = Item.create("folder", "/home/folder2/", null, "54", root);
        var item5 = Item.create("file", "/home/folder1/", "jpg", "42", root);

        List<Item> children1 = Item.getChildren("/home/folder1");

        Assertions.assertNotNull(children1);
        Assertions.assertEquals(1, children1.size());
        Assertions.assertTrue(children1.contains(item5.get()));
    }

    private Root getMockedRoot() {
        return Root.create("home", "/", "234342", "4545323").get();
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
