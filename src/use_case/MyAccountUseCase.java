package use_case;

import entities.UserAccount;

public class MyAccountUseCase {
    private final UserAccount userAccount;

    public MyAccountUseCase(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    // Getter for Username
    public String getUsername() {
        return userAccount.getUsername();
    }

    // Setter for Username
    public void setUsername(String username) {
        userAccount.setUsername(username);
    }

    // Getter for Preferences
    public String getPreferences() {
        return userAccount.getPreferences();
    }

    // Setter for Preferences
    public void setPreferences(String preferences) {
        userAccount.setPreferences(preferences);
    }

    // Getter for Allergies
    public String getAllergies() {
        return userAccount.getAllergies();
    }

    // Setter for Allergies
    public void setAllergies(String allergies) {
        userAccount.setAllergies(allergies);
    }
}
