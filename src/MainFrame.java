import javax.swing.*;
import java.awt.*;

/**
 * Main application frame containing tabs for Order, History, and Admin.
 */
public class MainFrame extends JFrame {
    private JTabbedPane       tabbedPane;
    private OrderPanel        orderPanel;
    private ReceiptPanel      receiptPanel;
    private OrderHistoryPanel historyPanel;
    private AdminPanel        adminPanel;
    private Toolbar           toolbar;

    public MainFrame() {
        super(AppConfig.getRestaurantName() + " POS System");

        // UI init
        DatabaseManager.initializeDatabase(); // Ensure schema exists

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);

        // Core components
        toolbar      = new Toolbar();
        receiptPanel = new ReceiptPanel();
        orderPanel   = new OrderPanel();
        historyPanel = new OrderHistoryPanel();
        adminPanel   = new AdminPanel();

        // ── Order flow wiring ──────────────────────────────────────────────────
        orderPanel.setCheckoutListener(order -> {
            // Update receipt
            receiptPanel.showReceipt(order);
            // Update models
            historyPanel.loadData();
            toolbar.refreshMetrics();
            JOptionPane.showMessageDialog(this, "Order " + order.getOrderNumber() + " completed!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        // ── Layout construction ────────────────────────────────────────────────
        // Create an order split pane (Menu/Cart on left, Receipt on right)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, orderPanel, receiptPanel);
        splitPane.setResizeWeight(0.65); // Give more space to menu/cart
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);
        splitPane.setDividerSize(6);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 245, 245));

        tabbedPane.addTab("  🍔 New Order  ", splitPane);
        tabbedPane.addTab("  📋 Order History  ", historyPanel);
        tabbedPane.addTab("  ⚙ Admin Settings  ", adminPanel);

        // Tab change listener to refresh history
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // History Tab
                historyPanel.loadData();
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
