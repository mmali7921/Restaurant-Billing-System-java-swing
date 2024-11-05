public class BasicBurger extends Burger {
    private String rollType;

    BasicBurger() {
        super("Basic Burger", 4.50);
        this.rollType = "White";
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
