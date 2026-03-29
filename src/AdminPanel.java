import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Admin properties panel. Pinch protected.
 * Tab 3 of the application.
 */
public class AdminPanel extends JPanel {

    private static final Color PANEL_BG  = new Color(250, 245, 235);
    private static final Color SAFFRON   = new Color(230, 81, 0);
    private static final Color SAFFRON_LT= new Color(255, 167, 38);
    private static final Font  FONT_BOLD = new Font("SansSerif", Font.BOLD,   12);
    private static final Font  FONT_BODY = new Font("SansSerif", Font.PLAIN,  12);

    private JPanel     contentPanel;
    private JTextField nameField;
    private JTextField taglineField;
    private JPasswordField pinField;

    public AdminPanel() {
        setBackground(PANEL_BG);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Title ──────────────────────────────────────────────────────────────
        JLabel title = new JLabel("⚙  Settings & Admin", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(SAFFRON);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        add(title, BorderLayout.NORTH);

        // ── Content ────────────────────────────────────────────────────────────
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PANEL_BG);
        contentPanel.setVisible(false); // Hidden until PIN entered

        contentPanel.add(buildSettingsForm());

        JPanel centerWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrap.setOpaque(false);
        centerWrap.add(contentPanel);
        add(centerWrap, BorderLayout.CENTER);

        // ── Login / PIN overlay ────────────────────────────────────────────────
        showLoginDialog();
    }

    private JPanel buildSettingsForm() {
        JPanel p = new JPanel(new GridLayout(0, 2, 10, 14));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(SAFFRON_LT), "Restaurant Config",
                        TitledBorder.LEFT, TitledBorder.TOP, FONT_BOLD, SAFFRON),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        p.add(label("Restaurant Name:"));
        nameField = new JTextField(AppConfig.getRestaurantName(), 15);
        p.add(nameField);

        p.add(label("Tagline:"));
        taglineField = new JTextField(AppConfig.getTagline(), 15);
        p.add(taglineField);

        p.add(label("Admin PIN:"));
        pinField = new JPasswordField(AppConfig.getAdminPin(), 15);
        p.add(pinField);

        JButton saveBtn = new JButton("💾  Save Changes");
        saveBtn.setFont(FONT_BOLD);
        saveBtn.setBackground(new Color(46, 125, 50));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.setOpaque(true);
        saveBtn.addActionListener(e -> saveSettings());

        p.add(new JLabel("")); 
        p.add(saveBtn);
        
        return p;
    }

    private void saveSettings() {
        AppConfig.setRestaurantName(nameField.getText().trim());
        AppConfig.setTagline(taglineField.getText().trim());
        AppConfig.setAdminPin(new String(pinField.getPassword()));
        AppConfig.saveConfig();
        JOptionPane.showMessageDialog(this, "Settings saved successfully!\nRestart app to see all changes.",
                "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLoginDialog() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        JPasswordField pf = new JPasswordField(10);
        p.add(new JLabel("Enter Admin PIN to access settings:"), BorderLayout.NORTH);
        p.add(pf, BorderLayout.CENTER);

        int res = JOptionPane.showConfirmDialog(this, p, "Admin Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            String entered = new String(pf.getPassword());
            if (entered.equals(AppConfig.getAdminPin())) {
                contentPanel.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect PIN.", "Error", JOptionPane.ERROR_MESSAGE);
                showLoginDialog();
            }
        }
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BOLD);
        return l;
    }
}
