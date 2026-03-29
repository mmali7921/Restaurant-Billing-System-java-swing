import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main ordering panel — multi-item cart with live GST totals.
 * Tab 1 of the application.
 */
public class OrderPanel extends JPanel {

    // ── Theme ─────────────────────────────────────────────────────────────────
    static final Color SAFFRON      = new Color(230, 81, 0);
    static final Color SAFFRON_LT   = new Color(255, 167, 38);
    static final Color CREAM        = new Color(255, 248, 235);
    static final Color PANEL_BG     = new Color(250, 245, 235);
    static final Color GREEN_BTN    = new Color(46, 125, 50);
    static final Color RED_BTN      = new Color(183, 28, 28);
    static final Color HEADER_BG    = new Color(245, 124, 0);
    static final Font  FONT_BODY    = new Font("SansSerif", Font.PLAIN,  12);
    static final Font  FONT_BOLD    = new Font("SansSerif", Font.BOLD,   12);
    static final Font  FONT_TITLE   = new Font("SansSerif", Font.BOLD,   14);
    static final Font  FONT_TOTAL   = new Font("SansSerif", Font.BOLD,   16);

    // ── State ─────────────────────────────────────────────────────────────────
    private final List<OrderItem> cartItems = new ArrayList<>();
    private Burger selectedBurger = null;
    private OrderCheckoutListener checkoutListener;

    // ── Left panel components ─────────────────────────────────────────────────
    private JList<Burger>         menuList;
    private DefaultListModel<Burger> menuModel;
    private JComboBox<String>     categoryFilter;
    private final List<JCheckBox> toppingBoxes = new ArrayList<>();
    private JSpinner              qtySpinner;

    // ── Right panel components ────────────────────────────────────────────────
    private JTextField            tableField;
    private JTextField            cashierField;
    private DefaultTableModel     cartModel;
    private JTable                cartTable;
    private JLabel                subtotalLabel;
    private JLabel                cgstLabel;
    private JLabel                sgstLabel;
    private JLabel                totalLabel;
    private JButton               checkoutBtn;

    // ─────────────────────────────────────────────────────────────────────────
    public OrderPanel() {
        setBackground(PANEL_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildMenuPanel(), BorderLayout.WEST);
        add(buildCartPanel(), BorderLayout.CENTER);
    }

    public void setCheckoutListener(OrderCheckoutListener l) { this.checkoutListener = l; }

    // ═════════════════════════════════════════════════════════════════════════
    // LEFT — Menu Panel
    // ═════════════════════════════════════════════════════════════════════════

    private JPanel buildMenuPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(PANEL_BG);
        p.setPreferredSize(new Dimension(380, 0));
        p.setBorder(sectionBorder("Menu"));

        // Category filter
        JPanel filterRow = row();
        filterRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterRow.add(label("Category:", FONT_BOLD));
        categoryFilter = new JComboBox<>(new String[]{"All", "Veg", "Non-Veg", "Premium"});
        categoryFilter.setFont(FONT_BODY);
        categoryFilter.addActionListener(e -> refreshMenu());
        filterRow.add(categoryFilter);
        p.add(filterRow);
        p.add(vgap(4));

