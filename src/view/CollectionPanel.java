package view;

import entities.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionPanel extends JPanel {
    private final List<Recipe> savedRecipes; // List of saved recipes
    private final JPanel recipeListPanel;

    public CollectionPanel() {
        this.savedRecipes = new ArrayList<>();
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Saved Recipes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Recipe List Panel
        recipeListPanel = new JPanel();
        recipeListPanel.setLayout(new BoxLayout(recipeListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(recipeListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Add a new recipe to the collection
    public void addRecipe(Recipe recipe) {
        savedRecipes.add(recipe);
        JPanel recipePanel = new JPanel(new BorderLayout());
        JLabel recipeLabel = new JLabel(recipe.getLabel());
        JButton removeButton = new JButton("Remove");

        removeButton.addActionListener(e -> {
            savedRecipes.remove(recipe);
            recipeListPanel.remove(recipePanel);
            recipeListPanel.revalidate();
            recipeListPanel.repaint();
        });

        recipePanel.add(recipeLabel, BorderLayout.CENTER);
        recipePanel.add(removeButton, BorderLayout.EAST);
        recipeListPanel.add(recipePanel);

        recipeListPanel.revalidate();
        recipeListPanel.repaint();
    }

    // Get the saved recipes (useful for future features like Meal Planner)
    public List<Recipe> getSavedRecipes() {
        return savedRecipes;
    }
}
