package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import data_access.EdamamAPI;

public class BrowsePanel extends JPanel {
    private JTextField searchField;
    // Text field for entering search keywords
    private JComboBox<String> tagFilter;
    // Dropdown for dietary tags filter
    private JButton searchButton;
    // Button to execute the search
    private JPanel resultPanel;
    // Panel to display each recipe item with Save and View Detail buttons

    private List<List<String>> recipes;

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

        // Result panel to show recipes
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);

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
            // Clear previous results
            resultPanel.removeAll();

            // Fetch recipes from the API
            recipes = EdamamAPI.searchRecipes(keyword, 10);

            // For each recipe, create a panel with title, Save, and View Detail buttons
            for (List<String> recipe : recipes) {
                String recipeTitle = recipe.get(0);
                resultPanel.add(createRecipeItem(recipeTitle));
            }

            // Refresh the result panel to show new content
            resultPanel.revalidate();
            resultPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipes: " + ex.getMessage());
        }
    }

    // Helper method to create a panel for each recipe item with Save and View Detail buttons
    private JPanel createRecipeItem(String recipeTitle) {
        JPanel recipeItemPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(recipeTitle);

        // Buttons for each recipe item
        JButton saveButton = new JButton("Save");
        JButton viewDetailButton = new JButton("View Detail");

        // Add action listeners to buttons
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRecipe(recipeTitle);
            }
        });

        viewDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewRecipeDetail(recipeTitle);
            }
        });

        // Panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(viewDetailButton);

        // Add title and button panel to the recipe item panel
        recipeItemPanel.add(titleLabel, BorderLayout.CENTER);
        recipeItemPanel.add(buttonPanel, BorderLayout.EAST);

        return recipeItemPanel;
    }

    // Method to save a recipe (placeholder implementation)
    private void saveRecipe(String recipeTitle) {
        // Placeholder for save functionality
        JOptionPane.showMessageDialog(this, "Recipe saved: " + recipeTitle);
    }

    private void viewRecipeDetail(String recipeTitle) {
        try {
            for (List<String> recipe : recipes) {
                if (recipe.get(0).equals(recipeTitle)) {
                    // Remove "Recipe X: " prefix from the title
                    String title = recipeTitle.substring(recipeTitle.indexOf(":") + 1).trim();

                    // Create a panel to display the details
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                    detailsPanel.setPreferredSize(new Dimension(400, 0)); // Set fixed width
                    detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

                    // Add the bolded title
                    JLabel titleLabel = new JLabel("<html><b>" + title + "</b></html>");
                    detailsPanel.add(titleLabel);

                    // Add a space between the title and the URL
                    detailsPanel.add(Box.createVerticalStrut(10));

                    // Add the URL as a clickable, wrapping link
                    String url = recipe.get(1).replace("URL: ", "");
                    String formattedUrl = url.replaceAll("(.{50})", "$1<br>").trim(); // Trim leading/trailing whitespace or breaks
                    JLabel urlLabel = new JLabel("<html><a href='" + url + "'>" + formattedUrl + "</a></html>");
                    urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    urlLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            try {
                                Desktop.getDesktop().browse(new java.net.URI(url));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(BrowsePanel.this, "Error opening URL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    detailsPanel.add(urlLabel);

                    // Add a space between the URL and the calories
                    detailsPanel.add(Box.createVerticalStrut(10));

                    // Add calories rounded to the nearest whole number
                    double rawCalories = Double.parseDouble(recipe.get(2).replace("Calories: ", "").trim());
                    long roundedCalories = Math.round(rawCalories);
                    JLabel caloriesLabel = new JLabel("<html><b>Calories:</b> " + roundedCalories + "</html>");
                    detailsPanel.add(caloriesLabel);

                    // Add a space between the calories and the ingredients
                    detailsPanel.add(Box.createVerticalStrut(10));

                    // Add ingredients as a bulleted list
                    String ingredients = recipe.get(3).replace("Ingredients: ", "").replace("[", "").replace("]", "");
                    String[] ingredientList = ingredients.split(",");
                    StringBuilder bulletList = new StringBuilder("<html><b>Ingredients:</b><ul>");
                    for (String ingredient : ingredientList) {
                        bulletList.append("<li>").append(ingredient.trim()).append("</li>");
                    }
                    bulletList.append("</ul></html>");
                    JLabel ingredientsLabel = new JLabel(bulletList.toString());
                    detailsPanel.add(ingredientsLabel);

                    // Wrap the panel in a JScrollPane to allow vertical scrolling if needed
                    JScrollPane scrollPane = new JScrollPane(detailsPanel);
                    scrollPane.setPreferredSize(new Dimension(400, 300)); // Fixed width, height adjusts
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove scroll pane border

                    // Show the scrollable details panel in a JOptionPane
                    JOptionPane.showMessageDialog(this, scrollPane, "Recipe Details", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            // If no matching recipe is found
            JOptionPane.showMessageDialog(this, "Recipe not found.", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipe details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
