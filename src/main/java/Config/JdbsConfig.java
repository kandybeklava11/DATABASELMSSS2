package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbsConfig {
    private static final String url = "jdbc:postgresql://localhost:5432/DATABASELMSSS2";
    private static final String userName = "postgres";
    private static final String password = "1234";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Successfully connected!");
            return  connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
