package interface_adapter.loggedIn;

import interface_adapter.ViewModel;

/**
 * The View Model for the Logged In View.
 */
public class LoggedInViewModel extends ViewModel<LoggedInState> {

    public LoggedInViewModel() {
        super("Logged in");
        setState(new LoggedInState());
    }

    public LoggedInState getLoggedInState() {
        return this.getState();
    }

    /**
     * Sets the current state of the logged-in user.
     * This method updates the current LoggedInState to the provided state and triggers
     * a property change event to notify the view that the state has been updated.
     *
     * @param loggedInState The new state representing the logged-in user.
     */
    public void setLoggedInState(LoggedInState loggedInState) {
        this.setState(loggedInState);
        this.firePropertyChanged();
    }
}
