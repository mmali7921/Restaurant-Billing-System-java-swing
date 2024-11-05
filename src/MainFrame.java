import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private TextPanel textPanel;
    private Toolbar toolbar;
    private FormPanel formPanel;

    MainFrame() {
        super("McDonald's");
        setLayout(new BorderLayout());
        textPanel = new TextPanel();
        toolbar = new Toolbar();
        formPanel = new FormPanel();

        formPanel.setFormListener(e -> {
            Burger userSelection = e.getUserSelected();
            textPanel.appendText(userSelection.toString() + "\n");
            if (!userSelection.getToppings().isEmpty()) {
                textPanel.appendText("Extras:- \n");
                int i = 1;
                for (Topping topping : userSelection.getToppings()) {
                    textPanel.appendText(i + ". " + topping.toString());
                    i++;
                }
            }
            Bill bill = new Bill(userSelection);
            textPanel.appendText(bill.generateReceipt());
        });

        add(formPanel, BorderLayout.WEST);
        add(toolbar, BorderLayout.NORTH);
        add(textPanel, BorderLayout.EAST);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
