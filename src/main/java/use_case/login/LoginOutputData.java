package use_case.login;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final String allergies;
    private final String preference;
    private final boolean useCaseFailed;

    public LoginOutputData(String username, String preference, String allergies, boolean useCaseFailed) {
        this.username = username;
        this.allergies = allergies;
        this.preference = preference;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }

    public String getPreference() {
        return preference;
    }

    public String getAllergies() {
        return allergies;
    }
}
