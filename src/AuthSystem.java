import java.sql.*;
import java.util.Scanner;

public class AuthSystem {

    public boolean register(String username, String password, String role) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO account (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role.toLowerCase());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public String login(String username, String password, String role) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM account WHERE username=? AND password=? AND role=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role.toLowerCase());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? role.toLowerCase() : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
