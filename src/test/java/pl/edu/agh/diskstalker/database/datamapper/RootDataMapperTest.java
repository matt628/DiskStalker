package pl.edu.agh.diskstalker.database.datamapper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import pl.edu.agh.diskstalker.database.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.database.connection.QueryExecutor;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.guice.GuiceModule;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RootDataMapperTest {

    private static final String TEST_DB_ADDRESS = "jdbc:sqlite:disk_stalker_test.db";

    private final Injector injector = Guice.createInjector(new GuiceModule());

    private final ConnectionProvider connectionProvider = injector.getInstance(ConnectionProvider.class);
    private final QueryExecutor queryExecutor = injector.getInstance(QueryExecutor.class);
    private final RootDataMapper rootDataMapper = injector.getInstance(RootDataMapper.class);

    @BeforeAll
    public void init() throws Exception {
        connectionProvider.init(TEST_DB_ADDRESS);
        queryExecutor.init();
    }

    @BeforeEach
    public void setUp() throws SQLException {
        queryExecutor.delete("DELETE FROM Roots");
    }

    @AfterAll
    public void close() throws SQLException {
        connectionProvider.close();
    }

    @Test
    public void createRootTest() {
        // When
        var root1 = rootDataMapper.create("some", "/home", 234342, 340, 34647);
        var root2 = rootDataMapper.create("other", "/home/loc", 3454334, 340, 34647);
        var redundantRoot = rootDataMapper.create("other", "/home/loc", 3454334, 340, 34647);

        // Then
        checkRoot(root1);
        checkRoot(root2);

        assertNotEquals(root1.get().getId(), root2.get().getId());
        assertFalse(redundantRoot.isPresent());
    }

    @Test
    public void findRootTest() {
        // When
        var root = rootDataMapper.create("some", "/home", 234342, 340, 34647);
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
        var root = rootDataMapper.create("some", "/home", 234342, 340, 34647);
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
        var root = rootDataMapper.create("name", "/home", 224242, 340, 34647);
        var rootToDelete = rootDataMapper.create("name1", "/home/name", 6211, 340, 34647);
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
