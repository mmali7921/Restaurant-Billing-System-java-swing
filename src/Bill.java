import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bill {
    private Burger burger;

    public Bill(Burger burger) {
        this.burger = burger;
    }

    public String generateReceipt() {
        double total = calculateTotal(this.burger);
        double tax = total * 0.15;
        double netTotal = total + tax;
        saveBillToDatabase(netTotal);  // Save the netTotal to the database
        return String.format("\nAmount: $%.2f  + (Tax: $%.2f)\nTotal Bill: $%.2f\n", total, tax, netTotal);
    }

    private double calculateTotal(Burger burger) {
        double tempTotal = burger.getPrice();
        for (Topping topping : burger.getToppings()) {
            tempTotal += topping.getPrice();
        }
        return tempTotal;
    }

    private void saveBillToDatabase(double netTotal) {
        String sql = "INSERT INTO bills(burger_name, toppings, price, net_total) VALUES(?, ?, ?, ?)";

        StringBuilder toppings = new StringBuilder();
        for (Topping topping : burger.getToppings()) {
            toppings.append(topping.getName()).append(", ");
        }
        if (toppings.length() > 0) {
            toppings.setLength(toppings.length() - 2); // Remove last comma and space
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, burger.getName());
            pstmt.setString(2, toppings.toString());
            pstmt.setDouble(3, burger.getPrice());
            pstmt.setDouble(4, netTotal); // Store the netTotal
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:billing.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
