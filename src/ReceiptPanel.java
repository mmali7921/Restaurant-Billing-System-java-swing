import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Displays the formatted receipt after checkout and offers Print / Save actions.
 * Tab portion of MainFrame — updated by OrderPanel via callback.
 */
public class ReceiptPanel extends JPanel {

    private static final Color PANEL_BG  = new Color(250, 245, 235);
    private static final Color SAFFRON   = new Color(230, 81, 0);
    private static final Color SAFFRON_LT= new Color(255, 167, 38);
    private static final Color GREEN_BTN = new Color(46, 125, 50);
    private static final Color BLUE_BTN  = new Color(21, 101, 192);

    private final JTextArea receiptArea;
    private       String    lastReceiptText = "";
    private       String    lastOrderNumber = "";

    public ReceiptPanel() {
        setBackground(PANEL_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Header label ───────────────────────────────────────────────────────
        JLabel title = new JLabel("🧾  Receipt", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(SAFFRON);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        add(title, BorderLayout.NORTH);

        // ── Receipt text area ──────────────────────────────────────────────────
        receiptArea = new JTextArea();
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        receiptArea.setBackground(Color.WHITE);
        receiptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        receiptArea.setText("\n\n\n      Receipts will appear here after checkout.");

        JScrollPane scroll = new JScrollPane(receiptArea);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SAFFRON_LT, 1),
                "Tax Invoice",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), SAFFRON));
        add(scroll, BorderLayout.CENTER);

        // ── Bottom buttons ─────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        btnPanel.setOpaque(false);

        JButton printBtn = makeBtn("🖨  Print", SAFFRON,    Color.WHITE);
        JButton saveBtn  = makeBtn("💾  Save as TXT", BLUE_BTN, Color.WHITE);
        JButton clearBtn = makeBtn("✕  Clear", new Color(96, 96, 96), Color.WHITE);

        printBtn.addActionListener(e -> {
            if (!lastReceiptText.isEmpty()) ReceiptPrinter.printReceipt(lastReceiptText);
        });
        saveBtn.addActionListener(e -> {
            if (!lastReceiptText.isEmpty()) {
                String file = ReceiptPrinter.saveToFile(lastReceiptText, lastOrderNumber);
                if (file != null)
                    JOptionPane.showMessageDialog(this, "Receipt saved as: " + file,
                            "Saved", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        clearBtn.addActionListener(e -> clearReceipt());

        btnPanel.add(printBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(clearBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // ── Public API ─────────────────────────────────────────────────────────────

    public void showReceipt(Order order) {
        lastReceiptText = ReceiptFormatter.generateReceipt(order);
        lastOrderNumber = order.getOrderNumber();
        receiptArea.setText(lastReceiptText);
        receiptArea.setCaretPosition(0);
    }

    public void clearReceipt() {
        receiptArea.setText("\n\n\n      Receipts will appear here after checkout.");
        lastReceiptText = "";
        lastOrderNumber = "";
    }

    // ── Helper ─────────────────────────────────────────────────────────────────

    private static JButton makeBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        return b;
    }
}
