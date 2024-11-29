package view;

import entities.Recipe;
import entities.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SavedRecipesPanel extends JPanel {
    private final transient UserAccount userAccount;
    private final JPanel recipesDisplayPanel;

    public SavedRecipesPanel(UserAccount userAccount) {
        this.userAccount = userAccount;
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Saved Recipes");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Recipes Display Panel
        recipesDisplayPanel = new JPanel();
        recipesDisplayPanel.setLayout(new BoxLayout(recipesDisplayPanel, BoxLayout.Y_AXIS));

        // Scrollable Pane for Saved Recipes
        JScrollPane scrollPane = new JScrollPane(recipesDisplayPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        updateSavedRecipesPanel();
    }

    public void updateSavedRecipesPanel() {
        recipesDisplayPanel.removeAll();

        List<Recipe> savedRecipes = userAccount.getSavedRecipes();
        if (savedRecipes.isEmpty()) {
            JLabel noRecipesLabel = new JLabel("No recipes saved yet.");
            noRecipesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            recipesDisplayPanel.add(noRecipesLabel);
        } else {
            for (Recipe recipe : savedRecipes) {
                recipesDisplayPanel.add(createRecipeItem(recipe));
            }
        }

        recipesDisplayPanel.revalidate();
        recipesDisplayPanel.repaint();
    }

    private JPanel createRecipeItem(Recipe recipe) {
        JPanel recipeItemPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(recipe.getLabel());
        JButton viewDetailButton = new JButton("View Details");

        viewDetailButton.addActionListener(e -> viewRecipeDetail(recipe));

        recipeItemPanel.add(titleLabel, BorderLayout.CENTER);
        recipeItemPanel.add(viewDetailButton, BorderLayout.EAST);

        return recipeItemPanel;
    }


    private void viewRecipeDetail(Recipe recipe) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Recipe Details",
                true);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        // Add recipe details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.add(new JLabel("<html><b>" + recipe.getLabel() + "</b></html>"));
        detailsPanel.add(new JLabel("<html><b>URL:</b> <a href='" + recipe.getUrl() + "'>" + recipe.getUrl() + "</a></html>"));
        detailsPanel.add(new JLabel("<html><b>Calories:</b> " + recipe.getCalories() + "</html>"));

        // Ingredients
        detailsPanel.add(new JLabel("<html><b>Ingredients:</b></html>"));
        for (String ingredient : recipe.getIngredientLines()) {
            detailsPanel.add(new JLabel("- " + ingredient));
        }

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);

        // OK Button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
