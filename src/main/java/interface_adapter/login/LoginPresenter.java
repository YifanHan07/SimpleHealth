package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.loggedIn.LoggedInState;
import interface_adapter.loggedIn.LoggedInViewModel;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel, LoggedInViewModel loggedInViewModel,
                          SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.

        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        loggedInState.setPreference(response.getPreference());
        loggedInState.setAllergies(response.getAllergies());
        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged();

        this.viewManagerModel.setState(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {
        SignupState state = signupViewModel.getState();
        state.setUsername("");
        state.setPassword("");
        state.setUsernameError("");
        state.setPasswordError("");
        this.viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
