package entites;

public class UserAccount {
    private String username;
    private String preferences;
    private String allergies;

    // Constructor
    public UserAccount(String username, String preferences, String allergies) {
        this.username = username;
        this.preferences = preferences;
        this.allergies = allergies;
    }

    // Getter for Username
    public String getUsername() {
        return username;
    }

    // Setter for Username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for Preferences
    public String getPreferences() {
        return preferences;
    }

    // Setter for Preferences
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    // Getter for Allergies
    public String getAllergies() {
        return allergies;
    }

    // Setter for Allergies
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
