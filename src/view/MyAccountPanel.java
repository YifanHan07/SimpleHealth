package view;

import data_access.MyAccountController;
import javax.swing.*;
import java.awt.*;

public class MyAccountPanel extends JPanel {
    private final transient MyAccountController controller;

    public MyAccountPanel(MyAccountController controller) {
        this.controller = controller;

        // Set layout and border
        setLayout(new GridBagLayout()); // Centers components within the panel
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the panel

        // Create a panel for components
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical layout
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding inside the box

        // Preferences Section
        JPanel preferencesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel preferencesLabel = new JLabel("Preferences:");
        JTextField preferencesField = new JTextField(controller.getPreferences(), 20); // Use the controller
        preferencesPanel.add(preferencesLabel);
        preferencesPanel.add(preferencesField);

        // Allergies Section
        JPanel allergiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel allergiesLabel = new JLabel("Allergies:");
        JTextField allergiesField = new JTextField(controller.getAllergies(), 20); // Use the controller
        allergiesPanel.add(allergiesLabel);
        allergiesPanel.add(allergiesField);

        // Save Button Section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Update preferences and allergies using the controller
            controller.updatePreferences(preferencesField.getText());
            controller.updateAllergies(allergiesField.getText());

            // Show confirmation message
            JOptionPane.showMessageDialog(this, "Details Saved Successfully!");
        });
        buttonPanel.add(saveButton);

        // Add components to content panel
        contentPanel.add(preferencesPanel);
        contentPanel.add(Box.createVerticalStrut(10)); // Spacer
        contentPanel.add(allergiesPanel);
        contentPanel.add(Box.createVerticalStrut(20)); // Spacer
        contentPanel.add(buttonPanel);

        // Add content panel to the center of the main panel
        add(contentPanel, new GridBagConstraints());
    }
}
