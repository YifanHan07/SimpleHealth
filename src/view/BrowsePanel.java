package view;

import data_access.EdamamAPI;
import entity.NutritionInfo;
import entity.Recipe;
import interface_adapter.mealplaner.MealPlannerController;
import interface_adapter.mealplaner.SaveRecipeController;
import interface_adapter.searchRecipe.tagController;
import use_case.searchRecipe.tagInteractor;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class BrowsePanel extends JPanel {
    private JTextField searchField;
    private JComboBox<Integer> resultsFilter;
    private JButton searchButton;
    private JPanel resultPanel;
    private JButton tagFilterButton = new JButton("Select Tags");
    private List<String> selectedTags = new ArrayList<>();

    private List<Recipe> recipes;

    private final tagController tagController;
    private final SaveRecipeController saveRecipeController;
    private final MealPlannerController mealPlannerController;
    private SaveRecipeHandler saveRecipeHandler;

    public BrowsePanel(SaveRecipeController saveRecipeController, MealPlannerController mealPlannerController) {
        this.saveRecipeController = saveRecipeController;
        this.mealPlannerController = mealPlannerController;

        tagInteractor interactor = new tagInteractor(new EdamamAPI());
        this.tagController = new tagController(interactor);

        setLayout(new BorderLayout());

        // Top panel for search input and filters
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        tagFilterButton.addActionListener(e -> tagSelection());

        resultsFilter = new JComboBox<>(new Integer[]{5, 10, 20});
        resultsFilter.setSelectedItem(10);

        searchButton = new JButton("Search");

        // Add components to the top panel
        topPanel.add(new JLabel("Keyword:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Tag:"));
        topPanel.add(tagFilterButton);
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

    private void tagSelection() {
        List<String> availableTags = tagController.getAvailableTags();
        tagSelectionView dialog = new tagSelectionView(
                (Frame) SwingUtilities.getWindowAncestor(this),
                availableTags,
                selectedTags,
                tags -> {
                    this.selectedTags = tags;
                    String buttonText = selectedTags.isEmpty()
                            ? "Select Tags"
                            : String.join(", ", selectedTags.subList(0, Math.min(selectedTags.size(), 3)))
                            + (selectedTags.size() > 3 ? "..." : "");
                    tagFilterButton.setText(buttonText);
                }
        );
        dialog.setVisible(true);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keyword cannot be empty.");
            return;
        }

        int maxResults = (int) resultsFilter.getSelectedItem();

        try {
            List<Recipe> recipes = tagController.fetchRecipes(keyword, maxResults, selectedTags);

            resultPanel.removeAll();
            if (recipes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No recipes found for the given keyword and tags.");
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

        // "Save to Collection" Button
        JButton saveButton = new JButton("Save to Collection");
        saveButton.addActionListener(e -> {
            System.out.println("Save button clicked for recipe: " + recipe.getLabel());
            saveRecipeHandler.save(recipe);
        });

        // "View Detail" Button
        JButton viewDetailButton = new JButton("View Detail");
        viewDetailButton.addActionListener(e -> showRecipeDetails(recipe));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(viewDetailButton);

        recipeItemPanel.add(titleLabel, BorderLayout.CENTER);
        recipeItemPanel.add(buttonPanel, BorderLayout.EAST);
        return recipeItemPanel;
    }


    private void showRecipeDetails(Recipe recipe) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Recipe Details", true);
        dialog.setSize(500, 600);
        dialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailsPanel.add(new JLabel("<html><h3>" + recipe.getLabel() + "</h3></html>"));

        // Add Recipe URL
        JLabel urlLabel = new JLabel("<html><a href='" + recipe.getUrl() + "'>View Recipe Online</a></html>");
        urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(recipe.getUrl()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BrowsePanel.this, "Error opening URL: " + ex.getMessage());
                }
            }
        });
        detailsPanel.add(urlLabel);

        try {
            // Fetch Nutrition Info
            NutritionInfo nutritionInfo = EdamamAPI.getNutritionInfo(recipe.getIngredientLines());

            // Basic nutrients
            detailsPanel.add(new JLabel("<html><b>Calories:</b> " + nutritionInfo.getCalories() + " kcal</html>"));
            detailsPanel.add(new JLabel("<html><b>Fat:</b> " + nutritionInfo.getFat() + " g</html>"));
            detailsPanel.add(new JLabel("<html><b>Carbohydrates:</b> " + nutritionInfo.getCarbohydrates() + " g</html>"));
            detailsPanel.add(new JLabel("<html><b>Fiber:</b> " + nutritionInfo.getFiber() + " g</html>"));
            detailsPanel.add(new JLabel("<html><b>Sugar:</b> " + nutritionInfo.getSugar() + " g</html>"));

            // Additional nutrients (Other Nutrition)
            if (!nutritionInfo.getAdditionalNutrients().isEmpty()) {
                detailsPanel.add(new JLabel("<html><b>Other Nutrition:</b></html>"));
                nutritionInfo.getAdditionalNutrients().forEach((key, value) -> {
                    detailsPanel.add(new JLabel("<html>" + key + ": " + value + "</html>"));
                });
            }

        } catch (Exception ex) {
            detailsPanel.add(new JLabel("<html><b>Error fetching nutrition info:</b> " + ex.getMessage() + "</html>"));
        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void setSaveRecipeHandler(SaveRecipeHandler handler) {
        this.saveRecipeHandler = handler;
    }

    @FunctionalInterface
    public interface SaveRecipeHandler {
        void save(Recipe recipe);
    }
}
