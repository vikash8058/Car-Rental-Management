import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String USER = "root";
    private static final String PASSWORD = "savi2026";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
