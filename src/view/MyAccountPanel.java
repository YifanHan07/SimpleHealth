package view;

import data_access.MyAccountController;
import javax.swing.*;
import java.awt.*;

public class MyAccountPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField preferencesField;
    private final JTextField allergiesField;

    public MyAccountPanel(MyAccountController controller) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name Section
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        nameField = new JTextField(controller.getName(), 20);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Preferences Section
        JLabel preferencesLabel = new JLabel("Preferences:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(preferencesLabel, gbc);

        preferencesField = new JTextField(controller.getPreferences(), 20);
        gbc.gridx = 1;
        add(preferencesField, gbc);

        // Allergies Section
        JLabel allergiesLabel = new JLabel("Allergies:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(allergiesLabel, gbc);

        allergiesField = new JTextField(controller.getAllergies(), 20);
        gbc.gridx = 1;
        add(allergiesField, gbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            controller.updateName(nameField.getText());
            controller.updatePreferences(preferencesField.getText());
            controller.updateAllergies(allergiesField.getText());
            JOptionPane.showMessageDialog(this, "Details Saved Successfully!");
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(saveButton, gbc);
    }
}
