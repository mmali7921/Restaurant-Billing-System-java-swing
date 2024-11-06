import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private JTextArea textArea;

    TextPanel() {
        setPreferredSize(new Dimension(490, 800));
        setBorder(BorderFactory.createTitledBorder("Order Summary"));
        textArea = new JTextArea();
        textArea.setFont(new Font("Verdana", Font.BOLD, 12));
        setLayout(new BorderLayout());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void appendText(String text) {
        textArea.append(text);
    }
    public void clearText() {
        textArea.setText("");
    }
}
