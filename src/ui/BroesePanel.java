package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import api.EdamamAPI;

public class BrowsePanel extends JPanel {
    private JTextField searchField;      // Text field for entering search keywords
    private JComboBox<String> tagFilter; // Dropdown for dietary tags filter
    private JButton searchButton;        // Button to execute the search
    private JList<String> resultList;    // List to display recipe titles

    public BrowsePanel() {
        setLayout(new BorderLayout());

        // Top panel for search input and filter
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        tagFilter = new JComboBox<>(new String[]{"All", "Vegan", "Gluten-Free", "Low-Calorie", "High-Protein"});
        searchButton = new JButton("Search");

        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Tag:"));
        topPanel.add(tagFilter);
        topPanel.add(searchButton);

        // Result list to show recipes
        resultList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(resultList);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }

    // Method to perform search and update result list
    private void performSearch() {
        String keyword = searchField.getText();
        String selectedTag = (String) tagFilter.getSelectedItem();

        try {
            List<Recipe> recipes = EdamamAPI.searchRecipes(keyword, 10); // Use api.EdamamAPI to search
            DefaultListModel<String> listModel = new DefaultListModel<>();

            for (Recipe recipe : recipes) {
                listModel.addElement(recipe.getTitle()); // Display recipe titles
            }
            resultList.setModel(listModel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipes: " + ex.getMessage());
        }
    }
}
