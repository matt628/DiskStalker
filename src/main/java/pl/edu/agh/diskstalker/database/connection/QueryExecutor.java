package pl.edu.agh.diskstalker.database.connection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class QueryExecutor {

    private final Logger logger = Logger.getGlobal();
    private final ConnectionProvider connectionProvider;
    private final QueryHelper queryHelper;

    @Inject public QueryExecutor(ConnectionProvider connectionProvider, QueryHelper queryHelper) {
        this.connectionProvider = connectionProvider;
        this.queryHelper = queryHelper;

        init();
    }

    public void init() {
        try {
            logger.info("Creating table Roots");
            create("CREATE TABLE IF NOT EXISTS Roots (" +
                    "RootID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name VARCHAR(100) NOT NULL," +
                    "Path VARCHAR(100) NOT NULL," +
                    "MaxSize VARCHAR(20)" +
                    ");");

            logger.info("Creating table Types");
            create("CREATE TABLE IF NOT EXISTS Types (" +
                    "TypeID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Extension VARCHAR(20) NOT NULL UNIQUE," +
                    "Description VARCHAR(200)" +
                    ");");

        } catch (SQLException e) {
            logger.info("Error during create tables: " + e.getMessage());
            throw new RuntimeException("Cannot create tables");
        }
    }

    public int createAndObtainId(final String insertSql, Object... args) throws SQLException {
        PreparedStatement statement = connectionProvider.getConnection().prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        queryHelper.mapParams(statement, args);
        statement.execute();
        try (final ResultSet resultSet = statement.getGeneratedKeys()) {
            return readIdFromResultSet(resultSet);
        }
    }

    private int readIdFromResultSet(final ResultSet resultSet) throws SQLException {
        return resultSet.next() ? resultSet.getInt(1) : -1;
    }

    public void create(final String insertSql, Object... args) throws SQLException {
        PreparedStatement ps = connectionProvider.getConnection().prepareStatement(insertSql);
        queryHelper.mapParams(ps, args);
        ps.execute();
    }

    public ResultSet read(final String sql, Object... args) throws SQLException {
        PreparedStatement ps = connectionProvider.getConnection().prepareStatement(sql);
        queryHelper.mapParams(ps, args);
        final ResultSet resultSet = ps.executeQuery();
        logger.info(String.format("Query: %s executed.", sql));
        return resultSet;
    }

    public void delete(final String sql, Object... args) throws SQLException {
        PreparedStatement ps = connectionProvider.getConnection().prepareStatement(sql);
        queryHelper.mapParams(ps, args);
        ps.executeUpdate();
    }

    public void executeUpdate(final List<String> sql, List<List<Object>> args) throws SQLException {
        connectionProvider.getConnection().setAutoCommit(false);
        for (int i = 0; i < sql.size(); i++) {
            PreparedStatement ps = connectionProvider.getConnection().prepareStatement(sql.get(i));
            queryHelper.mapParams(ps, args.get(i));
            ps.executeUpdate();
            logger.info(String.format("Query: %s executed.", sql.get(i)));
        }
        connectionProvider.getConnection().commit();
        connectionProvider.getConnection().setAutoCommit(true);
    }
}
