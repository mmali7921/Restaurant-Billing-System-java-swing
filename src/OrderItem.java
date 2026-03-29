import java.util.ArrayList;
import java.util.List;

/**
 * Represents one line-item in an order (one burger + its selected toppings + quantity).
 */
public class OrderItem {

    private final Burger       burger;
    private final List<Topping> toppings;
    private       int          quantity;

    public OrderItem(Burger burger, int quantity) {
        this.burger   = burger;
        this.quantity = quantity;
        this.toppings = new ArrayList<>();
    }

    public void addTopping(Topping t) { toppings.add(t); }

    // ── Derived totals ─────────────────────────────────────────────────────────

    public double getToppingsCost() {
        double sum = 0;
        for (Topping t : toppings) sum += t.getPrice();
        return sum * quantity;
    }

    public double getItemTotal() {
        return (burger.getPrice() + getToppingsCostPerUnit()) * quantity;
    }

    private double getToppingsCostPerUnit() {
        double sum = 0;
        for (Topping t : toppings) sum += t.getPrice();
        return sum;
    }

    public String getToppingsText() {
        if (toppings.isEmpty()) return "—";
        StringBuilder sb = new StringBuilder();
        for (Topping t : toppings) sb.append(t.getName()).append(", ");
        return sb.substring(0, sb.length() - 2);
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public Burger       getBurger()   { return burger; }
    public List<Topping> getToppings() { return toppings; }
    public int          getQuantity() { return quantity; }
    public void         setQuantity(int q) { this.quantity = q; }
}
