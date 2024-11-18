package use_case.logout;

/**
 * The Input Data for the Logout Use Case.
 */
public class LogoutInputData {

    private String username = "null";

    public LogoutInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
