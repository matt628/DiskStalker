package pl.edu.agh.diskstalker;

import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        QueryExecutor.create("INSERT INTO Items (ItemID, Path, Type, Size) VALUES (1, '/home', 'root', '4233')");

        ConnectionProvider.close();
    }
}
