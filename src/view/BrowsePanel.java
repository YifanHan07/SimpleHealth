package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import data_access.EdamamAPI;
import entites.Recipe;

public class BrowsePanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> tagFilter;
    private JComboBox<Integer> resultsFilter; // Dropdown for max results
    private JButton searchButton;
    private JPanel resultPanel;

    private List<Recipe> recipes;

    public BrowsePanel() {
        setLayout(new BorderLayout());

        // Top panel for search input and filters
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);

        tagFilter = new JComboBox<>(new String[]{"All", "alcohol-cocktail", "alcohol-free", "celery-free",
                "crustacean-free", "dairy-free","egg-free", "fish-free", "fodmap-free", "gluten-free",
                "immuno-supportive", "keto-friendly", "kidney-friendly", "kosher", "low-fat-abs", "low-potassium",
                "low-sugar", "lupine-free", "Mediterranean", "mollusk-free", "mustard-free", "no-oil-added", "paleo",
                "peanut-free", "pescatarian", "pork-free", "red-meat-free", "sesame-free", "shellfish-free", "soy-free",
                "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian", "wheat-free"});

        // Updated dropdown for selecting the number of results
        resultsFilter = new JComboBox<>(new Integer[]{5, 10, 20});
        resultsFilter.setSelectedItem(10); // Default value

        searchButton = new JButton("Search");

        // Add components to the top panel
        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Tag:"));
        topPanel.add(tagFilter);
        topPanel.add(new JLabel("Results:")); // Label for the new dropdown
        topPanel.add(resultsFilter);
        topPanel.add(searchButton);

        // Result panel to show recipes
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Action listener for the search button
        searchButton.addActionListener(e -> performSearch());
    }

    // Method to perform search and update result list
    private void performSearch() {
        String keyword = searchField.getText();
        String tag = tagFilter.getSelectedItem().toString();
        if (tag.equals("All")) {
            tag = "DASH";
        }

        int maxResults = (int) resultsFilter.getSelectedItem(); // Get the selected maxResults value

        System.out.println("maxResults: " + maxResults);

        try {
            // Clear previous results
            resultPanel.removeAll();

            // Fetch recipes from the API (up to 25 due to API limitations)
            recipes = EdamamAPI.searchRecipes(keyword, 25, tag);

            // Display only the number of recipes selected by the user
            int recipesToDisplay = Math.min(maxResults, recipes.size());
            for (int i = 0; i < recipesToDisplay; i++) {
                Recipe recipe = recipes.get(i);
                resultPanel.add(createRecipeItem(recipe));
            }

            // Refresh the result panel to show new content
            resultPanel.revalidate();
            resultPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipes: " + ex.getMessage());
        }
    }

    // Helper method to create a panel for each recipe item with Save and View Detail buttons
    private JPanel createRecipeItem(Recipe recipe) {
        JPanel recipeItemPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(recipe.getLabel());

        // Buttons for each recipe item
        JButton saveButton = new JButton("Save");
        JButton viewDetailButton = new JButton("View Detail");

        // Add action listeners to buttons
        saveButton.addActionListener(e -> saveRecipe(recipe));

        viewDetailButton.addActionListener(e -> viewRecipeDetail(recipe));

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
    private void saveRecipe(Recipe recipe) {
        // Placeholder for save functionality
        JOptionPane.showMessageDialog(this, "Recipe saved: " + recipe.getLabel());
    }

    private void viewRecipeDetail(Recipe recipe) {
        try {
            // Create a custom dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Recipe Details", true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(500, 400);
            dialog.setLayout(new BorderLayout());

            // Panel for content
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

            // Initial content is the recipe details
            showRecipeDetails(detailsPanel, recipe);

            // Add buttons at the bottom
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            // OK Button
            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> dialog.dispose());

            // Toggle Button (acts as a switch between views)
            JButton toggleButton = new JButton("View Nutrition Facts");

            // Add action listener to the toggle button
            toggleButton.addActionListener(new ActionListener() {
                boolean showingNutritionFacts = false;

                @Override
                public void actionPerformed(ActionEvent e) {
                    detailsPanel.removeAll();
                    if (!showingNutritionFacts) {
                        // Fetch and display nutrition facts
                        try {
                            // Use the ingredient list directly
                            List<String> ingredientsList = recipe.getIngredientLines();

                            // Fetch nutrition facts using EdamamAPI
                            List<String> nutritionFacts = EdamamAPI.getNutritionFacts(ingredientsList);

                            // Build the HTML content
                            StringBuilder nutritionFactsHtml = new StringBuilder("<html><b>Nutrition Facts:</b><br><ul>");
                            for (String fact : nutritionFacts) {
                                nutritionFactsHtml.append("<li>").append(fact).append("</li>");
                            }
                            nutritionFactsHtml.append("</ul></html>");
                            JLabel nutritionFactsLabel = new JLabel(nutritionFactsHtml.toString());
                            detailsPanel.add(nutritionFactsLabel);

                            // Update button text
                            toggleButton.setText("View Recipe Details");
                            showingNutritionFacts = true;

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(BrowsePanel.this, "Error fetching nutrition facts: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Display recipe details again
                        showRecipeDetails(detailsPanel, recipe);

                        // Update button text
                        toggleButton.setText("View Nutrition Facts");
                        showingNutritionFacts = false;
                    }
                    detailsPanel.revalidate();
                    detailsPanel.repaint();
                }
            });

            buttonPanel.add(toggleButton);
            buttonPanel.add(okButton);

            dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipe details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to display recipe details
    private void showRecipeDetails(JPanel detailsPanel, Recipe recipe) {
        // Add the bolded title
        JLabel titleLabel = new JLabel("<html><b>" + recipe.getLabel() + "</b></html>");
        detailsPanel.add(titleLabel);

        // Add a space between the title and the URL
        detailsPanel.add(Box.createVerticalStrut(10));

        // Add the URL as a clickable, wrapping link
        String url = recipe.getUrl();
        String formattedUrl = url.replaceAll("(.{50})", "$1<br>").trim();
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
        double rawCalories = recipe.getCalories();
        long roundedCalories = Math.round(rawCalories);
        JLabel caloriesLabel = new JLabel("<html><b>Calories:</b> " + roundedCalories + "</html>");
        detailsPanel.add(caloriesLabel);

        // Add a space between the calories and the ingredients
        detailsPanel.add(Box.createVerticalStrut(10));

        // Add ingredients as a bulleted list
        List<String> ingredientLines = recipe.getIngredientLines();
        StringBuilder bulletList = new StringBuilder("<html><b>Ingredients:</b><ul>");
        for (String ingredient : ingredientLines) {
            bulletList.append("<li>").append(ingredient.trim()).append("</li>");
        }
        bulletList.append("</ul></html>");
        JLabel ingredientsLabel = new JLabel(bulletList.toString());
        detailsPanel.add(ingredientsLabel);
    }
}