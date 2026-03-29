import java.util.ArrayList;

/**
 * Base burger model — extended with category for menu grouping.
 * Backward-compatible: existing subclasses (BasicBurger etc.) still compile.
 */
public class Burger {

    private final String name;
    private final Double price;
    private final String category;
    private ArrayList<Topping> toppings = new ArrayList<>();

    // ── Constructors ────────────────────────────────────────────────────────────

    /** Legacy constructor used by subclasses (BasicBurger, HealthyBurger, DeluxeBurger). */
    Burger(String name, Double price) {
        this(name, price, "General");
    }

    /** Full constructor used by MenuManager. */
    Burger(String name, Double price, String category) {
        this.name     = name;
        this.price    = price;
        this.category = category;
    }

    // ── Toppings ────────────────────────────────────────────────────────────────

    public void setToppings(Topping topping) { toppings.add(topping); }
    public void clearToppings()              { toppings.clear(); }
    public ArrayList<Topping> getToppings()  { return toppings; }

    // ── Getters ─────────────────────────────────────────────────────────────────

    public String getName()     { return name; }
    public Double getPrice()    { return price; }
    public String getCategory() { return category; }

    @Override
    public String toString() { return name; }
}
