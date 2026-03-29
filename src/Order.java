import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete customer order with numbering, timestamps, and GST computation.
 */
public class Order {

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("yyMMdd");

    private static int sessionCounter = 1;

    private final String        orderNumber;
    private final int           tableNumber;
    private final String        cashierName;
    private final LocalDateTime orderTime;
    private final List<OrderItem> items;
    private       String        status; // COMPLETED | CANCELLED

    public Order(int tableNumber, String cashierName) {
        this.tableNumber  = tableNumber;
        this.cashierName  = cashierName;
        this.orderTime    = LocalDateTime.now();
        this.items        = new ArrayList<>();
        this.status       = "COMPLETED";
        this.orderNumber  = String.format("BN-%s-%03d",
                orderTime.format(DT_FMT), sessionCounter++);
    }

    // ── Cart mutation ──────────────────────────────────────────────────────────

    public void addItem(OrderItem item)    { items.add(item); }
    public void removeItem(int index)      { items.remove(index); }

    // ── Financial calculations ─────────────────────────────────────────────────

    public double getSubtotal() {
        double sum = 0;
        for (OrderItem i : items) sum += i.getItemTotal();
        return sum;
    }

    public double getCGST()  { return getSubtotal() * AppConfig.getCGST(); }
    public double getSGST()  { return getSubtotal() * AppConfig.getSGST(); }
    public double getTotal() { return getSubtotal() + getCGST() + getSGST(); }

    // ── Utility ────────────────────────────────────────────────────────────────

    public boolean isEmpty() { return items.isEmpty(); }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String          getOrderNumber() { return orderNumber; }
    public int             getTableNumber() { return tableNumber; }
    public String          getCashierName() { return cashierName; }
    public LocalDateTime   getOrderTime()   { return orderTime; }
    public List<OrderItem>  getItems()       { return items; }
    public String          getStatus()      { return status; }
    public void            setStatus(String s) { this.status = s; }
}
