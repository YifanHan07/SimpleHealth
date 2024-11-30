package view;

import data_access.EdamamAPI;
import entity.NutritionInfo;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrowsePanel extends JPanel {
    private JTextField searchField;
    private JComboBox<Integer> resultsFilter;
    private JButton searchButton;
    private JPanel resultPanel;
    private JButton tagFilterButton = new JButton("Select Tags");
    private List<String> selectedTags = new ArrayList<>();

    private List<Recipe> recipes;
    private SaveRecipeHandler saveRecipeHandler;

    public BrowsePanel() {
        setLayout(new BorderLayout());

        // Top panel for search input and filters
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        tagFilterButton.addActionListener(e -> selection());


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

    private void performSearch() {
        String keyword = searchField.getText().trim();
        String tag = String.join(", ", selectedTags);
        StringBuilder tags = new StringBuilder();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keyword cannot be empty.");
            return;
        }
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

        int maxResults = (int) resultsFilter.getSelectedItem();

        try {
            resultPanel.removeAll();
            recipes = EdamamAPI.searchRecipes(keyword, maxResults, tags);

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
                saveRecipeHandler.save(recipe);
                JOptionPane.showMessageDialog(this, recipe.getLabel() + " saved to Collection.");
            }
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
        JLabel urlLabel = new JLabel("<html><a href='" + recipe.getUrl() + "'>View Full Recipe</a></html>");
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

            // Additional nutrients
            detailsPanel.add(new JLabel("<html><b>Other Nutrition:</b></html>"));
            nutritionInfo.getAdditionalNutrients().forEach((key, value) -> {
                detailsPanel.add(new JLabel("<html>" + key + ": " + value + "</html>"));
            });

        } catch (Exception ex) {
            detailsPanel.add(new JLabel("Error fetching nutrition info: " + ex.getMessage()));
        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());

        dialog.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
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
