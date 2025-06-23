import java.sql.*;
import java.util.*;

public class CarRentalSystem {

    private Scanner scanner = new Scanner(System.in);

    public void ownerPanel(String ownerUsername) {
        while (true) {
            System.out.println("\n=== Owner Panel ===");
            System.out.println("1. Add Car");
            System.out.println("2. View All Cars");
            System.out.println("3. View Rentals");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            switch (ch) {
                case 1 -> addCar();
                case 2 -> viewCars();
                case 3 -> viewRentals();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public void userPanel(String username) {
        while (true) {
            System.out.println("\n=== User Panel ===");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent Car");
            System.out.println("3. Return Car");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            switch (ch) {
                case 1 -> viewAvailableCars();
                case 2 -> rentCar(username);
                case 3 -> returnCar(username);
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void addCar() {
        System.out.print("Car ID: ");
        String id = scanner.nextLine();
        System.out.print("Brand: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Price per day: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO car VALUES (?, ?, ?, ?, true)");
            ps.setString(1, id);
            ps.setString(2, brand);
            ps.setString(3, model);
            ps.setDouble(4, price);
            ps.executeUpdate();
            System.out.println("Car added successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to add car: " + e.getMessage());
        }
    }

    private void viewCars() {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM car");
            System.out.printf("%-10s %-10s %-10s %-10s %-12s\n", "ID", "Brand", "Model", "Price", "Available");
            while (rs.next()) {
                System.out.printf("%-10s %-10s %-10s %-10.2f %-12s\n",
                        rs.getString("car_id"), rs.getString("brand"), rs.getString("model"),
                        rs.getDouble("base_price_per_day"),
                        rs.getBoolean("is_available") ? "Yes" : "No");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewAvailableCars() {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM car WHERE is_available = true");
            System.out.printf("%-10s %-10s %-10s %-10s\n", "ID", "Brand", "Model", "Price");
            while (rs.next()) {
                System.out.printf("%-10s %-10s %-10s %-10.2f\n",
                        rs.getString("car_id"), rs.getString("brand"),
                        rs.getString("model"), rs.getDouble("base_price_per_day"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rentCar(String username) {
        System.out.print("Enter Car ID to rent: ");
        String carId = scanner.nextLine();
        System.out.print("Enter number of days: ");
        int days = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("UPDATE car SET is_available=false WHERE car_id=? AND is_available=true");
            ps1.setString(1, carId);
            int rows = ps1.executeUpdate();
            if (rows == 0) {
                System.out.println("Car not available or does not exist.");
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO rental (car_id, username, rental_days) VALUES (?, ?, ?)");
            ps2.setString(1, carId);
            ps2.setString(2, username);
            ps2.setInt(3, days);
            ps2.executeUpdate();
            System.out.println("Car rented successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnCar(String username) {
        System.out.print("Enter Car ID to return: ");
        String carId = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE car SET is_available=true WHERE car_id=?");
            ps.setString(1, carId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Car returned successfully.");
            } else {
                System.out.println("Return failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewRentals() {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM rental");
            System.out.printf("%-5s %-10s %-15s %-10s %-20s\n", "ID", "CarID", "Username", "Days", "Date");
            while (rs.next()) {
                System.out.printf("%-5d %-10s %-15s %-10d %-20s\n",
                        rs.getInt("rental_id"), rs.getString("car_id"),
                        rs.getString("username"), rs.getInt("rental_days"),
                        rs.getTimestamp("rental_date").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
