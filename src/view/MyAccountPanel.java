package view;

import data_access.MyAccountController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyAccountPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField allergiesField;
    private final JComboBox<String> preferencesDropdown;
    private final JPanel selectedPreferencesPanel;
    private final JScrollPane scrollPane; // Scrollable container for the preferences panel
    private final List<String> selectedPreferences;

    public MyAccountPanel(MyAccountController controller) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        selectedPreferences = new ArrayList<>(List.of(controller.getPreferences().split(","))); // Initial preferences

        // Name Section
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        nameField = new JTextField(controller.getName(), 20);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Allergies Section
        JLabel allergiesLabel = new JLabel("Allergies:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(allergiesLabel, gbc);

        allergiesField = new JTextField(controller.getAllergies(), 20);
        gbc.gridx = 1;
        add(allergiesField, gbc);

        // Preferences Section
        JLabel preferencesLabel = new JLabel("Preferences:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(preferencesLabel, gbc);

        preferencesDropdown = new JComboBox<>(new String[]{
                "All", "alcohol-cocktail", "alcohol-free", "celery-free", "crustacean-free",
                "dairy-free", "egg-free", "fish-free", "fodmap-free", "gluten-free", "immuno-supportive",
                "keto-friendly", "kidney-friendly", "kosher", "low-fat-abs", "low-potassium", "low-sugar",
                "lupine-free", "Mediterranean", "mollusk-free", "mustard-free", "no-oil-added", "paleo",
                "peanut-free", "pescatarian", "pork-free", "red-meat-free", "sesame-free", "shellfish-free",
                "soy-free", "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian", "wheat-free"
        });
        preferencesDropdown.addActionListener(e -> addPreference());

        gbc.gridx = 1;
        add(preferencesDropdown, gbc);

        // Panel to show selected preferences (inside a scrollable container)
        selectedPreferencesPanel = new JPanel();
        selectedPreferencesPanel.setLayout(new BoxLayout(selectedPreferencesPanel, BoxLayout.Y_AXIS)); // Vertical alignment

        scrollPane = new JScrollPane(selectedPreferencesPanel);
        scrollPane.setPreferredSize(new Dimension(200, 100)); // Fixed size with scroll
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(scrollPane, gbc);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            controller.updateName(nameField.getText());
            controller.updatePreferences(String.join(",", selectedPreferences)); // Save as a comma-separated string
            controller.updateAllergies(allergiesField.getText());
            JOptionPane.showMessageDialog(this, "Details Saved Successfully!");
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(saveButton, gbc);

        updateSelectedPreferencesPanel(); // Initialize the tags display
    }

    private void addPreference() {
        String selected = (String) preferencesDropdown.getSelectedItem();
        if (!selectedPreferences.contains(selected)) {
            selectedPreferences.add(selected);
            updateSelectedPreferencesPanel();
        }
    }

    private void updateSelectedPreferencesPanel() {
        selectedPreferencesPanel.removeAll();
        for (String preference : selectedPreferences) {
            JLabel preferenceLabel = new JLabel(preference);
            JButton removeButton = new JButton("X");
            removeButton.addActionListener(e -> {
                selectedPreferences.remove(preference);
                updateSelectedPreferencesPanel();
            });

            JPanel tagPanel = new JPanel();
            tagPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            tagPanel.add(preferenceLabel);
            tagPanel.add(removeButton);

            selectedPreferencesPanel.add(tagPanel);
        }
        selectedPreferencesPanel.revalidate();
        selectedPreferencesPanel.repaint();
    }
}
