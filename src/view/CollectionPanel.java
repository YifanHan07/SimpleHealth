package view;

import entities.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionPanel extends JPanel {
    private final List<Recipe> savedRecipes; // List of saved recipes
    private final List<Recipe> selectedRecipes; // List of selected recipes for the meal planner
    private final JPanel recipeListPanel; // Panel to display saved recipes

    public CollectionPanel() {
        this.savedRecipes = new ArrayList<>();
        this.selectedRecipes = new ArrayList<>();
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

        // Meal Planner Button
        JButton mealPlannerButton = new JButton("Meal Planner");
        mealPlannerButton.addActionListener(e -> openMealPlanner());
        add(mealPlannerButton, BorderLayout.SOUTH);
    }

    // Add a new recipe to the collection
    public void addRecipe(Recipe recipe) {
        savedRecipes.add(recipe);
        JPanel recipePanel = new JPanel(new BorderLayout());
        JLabel recipeLabel = new JLabel(recipe.getLabel());

        // Select Button
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            if (!selectedRecipes.contains(recipe)) {
                selectedRecipes.add(recipe);
                JOptionPane.showMessageDialog(this, recipe.getLabel() + " added to Meal Planner.");
            } else {
                selectedRecipes.remove(recipe);
                JOptionPane.showMessageDialog(this, recipe.getLabel() + " removed from Meal Planner.");
            }
        });

        // Add components to recipe panel
        recipePanel.add(recipeLabel, BorderLayout.CENTER);
        recipePanel.add(selectButton, BorderLayout.EAST);

        // Add to recipe list panel
        recipeListPanel.add(recipePanel);
        recipeListPanel.revalidate();
        recipeListPanel.repaint();
    }

    // Open the Meal Planner dialog
    private void openMealPlanner() {
        if (selectedRecipes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No recipes selected for Meal Planner.");
            return;
        }

        // Create Meal Planner dialog
        JDialog mealPlannerDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Meal Planner", true);
        mealPlannerDialog.setSize(400, 300);
        mealPlannerDialog.setLayout(new BorderLayout());

        // Meal Planner Details
        JPanel mealPlannerPanel = new JPanel();
        mealPlannerPanel.setLayout(new BoxLayout(mealPlannerPanel, BoxLayout.Y_AXIS));

        int totalCalories = 0;
        StringBuilder details = new StringBuilder("<html><b>Selected Recipes:</b><br>");
        for (Recipe recipe : selectedRecipes) {
            details.append("- ").append(recipe.getLabel()).append("<br>");
            totalCalories += (int) recipe.getCalories();
        }
        details.append("<br><b>Total Calories:</b> ").append(totalCalories).append(" kcal</html>");

        JLabel mealDetailsLabel = new JLabel(details.toString());
        mealPlannerPanel.add(mealDetailsLabel);

        // OK Button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> mealPlannerDialog.dispose());

        mealPlannerDialog.add(new JScrollPane(mealPlannerPanel), BorderLayout.CENTER);
        mealPlannerDialog.add(okButton, BorderLayout.SOUTH);

        mealPlannerDialog.setLocationRelativeTo(this);
        mealPlannerDialog.setVisible(true);
    }
}
