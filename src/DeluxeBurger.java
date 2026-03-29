public class DeluxeBurger extends Burger {
    private String rollType;

    DeluxeBurger() {
        super("Deluxe Burger", 249.00, "Non-Veg");
        this.rollType = "Sausage";
    }

    public String getRollType() {
        return rollType;
    }

    @Override
    public String toString() {
        return String.format("%s with %s :- \nCost:  %s%.2f \n",
                this.getName(),this.getRollType(),AppConfig.getCurrencySymbol(),this.getPrice());
    }
}
