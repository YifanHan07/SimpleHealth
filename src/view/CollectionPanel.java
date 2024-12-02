package view;

import entity.Recipe;
import interface_adapter.mealplaner.MealPlannerController;
import interface_adapter.mealplaner.SaveRecipeController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A panel to display and manage saved recipes and meal planning.
 */
public class CollectionPanel extends JPanel {
    private final JPanel recipeListPanel; // Panel to display the list of saved recipes
    private final SaveRecipeController saveRecipeController; // Controller for saving recipes
    private final MealPlannerController mealPlannerController; // Controller for meal planning

    /**
     * Constructor for the CollectionPanel.
     *
     * @param saveRecipeController   The controller for managing saved recipes.
     * @param mealPlannerController  The controller for managing meal planning.
     */
    public CollectionPanel(SaveRecipeController saveRecipeController, MealPlannerController mealPlannerController) {
        this.saveRecipeController = saveRecipeController;
        this.mealPlannerController = mealPlannerController;
        setLayout(new BorderLayout());

        // Title label at the top of the panel
        JLabel titleLabel = new JLabel("Saved Recipes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Panel to display the list of saved recipes
        recipeListPanel = new JPanel();
        recipeListPanel.setLayout(new BoxLayout(recipeListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(recipeListPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Button to open the meal planner
        JButton mealPlannerButton = new JButton("Meal Planner");
        mealPlannerButton.addActionListener(e -> openMealPlanner());
        add(mealPlannerButton, BorderLayout.SOUTH);

        // Initial refresh to load any existing saved recipes
        refresh();
    }

    /**
     * Add a new recipe to the collection.
     * Saves the recipe via the controller and refreshes the display.
     *
     * @param recipe The recipe to add to the collection.
     */
    public void addRecipe(Recipe recipe) {
        saveRecipeController.saveRecipe(recipe); // Save the recipe using the controller
        refresh(); // Refresh the display to show the new recipe
    }

    /**
     * Refresh the recipe list panel.
     * Retrieves the saved recipes from `SaveRecipeController` and updates the display.
     */
    public void refresh() {
        recipeListPanel.removeAll(); // Clear the current display
        List<Recipe> savedRecipes = saveRecipeController.getSavedRecipes(); // Get the saved recipes

        // Ensure recipes are displayed in the panel
        if (savedRecipes.isEmpty()) {
            recipeListPanel.add(new JLabel("No saved recipes."));
        } else {
            for (Recipe recipe : savedRecipes) {
                JPanel recipePanel = new JPanel(new BorderLayout());
                JLabel recipeLabel = new JLabel(recipe.getLabel()); // Display the recipe label

                // Button to toggle selection for the meal planner
                JButton selectButton = new JButton("Select");
                selectButton.addActionListener(e -> {
                    if (mealPlannerController.getMealPlan().contains(recipe)) {
                        mealPlannerController.removeRecipeFromMealPlan(recipe); // Remove from meal plan
                        JOptionPane.showMessageDialog(this, recipe.getLabel() + " removed from Meal Planner.");
                    } else {
                        mealPlannerController.addRecipeToMealPlan(recipe); // Add to meal plan
                        JOptionPane.showMessageDialog(this, recipe.getLabel() + " added to Meal Planner.");
                    }
                });

                // Add components to the recipe panel
                recipePanel.add(recipeLabel, BorderLayout.CENTER);
                recipePanel.add(selectButton, BorderLayout.EAST);

                // Add the recipe panel to the list
                recipeListPanel.add(recipePanel);
            }
        }

        // Refresh the panel layout and repaint it
        recipeListPanel.revalidate();
        recipeListPanel.repaint();
    }

    /**
     * Open the Meal Planner dialog.
     * Displays the current meal plan, including recipes and their total calories.
     */
    private void openMealPlanner() {
        List<Recipe> selectedRecipes = mealPlannerController.getMealPlan();
        if (selectedRecipes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No recipes selected for Meal Planner.");
            return;
        }

        // Create the Meal Planner dialog
        JDialog mealPlannerDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Meal Planner", true);
        mealPlannerDialog.setSize(400, 300);
        mealPlannerDialog.setLayout(new BorderLayout());

        // Meal Planner details panel
        JPanel mealPlannerPanel = new JPanel();
        mealPlannerPanel.setLayout(new BoxLayout(mealPlannerPanel, BoxLayout.Y_AXIS));

        int totalCalories = mealPlannerController.getTotalCalories();
        double totalFat = mealPlannerController.getTotalFat();
        double totalFiber = mealPlannerController.getTotalFiber();
        double totalSugar = mealPlannerController.getTotalSugar();

        StringBuilder details = new StringBuilder("<html><b>Selected Recipes:</b><br>");
        for (Recipe recipe : selectedRecipes) {
            details.append("- ").append(recipe.getLabel()).append("<br>");
        }
        details.append("<br><b>Total Calories:</b> ").append(totalCalories).append(" kcal<br>");
        details.append("<b>Total Fat:</b> ").append(totalFat).append(" g<br>");
        details.append("<b>Total Fiber:</b> ").append(totalFiber).append(" g<br>");
        details.append("<b>Total Sugar:</b> ").append(totalSugar).append(" g</html>");

        JLabel mealDetailsLabel = new JLabel(details.toString());
        mealPlannerPanel.add(mealDetailsLabel);

        // OK button to close the dialog
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> mealPlannerDialog.dispose());

        mealPlannerDialog.add(new JScrollPane(mealPlannerPanel), BorderLayout.CENTER);
        mealPlannerDialog.add(okButton, BorderLayout.SOUTH);

        mealPlannerDialog.setLocationRelativeTo(this);
        mealPlannerDialog.setVisible(true);
    }

}

