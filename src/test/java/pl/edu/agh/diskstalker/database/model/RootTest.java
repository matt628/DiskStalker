package pl.edu.agh.diskstalker.database.model;

import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.database.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapperImpl;
import pl.edu.agh.diskstalker.database.executor.QueryExecutor;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RootTest {

    private final RootDataMapper rootDataMapper = new RootDataMapperImpl();

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
        var root1 = rootDataMapper.create("some", "/home", 234342);
        var root2 = rootDataMapper.create("other", "/home/loc", 3454334 );
        var redundantRoot = rootDataMapper.create("other", "/home/loc", 3454334 );

        // Then
        checkRoot(root1);
        checkRoot(root2);

        assertNotEquals(root1.get().getId(), root2.get().getId());
        assertFalse(redundantRoot.isPresent());
    }

    @Test
    public void findRootTest() {
        // When
        var root = rootDataMapper.create("some", "/home", 234342);
        var foundRootById = rootDataMapper.findById(root.get().getId());
        var nonExistingRoot = rootDataMapper.findById(Integer.MAX_VALUE);

        // Then
        checkRoot(foundRootById);

        assertEquals(root.get(), foundRootById.get());
        assertFalse(nonExistingRoot.isPresent());
    }

    @Test
    public void findRootByLocationTest() {
        // When
        var root = rootDataMapper.create("some", "/home", 234342);
        var foundRootByLocation = rootDataMapper.findByLocation(root.get().getName(), root.get().getPath());
        var nonExistingRoot = rootDataMapper.findByLocation(null, null);

        // Then
        checkRoot(foundRootByLocation);

        assertEquals(root.get(), foundRootByLocation.get());
        assertFalse(nonExistingRoot.isPresent());
    }

    @Test
    public void deleteByIdTest() {
        // Given
        var root = rootDataMapper.create("name", "/home", 224242);
        var rootToDelete = rootDataMapper.create("name1", "/home/name", 6211);
        var id = rootToDelete.get().getId();

        // When
        rootDataMapper.deleteById(id);

        // Then
        assertFalse(root.isEmpty());
    }

    private void checkRoot(final Optional<Root> root) {
        assertTrue(root.isPresent());
        root.ifPresent(r -> {
            assertTrue(r.getId() > 0);
            assertNotNull(r.getName());
            assertNotNull(r.getPath());
        });
    }
}
