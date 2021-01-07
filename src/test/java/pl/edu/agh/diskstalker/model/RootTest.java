package pl.edu.agh.diskstalker.model;

import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.database.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.database.executor.QueryExecutor;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RootTest {

    @BeforeAll
    public static void init() {
        ConnectionProvider.init("jdbc:sqlite:disk_stalker_test.db");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        QueryExecutor.delete("DELETE FROM Roots");
    }

    @AfterAll
    public static void cleanUp() throws SQLException {
        ConnectionProvider.close();
    }

    @Test
    public void createRootTest() {
        // When
        var root1 = Root.create("some", "/home", 234342);
        var root2 = Root.create("other", "/home/loc", 3454334 );
        var redundantRoot = Root.create("other", "/home/loc", 3454334 );

        // Then
        checkRoot(root1);
        checkRoot(root2);

        assertNotEquals(root1.get().getId(), root2.get().getId());
        assertFalse(redundantRoot.isPresent());
    }

    @Test
    public void findRootTest() {
        // When
        var root = Root.create("some", "/home", 234342);

        var foundRootById = Root.findById(root.get().getId());
        var nonExistingRoot = Root.findById(Integer.MAX_VALUE);

        // Then
        checkRoot(foundRootById);

        Assertions.assertEquals(root.get(), foundRootById.get());
        assertFalse(nonExistingRoot.isPresent());
    }

    @Test
    public void findRootByLocationTest() {
        // When
        var root = Root.create("some", "/home", 234342);
        var foundRootByLocation = Root.findByLocation(root.get().getName(), root.get().getPath());
        var nonExistingRoot = Root.findByLocation(null, null);

        // Then
        checkRoot(foundRootByLocation);

        Assertions.assertEquals(root.get(), foundRootByLocation.get());
        assertFalse(nonExistingRoot.isPresent());
    }
    @Test
    public void deleteByIdTest() {
        // When
        var root = Root.create("name", "/home", 224242);
        var rootToDelete = Root.create("name1", "/home/name", 6211);
        var id = rootToDelete.get().getId();
        //Then
        Root.deleteById(id);

        assertFalse(root.isEmpty());

    }

    //@Test
//    public void getPathNameTest(){
//        //When
//        var root = Root.create("name", "/home", 224242);
//        var pathname = root.get().getPathname();
//        assertEquals(pathname, root.get().getPath() + File.separator + );
//    }



    private void checkRoot(final Optional<Root> root) {
        assertTrue(root.isPresent());
        root.ifPresent(r -> {
            assertTrue(r.getId() > 0);
            assertNotNull(r.getName());
            assertNotNull(r.getPath());
            assertNotNull(r.getMaxSize());
        });
    }
}
