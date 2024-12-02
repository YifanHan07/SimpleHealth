package view;

import data_access.InMemoryUserDataAccessObject;
import data_access.MyAccountController;
import entity.UserAccount;
import entity.Recipe;
import interface_adapter.loggedIn.LoggedInState;
import interface_adapter.loggedIn.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.mealplaner.MealPlannerController;
import interface_adapter.mealplaner.SaveRecipeController;
import use_case.MyAccountUseCase;
import use_case.mealplaner.MealPlannerInteractor;
import use_case.mealplaner.SaveRecipeInteractor;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoggedInView extends JTabbedPane implements PropertyChangeListener {

    private final String viewName = "Logged in";
    private final LoggedInViewModel loggedInViewModel;
    private LogoutController logoutController;
    private MyAccountController myAccountController;
    private MyAccountPanel myAccountPanel;
    private BrowsePanel browsePanel;
    private CollectionPanel collectionPanel;
    private Recipe recipe;

    public LoggedInView(LoggedInViewModel loggedInViewModel, InMemoryUserDataAccessObject userDataAccessObject) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // Create User Account
        LoggedInState loggedInState = loggedInViewModel.getLoggedInState();
        UserAccount userAccount = new UserAccount(
                loggedInState.getUsername(),
                loggedInState.getPreference(),
                loggedInState.getAllergies()
        );

        // Initialize Use Cases and Controllers
        SaveRecipeInteractor saveRecipeInteractor = new SaveRecipeInteractor();
        MealPlannerInteractor mealPlannerInteractor = new MealPlannerInteractor();

        SaveRecipeController saveRecipeController = new SaveRecipeController(saveRecipeInteractor);
        MealPlannerController mealPlannerController = new MealPlannerController(mealPlannerInteractor);

        MyAccountUseCase myAccountUseCase = new MyAccountUseCase(userAccount, userDataAccessObject);
        myAccountController = new MyAccountController(myAccountUseCase);

        // Create Panels
        myAccountPanel = new MyAccountPanel(myAccountController);
        browsePanel = new BrowsePanel(saveRecipeController, mealPlannerController);
        collectionPanel = new CollectionPanel(saveRecipeController, mealPlannerController);

        // Integrate Panels: Save logic and refresh collection panel
        browsePanel.setSaveRecipeHandler(recipe -> {
            saveRecipeController.saveRecipe(recipe);  // Save recipe to collection
            collectionPanel.refresh();                // Refresh collection panel
            JOptionPane.showMessageDialog(null, recipe.getLabel() + " saved to Collection!");
        });

        // Add Tabs
        this.addTab("My Account", myAccountPanel);
        this.addTab("Browse Recipes", browsePanel);
        this.addTab("Collection", collectionPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            LoggedInState state = (LoggedInState) evt.getNewValue();
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
