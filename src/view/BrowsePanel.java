package view;

import data_access.EdamamAPI;
import entities.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BrowsePanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> tagFilter;
    private JComboBox<Integer> resultsFilter;
    private JButton searchButton;
    private JPanel resultPanel;

    private List<Recipe> recipes;
    private SaveRecipeHandler saveRecipeHandler;

    public BrowsePanel() {
        setLayout(new BorderLayout());

        // Top panel for search input and filters
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);

        tagFilter = new JComboBox<>(new String[]{
                "All", "gluten-free", "vegan", "vegetarian", "keto-friendly", "low-sugar", "low-fat-abs"
        });

        resultsFilter = new JComboBox<>(new Integer[]{5, 10, 20});
        resultsFilter.setSelectedItem(10);

        searchButton = new JButton("Search");

        // Add components to the top panel
        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Tag:"));
        topPanel.add(tagFilter);
        topPanel.add(new JLabel("Results:"));
        topPanel.add(resultsFilter);
        topPanel.add(searchButton);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        String tag = tagFilter.getSelectedItem().toString();

        if (tag.equalsIgnoreCase("All")) {
            tag = ""; // No health parameter
        }

        int maxResults = (int) resultsFilter.getSelectedItem();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keyword cannot be empty.");
            return;
        }

        try {
            resultPanel.removeAll();
            recipes = EdamamAPI.searchRecipes(keyword, maxResults, tag);

            if (recipes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No recipes found for the given keyword and tag.");
                return;
            }

            for (Recipe recipe : recipes) {
                resultPanel.add(createRecipeItem(recipe));
            }

            resultPanel.revalidate();
            resultPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching recipes: " + ex.getMessage());
        }
    }

    private JPanel createRecipeItem(Recipe recipe) {
        JPanel recipeItemPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(recipe.getLabel());

        // "Save" Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (saveRecipeHandler != null) {
                saveRecipeHandler.save(recipe); // Pass recipe to handler
                JOptionPane.showMessageDialog(this, recipe.getLabel() + " saved to Collection.");
            }
        });

        // "View Detail" Button
        JButton viewDetailButton = new JButton("View Detail");
        viewDetailButton.addActionListener(e -> showRecipeDetails(recipe));

        // Add buttons to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(viewDetailButton);

        recipeItemPanel.add(titleLabel, BorderLayout.CENTER);
        recipeItemPanel.add(buttonPanel, BorderLayout.EAST);
        return recipeItemPanel;
    }

    private void showRecipeDetails(Recipe recipe) {
        // Create a modal dialog to display recipe details
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Recipe Details", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add recipe details
        detailsPanel.add(new JLabel("<html><b>Recipe Name:</b> " + recipe.getLabel() + "</html>"));
        detailsPanel.add(Box.createVerticalStrut(10)); // Spacer
        detailsPanel.add(new JLabel("<html><b>Calories:</b> " + Math.round(recipe.getCalories()) + " kcal</html>"));
        detailsPanel.add(Box.createVerticalStrut(10)); // Spacer

        // Ingredients
        JLabel ingredientsLabel = new JLabel("<html><b>Ingredients:</b></html>");
        detailsPanel.add(ingredientsLabel);

        DefaultListModel<String> ingredientListModel = new DefaultListModel<>();
        for (String ingredient : recipe.getIngredientLines()) {
            ingredientListModel.addElement(ingredient);
        }
        JList<String> ingredientList = new JList<>(ingredientListModel);
        ingredientList.setEnabled(false); // Read-only list
        detailsPanel.add(new JScrollPane(ingredientList));

        // OK Button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public interface SaveRecipeHandler {
        void save(Recipe recipe);
    }

    public void setSaveRecipeHandler(SaveRecipeHandler handler) {
        this.saveRecipeHandler = handler;
    }
}
