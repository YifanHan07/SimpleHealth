package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String username;
    private final String password;
    private final String repeatPassword;
    private final String preference;
    private final String allergies;

    public SignupInputData(String username, String password, String repeatPassword, String preference,
            String allergies) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.preference = preference;
        this.allergies = allergies;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    String getPreference() {
        return preference;
    }

    String getAllergies() {
        return allergies;
    }
}
