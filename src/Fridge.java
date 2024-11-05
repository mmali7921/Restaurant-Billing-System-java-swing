import java.util.ArrayList;

public class Fridge {
    public static ArrayList<Burger> prepareBurgers() {
        ArrayList<Burger> allBurgers = new ArrayList<>();
        allBurgers.add(new BasicBurger());
        allBurgers.add(new HealthyBurger());
        allBurgers.add(new DeluxeBurger());
        return allBurgers;
    }

    public static ArrayList<Topping> prepareToppings() {
        ArrayList<Topping> allToppings = new ArrayList<>();
        allToppings.add(new Topping("Tomato", 1.27));
        allToppings.add(new Topping("Lettuce", 2.37));
        allToppings.add(new Topping("Cheese", 2.77));
        allToppings.add(new Topping("Carrot", 2.27));
        allToppings.add(new Topping("Pepper", 0.57));
        allToppings.add(new Topping("Olives", 1.77));
        return allToppings;
    }
}
