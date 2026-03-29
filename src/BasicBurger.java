public class BasicBurger extends Burger {
    private String rollType;

    BasicBurger() {
        super("Basic Burger", 89.00, "Veg");
        this.rollType = "White";
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
