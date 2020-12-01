package pl.edu.agh.diskstalker;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.executor.QueryExecutor;
import pl.edu.agh.diskstalker.model.Item;

import java.sql.SQLException;

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
//        var item1 = Item.create()
    }
}
