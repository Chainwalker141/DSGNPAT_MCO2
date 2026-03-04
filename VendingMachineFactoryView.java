/**
 * The graphical user interface (GUI) view for the Vending Machine Factory application.
 * This class extends JFrame and provides a user interface to create and test vending machines.
 *
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class VendingMachineFactoryView extends JFrame { // VIEW

    private JPanel currentPanel; // checker for current panel

    private JPanel factoryPanel;
    private JPanel testingPanel;
    private CardLayout cardLayout;

    // Create
    private JButton createButton;
    private JButton testButton;
    private JButton exitButton;

    // test
    private JButton testFeaturesButton;
    private JButton maintenanceButton;
    private JButton exitTestingButton;

    private JTextArea itemDisplayArea;

    // Maintenance
    private JPanel maintenancePanel;
    private JButton restockButton;
    private JButton setPriceButton;
    private JButton collectPaymentButton;
    private JButton replenishChangeButton;
    private JButton summaryButton;

    // vending machine items table
    private JTable itemTable;
    private DefaultTableModel tableModel;
    protected   String[] columnNames = {"Slot", "Item Name", "Price", "Calories", "Stock"};

    // slot input bar
    private JTextField slotInputField;
    private JButton paymentButton;

    /**
     * Creates the graphical user interface (GUI) view for the Vending Machine Factory application.
     * This constructor initializes the main JFrame window and sets up the factory panel, testing panel, and maintenance panel using CardLayout.
     */
    public VendingMachineFactoryView() {
        setTitle("Vending Machine Factory");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the factory panel
        factoryPanel = new JPanel();
        factoryPanel.setLayout(new BorderLayout());

        // Add the label at the top
        JLabel titleLabel = new JLabel("VENDING MACHINE FACTORY", SwingConstants.CENTER);
        factoryPanel.add(titleLabel, BorderLayout.NORTH);

        // Add the buttons at the center
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        createButton = new JButton("Create a Vending Machine");
        testButton = new JButton("Test Vending Machine");
        exitButton = new JButton("Exit");


        buttonPanel.add(createButton);
        buttonPanel.add(testButton);
        buttonPanel.add(exitButton);
        factoryPanel.add(buttonPanel, BorderLayout.CENTER);

        // Creates the testing panel
        testingPanel = new JPanel();
        testingPanel.setLayout(new BorderLayout());

        // Adds the label at the top of the testing panel
        JLabel testingLabel = new JLabel("TESTING FEATURES", SwingConstants.CENTER);
        testingPanel.add(testingLabel, BorderLayout.NORTH);

        // adds slot input bar and table to testing panel
        JPanel testingContentPanel = new JPanel(new BorderLayout());
        testingPanel.add(testingContentPanel, BorderLayout.CENTER);

        // slot input bar
        slotInputField = new JTextField();
        paymentButton = new JButton("Payment");

        JPanel slotInputBar = new JPanel();
        slotInputBar.add(new JLabel("Enter slot number: "));
        slotInputBar.add(slotInputField);
        slotInputBar.add(paymentButton);

        testingContentPanel.add(slotInputBar, BorderLayout.NORTH);

        // initializes item table for machine
        Object[][] data = new Object[0][columnNames.length]; // empty data to be updated by helper method
        tableModel = new DefaultTableModel(data, columnNames);
        tableModel.setColumnIdentifiers(columnNames);
        itemTable = new JTable(tableModel);

        testingContentPanel.add(new JScrollPane(itemTable), BorderLayout.CENTER);


        // Adds the buttons at the center of the testing panel
        JPanel testingButtonPanel = new JPanel(new GridLayout(3, 1));
        testFeaturesButton = new JButton("Test Machine Features");
        maintenanceButton = new JButton("Maintenance");
        exitTestingButton = new JButton("Back");

        testingButtonPanel.add(testFeaturesButton);
        testingButtonPanel.add(maintenanceButton);
        testingButtonPanel.add(exitTestingButton);
        testingPanel.add(testingButtonPanel, BorderLayout.CENTER);

        // creates the maintenance panel
        maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridLayout(6, 1));

        restockButton = new JButton("Restock items");
        setPriceButton = new JButton("Set prices for an item");
        collectPaymentButton = new JButton("Collect Payment");
        replenishChangeButton = new JButton("Replenish Change");
        summaryButton = new JButton("Summary of Transactions");
        JButton backButton = new JButton("Back");

        maintenancePanel.add(restockButton);
        maintenancePanel.add(setPriceButton);
        maintenancePanel.add(collectPaymentButton);
        maintenancePanel.add(replenishChangeButton);
        maintenancePanel.add(summaryButton);
        maintenancePanel.add(backButton);

        // Sets up the CardLayout
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        add(factoryPanel, "factory");
        add(testingPanel, "testing");
        add(maintenancePanel, "maintenance");

        // Shows the factory panel initially
        cardLayout.show(getContentPane(), "factory");

        setVisible(true);
        itemTable.setOpaque(true);

    }


    // Methods to set ActionListeners for buttons in the factory panel
    public void setCreateButtonListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    public void setTestButtonListener(ActionListener listener) {
        testButton.addActionListener(listener);
    }

    public void setExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    // Methods to set ActionListeners for buttons in the testing panel
    public void setTestFeaturesButtonListener(ActionListener listener) {
        testFeaturesButton.addActionListener(listener);

    }

    public void setMaintenanceButtonListener(ActionListener listener) {
        maintenanceButton.addActionListener(listener);

        // Calls the setupVendingMachineView method
        Object[][] testData = new Object[0][5];
        String[] testColumnNames = {"Slot", "Item Name", "Price", "Calories", "Stock"};

    }

    public void setExitTestingButtonListener(ActionListener listener) {
        exitTestingButton.addActionListener(listener);
    }

    // Method to switch to the testing panel
    public void switchToTestingPanel() {
        cardLayout.show(getContentPane(), "testing");
    }


    // Method to switch back to the factory panel
    public void switchToFactoryPanel() {
        cardLayout.show(getContentPane(), "factory");
    }

}