        // Burger JList
        menuModel = new DefaultListModel<>();
        menuList  = new JList<>(menuModel);
        menuList.setCellRenderer(new BurgerCellRenderer());
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFixedCellHeight(54);
        menuList.setBackground(Color.WHITE);
        menuList.setBorder(BorderFactory.createLineBorder(SAFFRON_LT));
        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                selectedBurger = menuList.getSelectedValue();
        });
        refreshMenu();

        JScrollPane menuScroll = new JScrollPane(menuList);
        menuScroll.setPreferredSize(new Dimension(360, 210));
        menuScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));
        menuScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(menuScroll);
        p.add(vgap(8));

        // Toppings
        JPanel toppingsPanel = buildToppingsPanel();
        toppingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(toppingsPanel);
        p.add(vgap(8));

        // Qty + Add to Cart
        JPanel addRow = row();
        addRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        addRow.add(label("Qty:", FONT_BOLD));
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        qtySpinner.setPreferredSize(new Dimension(55, 28));
        addRow.add(qtySpinner);
        JButton addBtn = buildBtn("+ Add to Cart", SAFFRON, Color.WHITE);
        addBtn.addActionListener(e -> addToCart());
        addRow.add(Box.createHorizontalStrut(8));
        addRow.add(addBtn);
        p.add(addRow);

        return p;
    }

    private JPanel buildToppingsPanel() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(PANEL_BG);
        wrap.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SAFFRON_LT), "Extras / Toppings",
                TitledBorder.LEFT, TitledBorder.TOP, FONT_BOLD, SAFFRON));

        JPanel grid = new JPanel(new GridLayout(0, 1, 4, 4));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        toppingBoxes.clear();
        String sym = AppConfig.getCurrencySymbol();
        for (Topping t : MenuManager.getToppings()) {
            JCheckBox cb = new JCheckBox(
                    String.format("%s +%s%.0f", t.getName(), sym, t.getPrice()));
            cb.setBackground(Color.WHITE);
            cb.setFont(FONT_BODY);
            toppingBoxes.add(cb);
            grid.add(cb);
        }
        JScrollPane sc = new JScrollPane(grid);
        sc.setPreferredSize(new Dimension(360, 150));
        sc.setBorder(null);
        wrap.add(sc, BorderLayout.CENTER);
        return wrap;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // RIGHT — Cart Panel
    // ═════════════════════════════════════════════════════════════════════════

    private JPanel buildCartPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(PANEL_BG);
        p.setBorder(sectionBorder("Current Order"));

        // Order info (table + cashier)
        JPanel infoRow = row();
        infoRow.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        infoRow.add(label("Table No:", FONT_BOLD));
        tableField = new JTextField("1", 4);
        tableField.setFont(FONT_BODY);
        infoRow.add(tableField);
        infoRow.add(Box.createHorizontalStrut(16));
        infoRow.add(label("Cashier:", FONT_BOLD));
        cashierField = new JTextField("Admin", 10);
        cashierField.setFont(FONT_BODY);
        infoRow.add(cashierField);
        p.add(infoRow, BorderLayout.NORTH);

        // Cart JTable
        String[] cols = {"Item", "Toppings", "Qty", AppConfig.getCurrencySymbol() + " Amount"};
        cartModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartModel);
        cartTable.setFont(FONT_BODY);
        cartTable.setRowHeight(28);
        cartTable.getTableHeader().setFont(FONT_BOLD);
        cartTable.getTableHeader().setBackground(HEADER_BG);
        cartTable.getTableHeader().setForeground(Color.WHITE);
        cartTable.setSelectionBackground(new Color(255, 224, 178));
        cartTable.setGridColor(new Color(230, 210, 180));
        cartTable.setShowGrid(true);
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(160);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(40);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(80);

        JScrollPane sc = new JScrollPane(cartTable);
        sc.setBorder(BorderFactory.createLineBorder(SAFFRON_LT));
        p.add(sc, BorderLayout.CENTER);

        // Bottom: totals + buttons
        p.add(buildBottomPanel(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildBottomPanel() {
        JPanel outer = new JPanel(new BorderLayout(8, 8));
        outer.setBackground(PANEL_BG);

        // Remove button
        JButton removeBtn = buildBtn("Remove Selected", RED_BTN, Color.WHITE);
        removeBtn.addActionListener(e -> removeSelected());
        JPanel removeRow = row();
        removeRow.add(removeBtn);
        outer.add(removeRow, BorderLayout.NORTH);

        // Totals box
        String sym = AppConfig.getCurrencySymbol();
        JPanel totals = new JPanel(new GridLayout(4, 2, 4, 4));
        totals.setBackground(new Color(255, 243, 224));
        totals.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SAFFRON_LT),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        subtotalLabel = label(sym + "0.00", FONT_BOLD);
        cgstLabel     = label(sym + "0.00", FONT_BODY);
        sgstLabel     = label(sym + "0.00", FONT_BODY);
        totalLabel    = label(sym + "0.00", FONT_TOTAL);
        totalLabel.setForeground(new Color(183, 28, 28));

        totals.add(label("Subtotal:",                                FONT_BOLD)); totals.add(subtotalLabel);
        totals.add(label(String.format("CGST @%.0f%%:", AppConfig.getCGST()*100), FONT_BODY)); totals.add(cgstLabel);
        totals.add(label(String.format("SGST @%.0f%%:", AppConfig.getSGST()*100), FONT_BODY)); totals.add(sgstLabel);
        totals.add(label("TOTAL:",                                   FONT_TOTAL)); totals.add(totalLabel);

        outer.add(totals, BorderLayout.CENTER);

        // Action buttons
        JPanel btnRow = row();
        JButton clearBtn = buildBtn("Clear Order", new Color(96,96,96), Color.WHITE);
        clearBtn.addActionListener(e -> clearCart());
        checkoutBtn = buildBtn("Checkout", GREEN_BTN, Color.WHITE);
        checkoutBtn.setFont(FONT_TITLE);
        checkoutBtn.addActionListener(e -> checkout());
        btnRow.add(clearBtn);
        btnRow.add(Box.createHorizontalStrut(12));
        btnRow.add(checkoutBtn);
        outer.add(btnRow, BorderLayout.SOUTH);

        return outer;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Cart Logic
    // ═════════════════════════════════════════════════════════════════════════

    private void addToCart() {
        if (selectedBurger == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a burger from the menu first.",
                    "No Item Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int qty = (int) qtySpinner.getValue();
        OrderItem item = new OrderItem(new Burger(
                selectedBurger.getName(), selectedBurger.getPrice(), selectedBurger.getCategory()), qty);

        ArrayList<Topping> allToppings = MenuManager.getToppings();
        for (int i = 0; i < toppingBoxes.size() && i < allToppings.size(); i++) {
            if (toppingBoxes.get(i).isSelected()) item.addTopping(allToppings.get(i));
        }

        cartItems.add(item);
        refreshCartTable();
        clearInputSelections();
    }

    private void removeSelected() {
        int row = cartTable.getSelectedRow();
        if (row >= 0) {
            cartItems.remove(row);
            refreshCartTable();
        }
    }

    private void clearCart() {
        cartItems.clear();
        refreshCartTable();
        clearInputSelections();
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Cart is empty. Please add items before checkout.",
                    "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tableStr   = tableField.getText().trim();
        String cashierStr = cashierField.getText().trim();
        if (tableStr.isEmpty() || cashierStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Table Number and Cashier name.",
                    "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int tableNo;
        try { tableNo = Integer.parseInt(tableStr); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Table number must be numeric.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = new Order(tableNo, cashierStr);
        for (OrderItem it : cartItems) order.addItem(it);

        // Save to DB
        DatabaseManager.saveOrder(order);

        if (checkoutListener != null) checkoutListener.onOrderCheckout(order);

        clearCart();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Helpers
    // ═════════════════════════════════════════════════════════════════════════

    private void refreshMenu() {
        String cat = (String) categoryFilter.getSelectedItem();
        menuModel.clear();
        for (Burger b : MenuManager.getMenu()) {
            if ("All".equals(cat) || b.getCategory().equals(cat)) menuModel.addElement(b);
        }
        selectedBurger = null;
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        String sym = AppConfig.getCurrencySymbol();
        for (OrderItem it : cartItems) {
            cartModel.addRow(new Object[]{
                it.getBurger().getName(),
                it.getToppingsText(),
                "x" + it.getQuantity(),
                sym + String.format("%.2f", it.getItemTotal())
            });
        }
        updateTotals();
    }

    private void updateTotals() {
        double sub = 0;
        for (OrderItem it : cartItems) sub += it.getItemTotal();
        double cgst  = sub * AppConfig.getCGST();
        double sgst  = sub * AppConfig.getSGST();
        double total = sub + cgst + sgst;
        String sym = AppConfig.getCurrencySymbol();
        subtotalLabel.setText(sym + String.format("%.2f", sub));
        cgstLabel.setText(sym + String.format("%.2f", cgst));
        sgstLabel.setText(sym + String.format("%.2f", sgst));
        totalLabel.setText(sym + String.format("%.2f", total));
    }

    private void clearInputSelections() {
        menuList.clearSelection();
        selectedBurger = null;
        for (JCheckBox cb : toppingBoxes) cb.setSelected(false);
        qtySpinner.setValue(1);
    }

    // ── UI factory helpers ────────────────────────────────────────────────────

    private static JPanel row() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        p.setOpaque(false);
        return p;
    }

    private static JLabel label(String text, Font font) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        return l;
    }

    private static JButton buildBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        return b;
    }

    private static Border sectionBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(SAFFRON, 2), title,
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 13), SAFFRON));
    }

    private static Component vgap(int h) { return Box.createVerticalStrut(h); }

    // ═════════════════════════════════════════════════════════════════════════
    // Custom burger list renderer
    // ═════════════════════════════════════════════════════════════════════════

    private static class BurgerCellRenderer extends DefaultListCellRenderer {
        private static final Color VEG_COLOR    = new Color(46, 125, 50);
        private static final Color NONVEG_COLOR = new Color(183, 28, 28);
        private static final Color PREM_COLOR   = new Color(74, 20, 140);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            JPanel panel = new JPanel(new BorderLayout(8, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

            if (value instanceof Burger) {
                Burger b = (Burger) value;
                String sym = AppConfig.getCurrencySymbol();

                JLabel nameLabel = new JLabel(b.getName());
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

                JLabel priceLabel = new JLabel(sym + String.format("%.2f", b.getPrice()));
                priceLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

                Color catColor = "Veg".equals(b.getCategory()) ? VEG_COLOR :
                        "Non-Veg".equals(b.getCategory()) ? NONVEG_COLOR : PREM_COLOR;
                JLabel catLabel = new JLabel("● " + b.getCategory());
                catLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
                catLabel.setForeground(catColor);

                JPanel left = new JPanel();
                left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
                left.setOpaque(false);
                left.add(nameLabel);
                left.add(catLabel);

                panel.add(left, BorderLayout.CENTER);
                panel.add(priceLabel, BorderLayout.EAST);
            }

            if (isSelected) {
                panel.setBackground(new Color(255, 224, 178));
            } else {
                panel.setBackground(index % 2 == 0 ? Color.WHITE : new Color(255, 251, 245));
            }
            return panel;
        }
    }
}
