package pl.edu.agh.diskstalker.database.connection;

import com.google.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
public final class ConnectionProvider {

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";

    private static final String JDBC_ADDRESS = "jdbc:sqlite:disk_stalker.db";

    private final Logger logger = Logger.getGlobal();

    private Optional<Connection> connection = Optional.empty();

    public ConnectionProvider() throws Exception {
        init(JDBC_ADDRESS);
    }

    public void init(final String jdbcAddress) throws Exception {
        close();
        logger.info("Loading driver");
        Class.forName(JDBC_DRIVER);
        connection = Optional.of(DriverManager.getConnection(jdbcAddress));
        logger.info("Connection created");
    }

    public Connection getConnection() {
        return connection.orElseThrow(() -> new RuntimeException("Connection is not valid."));
    }

    public void close() throws SQLException {
        if (connection.isPresent()) {
            logger.info("Closing connection");
            connection.get().close();
            connection = Optional.empty();
        }
    }
}
