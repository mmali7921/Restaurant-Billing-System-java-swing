import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Centralized database manager using burgernama.db (SQLite).
 * Handles schema creation, order persistence, and reporting queries.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:burgernama.db";

    // ── Connection ─────────────────────────────────────────────────────────────

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // ── Schema init ────────────────────────────────────────────────────────────

    public static void initializeDatabase() {
        exec("CREATE TABLE IF NOT EXISTS orders (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "order_number TEXT NOT NULL," +
             "table_number INTEGER," +
             "cashier_name TEXT," +
             "order_time   TEXT," +
             "status       TEXT," +
             "subtotal     REAL," +
             "cgst         REAL," +
             "sgst         REAL," +
             "total        REAL)");

        exec("CREATE TABLE IF NOT EXISTS order_items (" +
             "id           INTEGER PRIMARY KEY AUTOINCREMENT," +
             "order_id     INTEGER," +
             "item_name    TEXT," +
             "quantity     INTEGER," +
             "item_price   REAL," +
             "toppings     TEXT," +
             "toppings_cost REAL," +
             "line_total   REAL," +
             "FOREIGN KEY(order_id) REFERENCES orders(id))");
    }

    private static void exec(String sql) {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ── Persist order ──────────────────────────────────────────────────────────

    public static void saveOrder(Order order) {
        String sql = "INSERT INTO orders(order_number,table_number,cashier_name," +
                     "order_time,status,subtotal,cgst,sgst,total) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, order.getOrderNumber());
            ps.setInt(2,    order.getTableNumber());
            ps.setString(3, order.getCashierName());
            ps.setString(4, order.getOrderTime().toString());
            ps.setString(5, order.getStatus());
            ps.setDouble(6, order.getSubtotal());
            ps.setDouble(7, order.getCGST());
            ps.setDouble(8, order.getSGST());
            ps.setDouble(9, order.getTotal());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) saveOrderItems(c, keys.getLong(1), order);

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void saveOrderItems(Connection c, long orderId, Order order)
            throws SQLException {
        String sql = "INSERT INTO order_items(order_id,item_name,quantity,item_price," +
                     "toppings,toppings_cost,line_total) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (OrderItem it : order.getItems()) {
                ps.setLong(1,   orderId);
                ps.setString(2, it.getBurger().getName());
                ps.setInt(3,    it.getQuantity());
                ps.setDouble(4, it.getBurger().getPrice());
                ps.setString(5, it.getToppingsText());
                ps.setDouble(6, it.getToppingsCost());
                ps.setDouble(7, it.getItemTotal());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // ── Reporting queries ──────────────────────────────────────────────────────

    /** Returns rows for the Order History table. */
    public static List<Object[]> getAllOrders() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT order_number,table_number,cashier_name,order_time,status,total " +
                     "FROM orders ORDER BY id DESC";
        try (Connection c = connect();
             Statement s  = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getString("order_number"),
                    "Table " + rs.getInt("table_number"),
                    rs.getString("cashier_name"),
                    rs.getString("order_time").substring(0, 16).replace("T", "  "),
                    rs.getString("status"),
                    AppConfig.getCurrencySymbol() + String.format("%.2f", rs.getDouble("total"))
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rows;
    }

    /** Total revenue for today (completed orders). */
    public static double getTodayRevenue() {
        String today = LocalDateTime.now().toLocalDate().toString();
        String sql   = "SELECT COALESCE(SUM(total),0) FROM orders " +
                       "WHERE order_time LIKE ? AND status='COMPLETED'";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, today + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Count of completed orders for today. */
    public static int getTodayOrderCount() {
        String today = LocalDateTime.now().toLocalDate().toString();
        String sql   = "SELECT COUNT(*) FROM orders " +
                       "WHERE order_time LIKE ? AND status='COMPLETED'";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, today + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** All-time revenue. */
    public static double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total),0) FROM orders WHERE status='COMPLETED'";
        try (Connection c = connect();
             Statement s  = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}
