public class HealthyBurger extends Burger {
    private String rollType;

    HealthyBurger() {
        super("Healthy Burger", 5.67);
        this.rollType = "Brown Rye";
    }

    public String getRollType() {
        return rollType;
    }

    @Override
    public String toString() {
        return String.format("%s with %s :- \nCost:  $%.2f \n",
                this.getName(),this.getRollType(),this.getPrice());
    }
}
