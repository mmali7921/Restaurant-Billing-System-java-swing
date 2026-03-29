public class HealthyBurger extends Burger {
    private String rollType;

    HealthyBurger() {
        super("Healthy Burger", 139.00, "Veg");
        this.rollType = "Brown Rye";
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
