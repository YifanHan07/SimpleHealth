import entities.UserAccount;
import use_case.MyAccountUseCase;
import data_access.MyAccountController;
import view.MyAccountPanel;
import view.BrowsePanel;
import view.SavedRecipesPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create a UserAccount instance
        UserAccount userAccount = new UserAccount("John Cena", "", "Peanuts");

        // Create panels for each tab
        MyAccountPanel myAccountPanel = new MyAccountPanel(new MyAccountController(new MyAccountUseCase(userAccount)));
        BrowsePanel browsePanel = new BrowsePanel(userAccount);
        SavedRecipesPanel savedRecipesPanel = new SavedRecipesPanel(userAccount);

        // Create the main application window
        JFrame frame = new JFrame("Simple Health Application");
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create a tabbed pane to hold the tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("My Account", myAccountPanel);
        tabbedPane.addTab("Browse Recipes", browsePanel);
        tabbedPane.addTab("Saved Recipes", savedRecipesPanel);

        // Add the tabbed pane to the frame
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Refresh Saved Recipes when switching to the tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == savedRecipesPanel) {
                savedRecipesPanel.updateSavedRecipesPanel();
            }
        });

        // Show the window
        frame.setVisible(true);
    }
}
