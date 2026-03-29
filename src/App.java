import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Set Nimbus Look and Feel for better UI styling
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to system default if Nimbus is unavailable
             try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
             catch (Exception ignored) {}
        }

        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
