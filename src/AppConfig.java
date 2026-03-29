import java.io.*;
import java.util.Properties;

/**
 * Application-wide configuration.
 * Reads from config.properties if present; otherwise uses defaults and creates the file.
 */
public class AppConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties props    = new Properties();

    static { loadConfig(); }

    // ── Load / Save ────────────────────────────────────────────────────────────

    private static void loadConfig() {
        // Defaults
        props.setProperty("restaurant.name",    "BurgerWala");
        props.setProperty("restaurant.tagline",  "Asli Swad, Har Baar!");
        props.setProperty("restaurant.gstin",    "27AABCS1429B1Z1");
        props.setProperty("restaurant.address",  "MG Road, Bengaluru - 560001");
        props.setProperty("currency.symbol",     "\u20b9");   // ₹
        props.setProperty("tax.cgst",            "0.09");     // 9 % (AC restaurant)
        props.setProperty("tax.sgst",            "0.09");     // 9 % (AC restaurant)
        props.setProperty("admin.pin",           "1234");

        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            props.load(in);
        } catch (IOException ignored) {
            saveConfig(); // write defaults so user can edit
        }
    }

    public static void saveConfig() {
        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "BurgerNama POS Configuration");
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public static String getRestaurantName() { return props.getProperty("restaurant.name"); }
    public static String getTagline()         { return props.getProperty("restaurant.tagline"); }
    public static String getGstin()           { return props.getProperty("restaurant.gstin"); }
    public static String getAddress()         { return props.getProperty("restaurant.address"); }
    public static String getCurrencySymbol()  { return props.getProperty("currency.symbol"); }
    public static double getCGST()            { return Double.parseDouble(props.getProperty("tax.cgst")); }
    public static double getSGST()            { return Double.parseDouble(props.getProperty("tax.sgst")); }
    public static String getAdminPin()        { return props.getProperty("admin.pin"); }

    // ── Setters (used by Admin panel) ──────────────────────────────────────────

    public static void setRestaurantName(String v) { props.setProperty("restaurant.name", v); }
    public static void setTagline(String v)         { props.setProperty("restaurant.tagline", v); }
    public static void setAdminPin(String v)        { props.setProperty("admin.pin", v); }
}
