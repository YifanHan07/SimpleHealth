package view;

import Interface_adapter.RecipeDetailController;
import entites.RecipeDetail;

import javax.swing.*;
import java.awt.*;

public class RecipeDetailView extends JPanel {
    private final RecipeDetailController controller;
    private final JPanel mainPanel;

    public RecipeDetailView(RecipeDetailController controller) {
        this.controller = controller;
        this.mainPanel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER); // Add mainPanel to RecipeDetailView
    }

    public void showRecipeDetail(RecipeDetail recipeDetail) {
        mainPanel.removeAll();

        JTextArea detailArea = new JTextArea(recipeDetail.getFormattedDetails());
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setEditable(false);

        mainPanel.add(new JScrollPane(detailArea), BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
