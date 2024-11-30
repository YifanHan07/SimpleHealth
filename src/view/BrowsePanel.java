package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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
    private JButton tagFilterButton = new JButton("Select Tags");
    private List<String> selectedTags = new ArrayList<>();

    private List<List<String>> recipes;

    public BrowsePanel() {
        setLayout(new BorderLayout());

        // Top panel for search input and filter
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        tagFilterButton.addActionListener(e -> selection());

        searchButton = new JButton("Search");

        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Tag:"));
        topPanel.add(tagFilterButton);
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

    private void selection() {
        String[] tags = {"All", "alcohol-cocktail", "alcohol-free", "celery-free",
                "crustacean-free", "dairy-free", "egg-free", "fish-free", "fodmap-free", "gluten-free",
                "immuno-supportive", "keto-friendly", "kidney-friendly", "kosher", "low-fat-abs", "low-potassium",
                "low-sugar", "lupine-free", "Mediterranean", "mollusk-free", "mustard-free", "no-oil-added", "paleo",
                "peanut-free", "pescatarian", "pork-free", "red-meat-free", "sesame-free", "shellfish-free", "soy-free",
                "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian", "wheat-free"};

        // Create checkboxes for tags
        JPanel checkboxPanel = new JPanel(new GridLayout(tags.length, 1));
        JCheckBox[] checkBoxes = new JCheckBox[tags.length];

        for (int i = 0; i < tags.length; i++) {
            checkBoxes[i] = new JCheckBox(tags[i]);
            checkBoxes[i].setSelected(selectedTags.contains(tags[i]));
            checkboxPanel.add(checkBoxes[i]);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(checkboxPanel),
                "Select Tags",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            selectedTags.clear();
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedTags.add(checkBox.getText());
                }
            }

            String buttonText = selectedTags.isEmpty()
                    ? "Select Tags"
                    : String.join(", ", selectedTags.subList(0, Math.min(selectedTags.size(), 3)))
                    + (selectedTags.size() > 3 ? "..." : "");
            tagFilterButton.setText(buttonText);
        }
    }

    // Method to perform search and update result list
    private void performSearch() {
        String keyword = searchField.getText();
        String tag = String.join(", ", selectedTags);
        StringBuilder tags = new StringBuilder();
        if (Objects.equals(tag, "All")) {
            tags.append("&health=DASH");
        }
        else {
            for (int i = 0; i < selectedTags.size(); i++) {
                if (tags.length() > 0) {
                    tags.append("&");
                }
                tags.append("health=").append(selectedTags.get(i));
        }
        }

        try {
            // Clear previous results
            resultPanel.removeAll();

            // Fetch recipes from the API
            recipes = EdamamAPI.searchRecipes(keyword, 10, tags);

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
                                    // Extract ingredients list
                                    String ingredientsStr = recipe.get(3).replace("Ingredients: ", "").replace("[", "").replace("]", "");
                                    String[] ingredientArray = ingredientsStr.split(",");
                                    List<String> ingredientsList = new ArrayList<>();
                                    for (String ingredient : ingredientArray) {
                                        ingredientsList.add(ingredient.trim());
                                    }

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
                    return;
                }
            }

            // If no matching recipe is found
            JOptionPane.showMessageDialog(this, "Recipe not found.", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipe details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to display recipe details
    private void showRecipeDetails(JPanel detailsPanel, List<String> recipe) {
        // Remove "Recipe X: " prefix from the title
        String recipeTitle = recipe.get(0);
        String title = recipeTitle.substring(recipeTitle.indexOf(":") + 1).trim();

        // Add the bolded title
        JLabel titleLabel = new JLabel("<html><b>" + title + "</b></html>");
        detailsPanel.add(titleLabel);

        // Add a space between the title and the URL
        detailsPanel.add(Box.createVerticalStrut(10));

        // Add the URL as a clickable, wrapping link
        String url = recipe.get(1).replace("URL: ", "");
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
        double rawCalories = Double.parseDouble(recipe.get(2).replace("Calories: ", "").trim());
        long roundedCalories = Math.round(rawCalories);
        JLabel caloriesLabel = new JLabel("<html><b>Calories:</b> " + roundedCalories + "</html>");
        detailsPanel.add(caloriesLabel);

        // Add a space between the calories and the ingredients
        detailsPanel.add(Box.createVerticalStrut(10));

        // Add ingredients as a bulleted list
        String ingredients = recipe.get(3).replace("Ingredients: ", "").replace("[", "").replace("]", "");
        String[] ingredientArray = ingredients.split(",");
        StringBuilder bulletList = new StringBuilder("<html><b>Ingredients:</b><ul>");
        for (String ingredient : ingredientArray) {
            bulletList.append("<li>").append(ingredient.trim()).append("</li>");
        }
        bulletList.append("</ul></html>");
        JLabel ingredientsLabel = new JLabel(bulletList.toString());
        detailsPanel.add(ingredientsLabel);
    }
}
