import entities.UserAccount;
import use_case.MyAccountUseCase;
import data_access.MyAccountController;
import view.BrowsePanel;
import view.MyAccountPanel;
import view.CollectionPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create User Account
        UserAccount userAccount = new UserAccount("John Cena", "Low Carb", "Peanuts");

        // Create Use Case and Controller
        MyAccountUseCase useCase = new MyAccountUseCase(userAccount);
        MyAccountController controller = new MyAccountController(useCase);

        // Create Panels
        MyAccountPanel myAccountPanel = new MyAccountPanel(controller);
        BrowsePanel browsePanel = new BrowsePanel();
        CollectionPanel collectionPanel = new CollectionPanel();

//        // Integrate Collection Panel with Browse Panel
//        browsePanel.setSaveRecipeHandler(recipe -> {
//            collectionPanel.addRecipe(recipe); // Save recipe to collection
//            JOptionPane.showMessageDialog(null, "Recipe saved to Collection!");
//        });

        // Create JFrame
        JFrame frame = new JFrame("Simple Health Application");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create Tabbed Pane for Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("My Account", myAccountPanel);
        tabbedPane.addTab("Browse Recipes", browsePanel);
        tabbedPane.addTab("Collection", collectionPanel); // Add Collection Panel

        // Add Tabbed Pane to Frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
