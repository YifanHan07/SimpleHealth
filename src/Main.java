import entities.UserAccount;
import use_case.MyAccountUseCase;
import data_access.MyAccountController;
import view.BrowsePanel;
import view.MyAccountPanel;

import javax.swing.*;

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

        // Create JFrame
        JFrame frame = new JFrame("Simple Health Application");
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new java.awt.BorderLayout());

        // Create Tabbed Pane for Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("My Account", myAccountPanel);
        tabbedPane.addTab("Browse Recipes", browsePanel);

        // Add Tabbed Pane to Frame
        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
