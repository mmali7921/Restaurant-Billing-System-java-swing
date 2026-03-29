import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Order History panel — shows all past orders from the database in a JTable.
 * Tab 2 of the application.
 */
public class OrderHistoryPanel extends JPanel {

    private static final Color PANEL_BG  = new Color(250, 245, 235);
    private static final Color SAFFRON   = new Color(230, 81, 0);
    private static final Color SAFFRON_LT= new Color(255, 167, 38);
    private static final Color HEADER_BG = new Color(245, 124, 0);
    private static final Font  FONT_BODY = new Font("SansSerif", Font.PLAIN,  12);
    private static final Font  FONT_BOLD = new Font("SansSerif", Font.BOLD,   12);

    private final DefaultTableModel tableModel;
    private final JTable            historyTable;
    private final JLabel            summaryLabel;

    public OrderHistoryPanel() {
        setBackground(PANEL_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Title ──────────────────────────────────────────────────────────────
        JLabel title = new JLabel("📋  Order History", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(SAFFRON);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        add(title, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────────────────────
        String[] cols = {"Order #", "Table", "Cashier", "Date & Time", "Status", "Total"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new JTable(tableModel);
        historyTable.setFont(FONT_BODY);
        historyTable.setRowHeight(26);
        historyTable.setGridColor(new Color(230, 210, 180));
        historyTable.setSelectionBackground(new Color(255, 224, 178));
        historyTable.setShowGrid(true);

        JTableHeader header = historyTable.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);

        // Column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(90);

        // Alternate row color renderer
        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(255, 251, 245));
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SAFFRON_LT),
                "All Orders (newest first)",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_BOLD, SAFFRON));
        add(scroll, BorderLayout.CENTER);

        // ── Bottom bar ─────────────────────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        bottom.setOpaque(false);

        summaryLabel = new JLabel("Loading...");
        summaryLabel.setFont(FONT_BOLD);

        JButton refreshBtn = makeBtn("↻  Refresh", SAFFRON, Color.WHITE);
        refreshBtn.addActionListener(e -> loadData());

        bottom.add(summaryLabel);
        bottom.add(refreshBtn);
        add(bottom, BorderLayout.SOUTH);

        loadData();
    }

    // ── Public API ─────────────────────────────────────────────────────────────

    public void loadData() {
        tableModel.setRowCount(0);
        List<Object[]> rows = DatabaseManager.getAllOrders();
        for (Object[] row : rows) tableModel.addRow(row);

        int count   = DatabaseManager.getTodayOrderCount();
        double rev  = DatabaseManager.getTodayRevenue();
        String sym  = AppConfig.getCurrencySymbol();
        summaryLabel.setText(String.format(
                "Today: %d orders  |  Revenue: %s%.2f  |  Total rows: %d",
                count, sym, rev, rows.size()));
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private static JButton makeBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        return b;
    }
}
