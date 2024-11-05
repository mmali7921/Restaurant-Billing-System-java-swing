import java.util.ArrayList;

public class Burger {
    private Double price;
    private String name;

    private ArrayList<Topping> toppings = new ArrayList<>();

    Burger(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }



    public void setToppings(Topping topping) {
        this.toppings.add(topping);
    }

    public ArrayList<Topping> getToppings() {
        return this.toppings;
    }

    @Override
    public String toString() {
        return name; // This will show only the name of the burger in the selection box
    }
}
