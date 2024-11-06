import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class FormPanel extends JPanel implements ActionListener {

    private JButton submitBtn;
    private JButton checkOut;
    private JList burgerList;
    private JButton printSaveBtn;
    private JButton clearBtn;
    private Burger userSelected;
    private int maxToppings;
    ArrayList<JCheckBox> burgerToppings = new ArrayList();
    private FormListener formListener;



    FormPanel(){


            createTable(); // Ensures the 'bills' table is created

        Dimension dimension = getPreferredSize();
        dimension.width = 490;
        setPreferredSize(dimension);

        TitledBorder innerBorder = BorderFactory.createTitledBorder("*** Menu *** ");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        Border fullBorder = BorderFactory.createCompoundBorder(outerBorder,innerBorder);
        innerBorder.setTitleJustification(TitledBorder.CENTER);
        setBorder(fullBorder);


        //Object
        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);
        checkOut = new JButton("Checkout");
        checkOut.addActionListener(this);
        checkOut.setEnabled(false);

        printSaveBtn = new JButton("Print/Save Bill"); // Initialize print/save button
        printSaveBtn.addActionListener(this);
        printSaveBtn.setEnabled(false); // Initially disabled

        clearBtn = new JButton("Clear"); // Initialize clear button
        clearBtn.addActionListener(this);
        clearBtn.setEnabled(true); // Initially disabled

        burgerList = new JList();
        DefaultListModel burgerModel = new DefaultListModel();

        for(Burger burger : Fridge.prepareBurgers()){
            burgerModel.addElement(burger);
        }
        burgerList.setModel(burgerModel);
        burgerList.setSelectedIndex(-1);
        burgerList.setPreferredSize(new Dimension(400,100));
        burgerList.setBorder(BorderFactory.createEtchedBorder());
        checkBoxInitialization();
        for (JCheckBox check: burgerToppings) {
            check.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    int checked = 0;

                    for (JCheckBox check:burgerToppings) {
                        if (check.isSelected()){
                            checked++;
                        }
                    }

                    check.setEnabled(false);
                    for (int i = 0; i < burgerToppings.size(); i++) {
                        if(check == burgerToppings.get(i)){
                            userSelected.setToppings(Fridge.prepareToppings().get(i));

                        }
                    }
                }
            });
        }
        componentLayout();
    }
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:billing.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void setFormListener(FormListener formListener) {
        this.formListener = formListener;
    }
    public void resetForm(ActionListener actionListener) {
        printSaveBtn.addActionListener(actionListener);
        clearBtn.addActionListener(actionListener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton clicked = (JButton) e.getSource();

        switch (clicked.getText()){
            case "Submit":
                int index = (Integer) burgerList.getSelectedIndex();
                if(index < 0){
                    JOptionPane.showMessageDialog(
                            null,
                            "Please Select a Burger",	//message
                            "Error",			//title
                            JOptionPane.ERROR_MESSAGE);
                }
                else{
                    // Reset toppings before selecting a new burger
                    userSelected = (Burger) burgerList.getSelectedValue();
                    userSelected.clearToppings();  // Clear any previously selected toppings

                    submitBtn.setEnabled(false);
                    burgerList.setEnabled(false);

                    // Enable toppings for selection
                    for (JCheckBox checkBox: burgerToppings){
                        checkBox.setEnabled(true);
                    }

                    checkOut.setEnabled(true);
                }
                break;


            case "Checkout":
                FormEvent fe = new FormEvent(e,userSelected);
                if(formListener != null){
                    formListener.formEventTrigger(fe);
                }
                for (JCheckBox checkBox : burgerToppings) {
                    checkBox.setEnabled(false);  // Disable toppings after selection
                }
                checkOut.setEnabled(false);
                printSaveBtn.setEnabled(true);
                break;
            case "Print/Save Bill":

                saveBillToDatabase();
                clearInputs();//// Clear inputs after saving
                printSaveBtn.setEnabled(false); // Disable the print/save button after saving
                break;
            case "Clear":
                clearInputs();


                break;

            default:break;
        }
    }

    private void componentLayout() {
        // Layout
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.weightx = 1;
        gc.weighty = 1;

        //        First Row
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,5);
        add(new JLabel("Please Select: "),gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,0,0,0);
        add(burgerList,gc);

        //        Second Row
        gc.gridy++;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(submitBtn,gc);

        // Third Row
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(new JLabel("Select Toppings:"),gc);

        // CheckBox Rows
        int i = 0;
        for(JCheckBox checkBox : burgerToppings){
            gc.gridx = 0;
            gc.gridy++;
            gc.weighty = 0.1;
            gc.anchor = GridBagConstraints.LINE_END;
            gc.insets = new Insets(0,0,0,5);
            add(new JLabel(Fridge.prepareToppings().get(i).toString()),gc);

            gc.gridx++;
            gc.anchor = GridBagConstraints.LINE_START;
            gc.insets = new Insets(0,0,0,0);
            add(checkBox,gc);

            checkBox.setEnabled(false);

            i++;
        }
        //gc.gridx = 0;
        gc.gridy++;
        gc.weighty = 2.0;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(checkOut,gc);


        // Add print/save button
        gc.gridy++;
        add(printSaveBtn, gc); // Place print/save button next to checkout

        // Add clear button
        // Reset gridx for the clear button
        gc.gridy++;   // Move to the next row

        add(clearBtn, gc); // Add clear button below the print/save button



    }

    private void buttonLayout(){
        setLayout(new BorderLayout());

        add(new JLabel("Hello"),BorderLayout.SOUTH);
    }

    public void checkBoxInitialization(){
        JCheckBox tomato = new JCheckBox("Tomato");
        JCheckBox lettuce = new JCheckBox("Lettuce");
        JCheckBox cheese = new JCheckBox("Cheese");
        JCheckBox carrot = new JCheckBox("Carrot");
        JCheckBox greenPepper = new JCheckBox("Green Pepper");
        JCheckBox olives = new JCheckBox("Olives");

        burgerToppings.add(tomato);
        burgerToppings.add(lettuce);
        burgerToppings.add(cheese);
        burgerToppings.add(carrot);
        burgerToppings.add(greenPepper);
        burgerToppings.add(olives);
    }

    private void saveBillToDatabase() {
        // Calculate the total and netTotal (including tax)
        double total = userSelected.getPrice();
        StringBuilder toppings = new StringBuilder();

        // Loop through the checkboxes and add the selected ones
        for (JCheckBox checkBox : burgerToppings) {
            if (checkBox.isSelected()) {
                toppings.append(checkBox.getText()).append(", ");
                total += Fridge.prepareToppings().get(burgerToppings.indexOf(checkBox)).getPrice();
            }
        }

        // Remove last comma and space if toppings were selected
        if (toppings.length() > 0) {
            toppings.setLength(toppings.length() - 2); // Remove the trailing comma
        }

        double tax = total * 0.15;
        double netTotal = total + tax;

        // SQL insert
        String sql = "INSERT INTO bills(burger_name, toppings, price, net_total) VALUES(?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userSelected.getName());
            pstmt.setString(2, toppings.toString()); // Save the selected toppings
            pstmt.setDouble(3, userSelected.getPrice());
            pstmt.setDouble(4, netTotal); // Save the net total
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bill saved to SQLite database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    private void clearInputs() {
        burgerList.setSelectedIndex(-1);
        for (JCheckBox checkBox : burgerToppings) {
            checkBox.setSelected(false);
            checkBox.setEnabled(false);
        }
        submitBtn.setEnabled(true);
        burgerList.setEnabled(true);
        checkOut.setEnabled(false);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS bills (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "burger_name TEXT," +
                "toppings TEXT," +
                "price REAL," +
                "net_total REAL)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




}