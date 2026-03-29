import java.util.ArrayList;

/**
 * Central menu catalogue — Indian burger menu with category grouping.
 * Replaces the original Fridge.java for new code; Fridge.java is kept for legacy compatibility.
 */
public class MenuManager {

    public static ArrayList<Burger> getMenu() {
        ArrayList<Burger> menu = new ArrayList<>();

        // ── Veg ───────────────────────────────────────────────
        menu.add(new Burger("Aloo Tikki Burger",  89.0,  "Veg"));
        menu.add(new Burger("Paneer Tikki Burger",139.0, "Veg"));
        menu.add(new Burger("McSpicy Paneer",     189.0, "Veg"));
        menu.add(new Burger("Veggie Crunch",      159.0, "Veg"));

        // ── Non-Veg ───────────────────────────────────────────
        menu.add(new Burger("Maharaja Mac",       249.0, "Non-Veg"));
        menu.add(new Burger("McSpicy Chicken",    229.0, "Non-Veg"));
        menu.add(new Burger("Crispy Chicken",     189.0, "Non-Veg"));
        menu.add(new Burger("Chicken Zinger",     219.0, "Non-Veg"));

        // ── Premium ───────────────────────────────────────────
        menu.add(new Burger("Double Cheese Stack",299.0, "Premium"));
        menu.add(new Burger("Signature Lamb",     349.0, "Premium"));
        menu.add(new Burger("Loaded Smash Burger",329.0, "Premium"));

        return menu;
    }

    public static ArrayList<Topping> getToppings() {
        ArrayList<Topping> t = new ArrayList<>();
        t.add(new Topping("Extra Cheese",  40.0));
        t.add(new Topping("Jalapenos",     25.0));
        t.add(new Topping("Tomato",        15.0));
        t.add(new Topping("Lettuce",       15.0));
        t.add(new Topping("Onion Rings",   30.0));
        t.add(new Topping("Extra Sauce",   20.0));
        t.add(new Topping("Olives",        30.0));
        t.add(new Topping("Mushrooms",     35.0));
        return t;
    }
}
