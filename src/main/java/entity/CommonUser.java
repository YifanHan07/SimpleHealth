package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String username;
    private final String password;
    private final String preference;
    private final String allergies;

    public CommonUser(String username, String password, String preference, String allergies) {
        this.username = username;
        this.password = password;
        this.preference = preference;
        this.allergies = allergies;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getPreference() {
        return preference;
    }

    public String getAllergies() {
        return allergies;
    }
}
