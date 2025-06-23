import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthSystem auth = new AuthSystem();
        CarRentalSystem system = new CarRentalSystem();

        while (true) {
            System.out.println("\n=== Car Rental System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String uname = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String pass = scanner.nextLine();
                    System.out.print("Enter role (owner/user): ");
                    String role = scanner.nextLine();
                    if (auth.register(uname, pass, role)) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed.");
                    }
                }
                case 2 -> {
                    System.out.print("Username: ");
                    String uname = scanner.nextLine();
                    System.out.print("Password: ");
                    String pass = scanner.nextLine();
                    System.out.print("Role (owner/user): ");
                    String role = scanner.nextLine();
                    String loggedIn = auth.login(uname, pass, role);
                    if (loggedIn == null) {
                        System.out.println("Login failed.");
                    } else {
                        System.out.println("Login successful as " + loggedIn);
                        if (loggedIn.equals("owner")) system.ownerPanel(uname);
                        else system.userPanel(uname);
                    }
                }
                case 3 -> {
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
