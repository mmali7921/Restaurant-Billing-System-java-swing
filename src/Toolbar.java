import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel {
    private JLabel textLabel;

    Toolbar() {
        setBorder(BorderFactory.createEtchedBorder());
        textLabel = new JLabel("Welcome to McDonald's");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.lightGray);
        add(textLabel);
    }
}
