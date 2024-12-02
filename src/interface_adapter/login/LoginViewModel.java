package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public LoginViewModel() {
        super("Log in");
        setState(new LoginState());
    }

    public LoginState getLoginState() {
        return this.getState();
    }

    /**
     * Sets the current login state for the login view.
     * This method updates the LoginState to the provided state and triggers
     * a property change event to notify the view that the state has been updated.
     *
     * @param loginState The new LoginState representing the current state of the login view.
     */
    public void setLoginState(LoginState loginState) {
        this.setState(loginState);
        this.firePropertyChanged();
    }
}
