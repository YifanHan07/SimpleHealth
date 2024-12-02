package view;

import data_access.InMemoryUserDataAccessObject;
import data_access.MyAccountController;
import entity.UserAccount;
import interface_adapter.loggedIn.LoggedInState;
import interface_adapter.loggedIn.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import use_case.MyAccountUseCase;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JTabbedPane implements PropertyChangeListener {

    private final String viewName = "Logged in";
    private final LoggedInViewModel loggedInViewModel;
    private LogoutController logoutController;
    private MyAccountController myAccountController;
    private MyAccountPanel myAccountPanel;
    private BrowsePanel browsePanel;
    private CollectionPanel collectionPanel;
    private InMemoryUserDataAccessObject userDataAccessObject;

    private final JLabel username;

    private final JButton logOut;

    public LoggedInView(LoggedInViewModel loggedInViewModel, InMemoryUserDataAccessObject userDataAccessObject) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.userDataAccessObject = userDataAccessObject;

        final JLabel title = new JLabel("Logged In Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        username = new JLabel();

        logOut = new JButton("Log Out");
        // Create User Account
        LoggedInState loggedInState = loggedInViewModel.getLoggedInState();
        UserAccount userAccount = new UserAccount(loggedInState.getUsername(), loggedInState.getPreference(),
                loggedInState.getAllergies());

        // Create Use Case and Controller
        MyAccountUseCase useCase = new MyAccountUseCase(userAccount, userDataAccessObject);
        myAccountController = new MyAccountController(useCase);

        // Create Panels
        myAccountPanel = new MyAccountPanel(myAccountController);
        browsePanel = new BrowsePanel();
        collectionPanel = new CollectionPanel();

        // Integrate Collection Panel with Browse Panel
        browsePanel.setSaveRecipeHandler(recipe -> {
            collectionPanel.addRecipe(recipe); // Save recipe to collection
            JOptionPane.showMessageDialog(null, "Recipe saved to Collection!");
        });


        // Create Tabbed Pane for Navigation

        this.addTab("My Account", myAccountPanel);
        this.addTab("Browse Recipes", browsePanel);
        this.addTab("Collection", collectionPanel); // Add Collection Panel

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
            myAccountController.update(state.getUsername(), state.getPreference(), state.getAllergies());
            myAccountPanel.updateFields(myAccountController);
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
        myAccountPanel.setLogoutController(logoutController);
    }
}
