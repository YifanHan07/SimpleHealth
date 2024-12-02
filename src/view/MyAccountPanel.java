package view;

import data_access.MyAccountController;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;

public class MyAccountPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField preferencesField;
    private final JTextField allergiesField;
    private LogoutController logoutController;

    public MyAccountPanel(MyAccountController controller ) {
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


        // Buttons Section
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(0, 10, 0, 10);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 0.5;

        // Logout Button
        JButton logout = new JButton("Log Out");
        logout.addActionListener(e -> {
            logoutController.execute(nameField.getText());
        });
        buttonGbc.gridx = 0;
        buttonPanel.add(logout, buttonGbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            controller.updateName(nameField.getText());
            controller.updatePreferences(preferencesField.getText());
            controller.updateAllergies(allergiesField.getText());
            JOptionPane.showMessageDialog(this, "Details Saved Successfully!");
        });
        buttonGbc.gridx = 1;
        buttonPanel.add(saveButton, buttonGbc);

        // Add button panel to main layout
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }
    public void updateFields(MyAccountController controller) {
        nameField.setText(controller.getName());
        preferencesField.setText(controller.getPreferences());
        allergiesField.setText(controller.getAllergies());
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
