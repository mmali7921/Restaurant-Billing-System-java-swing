import java.time.format.DateTimeFormatter;

/**
 * Produces a formatted receipt string suitable for display and printing.
 * Follows Indian GST invoice format (CGST + SGST split).
 */
public class ReceiptFormatter {

    private static final int    W   = 44;
    private static final String SYM = AppConfig.getCurrencySymbol();
    private static final DateTimeFormatter DTF =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy  hh:mm a");

    public static String generateReceipt(Order order) {
        StringBuilder sb = new StringBuilder();

        // ── Header ─────────────────────────────────────────────
        sb.append(center(AppConfig.getRestaurantName())).append("\n");
        sb.append(center(AppConfig.getTagline())).append("\n");
        sb.append(center(AppConfig.getAddress())).append("\n");
        sb.append(center("GSTIN: " + AppConfig.getGstin())).append("\n");
        sb.append(divider('=')).append("\n");

        // ── Order meta ─────────────────────────────────────────
        sb.append(fmt("Order No", order.getOrderNumber())).append("\n");
        sb.append(fmt("Table",    "Table " + order.getTableNumber())).append("\n");
        sb.append(fmt("Cashier",  order.getCashierName())).append("\n");
        sb.append(fmt("Date/Time",order.getOrderTime().format(DTF))).append("\n");
        sb.append(divider('-')).append("\n");

        // ── Column header ──────────────────────────────────────
        sb.append(String.format("%-24s %3s  %10s\n", "Item", "Qty", "Amount"));
        sb.append(divider('-')).append("\n");

        // ── Line items ─────────────────────────────────────────
        for (OrderItem item : order.getItems()) {
            String name = item.getBurger().getName();
            if (name.length() > 24) name = name.substring(0, 21) + "...";
            double lineBase = item.getBurger().getPrice() * item.getQuantity();

            sb.append(String.format("%-24s %3d  %s%8.2f\n",
                    name, item.getQuantity(), SYM, lineBase));

            if (!item.getToppings().isEmpty()) {
                String extras = item.getToppingsText();
                if (extras.length() > 28) extras = extras.substring(0, 25) + "...";
                sb.append(String.format("  Extras: %-22s %s%6.2f\n",
                        extras, SYM, item.getToppingsCost()));
            }
        }

        // ── Totals ─────────────────────────────────────────────
        sb.append(divider('-')).append("\n");
        sb.append(rAlign("Subtotal :  " + SYM + String.format("%8.2f", order.getSubtotal()))).append("\n");
        sb.append(rAlign(String.format("CGST @%.1f%%:  %s%8.2f",
                AppConfig.getCGST() * 100, SYM, order.getCGST()))).append("\n");
        sb.append(rAlign(String.format("SGST @%.1f%%:  %s%8.2f",
                AppConfig.getSGST() * 100, SYM, order.getSGST()))).append("\n");
        sb.append(divider('=')).append("\n");
        sb.append(rAlign("TOTAL    :  " + SYM + String.format("%8.2f", order.getTotal()))).append("\n");
        sb.append(divider('=')).append("\n");

        // ── Footer ─────────────────────────────────────────────
        sb.append(center("Thank you for dining with us!")).append("\n");
        sb.append(center("Please visit again  \u2764")).append("\n");
        sb.append(center("*Tax Invoice*")).append("\n");

        return sb.toString();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private static String divider(char c) { return String.valueOf(c).repeat(W); }

    private static String center(String text) {
        if (text == null || text.length() >= W) return text;
        int pad = (W - text.length()) / 2;
        return " ".repeat(pad) + text;
    }

    private static String rAlign(String text) {
        if (text == null || text.length() >= W) return text;
        return " ".repeat(W - text.length()) + text;
    }

    private static String fmt(String label, String value) {
        return String.format("%-10s: %s", label, value);
    }
}
