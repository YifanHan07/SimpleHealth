public class MyAccountController {
    private final MyAccountUseCase userAccount;

    public MyAccountController(MyAccountUseCase userAccount) {
        this.userAccount = userAccount;
    }

    // Getter for Name
    public String getName() {
        return userAccount.getUsername(); // Delegates to UserAccount through MyAccountUseCase
    }

    // Setter for Name
    public void updateName(String name) {
        userAccount.setUsername(name);
    }

    // Getter for Preferences
    public String getPreferences() {
        return userAccount.getPreferences();
    }

    // Setter for Preferences
    public void updatePreferences(String preferences) {
        userAccount.setPreferences(preferences);
    }

    // Getter for Allergies
    public String getAllergies() {
        return userAccount.getAllergies();
    }

    // Setter for Allergies
    public void updateAllergies(String allergies) {
        userAccount.setAllergies(allergies);
    }
}
