package interface_adapter.signup;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Signup View.
 */
public class SignupViewModel extends ViewModel<SignupState> {

    public static final String TITLE_LABEL = "Sign up";
    public static final String USERNAME_LABEL = "Account";
    public static final String PASSWORD_LABEL = "Password";
    public static final String REPEAT_PASSWORD_LABEL = "Repeat password";
    public static final String PREFERENCE_LABEL = "Preference";
    public static final String ALLERGIES_LABEL = "Allergies";

    public static final String SIGNUP_BUTTON_LABEL = "Sign up";

    public static final String TO_LOGIN_BUTTON_LABEL = "Login";

    public SignupViewModel() {
        super("Sign up");
        setState(new SignupState());
    }

}
