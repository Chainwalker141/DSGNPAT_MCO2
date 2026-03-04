/**
 * ItemDetailsDialog represents a non-modal dialog to display the details of a specific item in the vending machine.
 * The dialog displays information about the selected item, such as its name, price, and calorie count,
 * along with the current stock information of the vending machine.
 *
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import javax.swing.*;
import java.awt.*;

public class ItemDetailsDialog extends JDialog { // VIEW

    private Item item; // Store the item as an instance variable
    private double currentChange; // Store the current change as an instance variable
    private JTextArea textArea;

    /**
     * ItemDetailsDialog represents a non-modal dialog to display the items in the vending machine.
     * @param item The Item object representing the selected item whose details will be displayed in the dialog.
     * @param currentChange The amount of current change available in the vending machine to be displayed in the dialog.
     * @param vendingMachineData A string containing formatted information about the items in the vending machine,
     *                           which will be displayed in the text area of the dialog.
     */
    public ItemDetailsDialog(Item item, double currentChange, String vendingMachineData) {
        super((JFrame) null, "VENDING MACHINE", false); // null to make it non-modal
        this.item = item;
        this.currentChange = currentChange; // Set the current change

        // Creates and formats the UI components
        JTextArea textArea = new JTextArea(vendingMachineData);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Creates a label to display the current stock information
        JLabel stockLabel = new JLabel();

        // Creates a panel to hold the text area and the stock label
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.CENTER);
        panel.add(stockLabel, BorderLayout.SOUTH);

        // Adds the panel to the scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Adds components to the dialog
        add(scrollPane);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        setVisible(true);
    }
    public void updateTextArea(String updatedData) {
        textArea.setText(updatedData);
    }

}