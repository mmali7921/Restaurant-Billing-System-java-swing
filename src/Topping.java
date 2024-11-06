public class Topping {
    private String name;
    private Double price;

    Topping(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s : $%.2f\n", this.name, this.price);
    }
}
