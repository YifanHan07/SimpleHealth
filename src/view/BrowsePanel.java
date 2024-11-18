package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Interface_adapter.RecipeDetailController;
import Interface_adapter.RecipeDetailPresenter;
import data_access.EdamamAPI;
import entites.RecipeDetail;
import use_case.RecipeDetailInteractor;

public class BrowsePanel extends JPanel {
    private JTextField searchField;
    // Text field for entering search keywords
    private JComboBox<String> tagFilter;
    // Dropdown for dietary tags filter
    private JButton searchButton;
    // Button to execute the search
    private JPanel resultPanel;
    // Panel to display each recipe item with Save and View Detail buttons

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

            List<List<String>> recipes = EdamamAPI.searchRecipes(keyword, 10);

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
        String keyword = searchField.getText();

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
                try {
                    viewRecipeDetail(keyword, recipeTitle);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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

    // Method to view recipe details (placeholder implementation)
    private void viewRecipeDetail(String keyword, String recipeTitle) throws Exception {
        RecipeDetailInteractor recipeDetailInteractor = new RecipeDetailInteractor();
        RecipeDetailController controller = new RecipeDetailController(recipeDetailInteractor);

        RecipeDetailView view = new RecipeDetailView(controller);

        RecipeDetail recipeDetail = recipeDetailInteractor.execute(keyword, recipeTitle);
        view.showRecipeDetail(recipeDetail); // Pass the detail to the view

        JFrame frame = new JFrame("Recipe Detail - " + recipeTitle);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame when done
        frame.add(view); // Add the RecipeDetailView to the frame
        frame.setVisible(true);
    }
}
