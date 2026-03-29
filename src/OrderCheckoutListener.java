/**
 * Callback interface fired when the cashier completes an order.
 */
public interface OrderCheckoutListener {
    void onOrderCheckout(Order order);
}
