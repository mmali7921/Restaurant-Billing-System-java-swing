import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Top toolbar displaying the restaurant name, current date/time, and live metrics.
 */
public class Toolbar extends JPanel {

    private static final Color HEADER_BG = new Color(245, 124, 0); // Saffron

    private JLabel textLabel;
    private JLabel clockLabel;
    private JLabel metricsLabel;

    public Toolbar() {
        setLayout(new BorderLayout());
        setBackground(HEADER_BG);
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Let's create a nice styled title
        textLabel = new JLabel(AppConfig.getRestaurantName() + " POS");
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        textLabel.setForeground(Color.WHITE);

        metricsLabel = new JLabel();
        metricsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        metricsLabel.setForeground(new Color(255, 230, 200));
        refreshMetrics();

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        clockLabel.setForeground(Color.WHITE);
        startClock();

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.setOpaque(false);
        rightPanel.add(clockLabel);
        rightPanel.add(metricsLabel);

        add(textLabel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void refreshMetrics() {
        double todayRev = DatabaseManager.getTodayRevenue();
        String sym = AppConfig.getCurrencySymbol();
        metricsLabel.setText(String.format("Today: %s%.2f", sym, todayRev));
    }

    private void startClock() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        Timer timer = new Timer(1000, e -> {
            clockLabel.setText(LocalDateTime.now().format(dtf));
        });
        timer.start();
    }
}
