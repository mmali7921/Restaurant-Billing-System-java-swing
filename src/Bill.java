public class Bill {
    private Burger burger;

    public Bill(Burger burger) {
        this.burger = burger;
    }

    public String generateReceipt() {
        double total = calculateTotal(this.burger);
        double tax = total * 0.15;
        double netTotal = total + tax;
        // No need to save the bill here anymore
        return String.format("\nAmount: $%.2f  + (Tax: $%.2f)\nTotal Bill: $%.2f\n", total, tax, netTotal);
    }

    private double calculateTotal(Burger burger) {
        double tempTotal = burger.getPrice();
        for (Topping topping : burger.getToppings()) {
            tempTotal += topping.getPrice();
        }
        return tempTotal;
    }
}
