package use_case;

import entities.UserAccount;

public class MyAccountUseCase {
    private final UserAccount userAccount;

    public MyAccountUseCase(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getName() {
        return userAccount.getUsername();
    }

    public void updateName(String name) {
        userAccount.setUsername(name);
    }

    public String getPreferences() {
        return userAccount.getPreferences();
    }

    public void updatePreferences(String preferences) {
        userAccount.setPreferences(preferences);
    }

    public String getAllergies() {
        return userAccount.getAllergies();
    }

    public void updateAllergies(String allergies) {
        userAccount.setAllergies(allergies);
    }
}
