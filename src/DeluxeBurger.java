public class DeluxeBurger extends Burger {
    private String rollType;

    DeluxeBurger() {
        super("Deluxe Burger", 8.12);
        this.rollType = "Sausage";
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
