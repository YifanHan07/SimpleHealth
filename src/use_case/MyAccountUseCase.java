package use_case;

import data_access.InMemoryUserDataAccessObject;
import entity.UserAccount;

public class MyAccountUseCase {
    private final UserAccount userAccount;
    private final InMemoryUserDataAccessObject userDataAccessObject;

    public MyAccountUseCase(UserAccount userAccount, InMemoryUserDataAccessObject userDataAccessObject) {
        this.userAccount = userAccount;
        this.userDataAccessObject = userDataAccessObject;
    }

    public String getName() {
        return userAccount.getUsername();
    }

    public void updateName(String name) {
        userAccount.setUsername(name);
        userDataAccessObject.save(userAccount);
    }

    public String getPreferences() {
        return userAccount.getPreferences();
    }

    public void updatePreferences(String preferences) {
        if (!userAccount.getPreferences().equals(preferences)) {
            userAccount.setPreferences(preferences);
        }
        userDataAccessObject.save(userAccount);
    }

    public String getAllergies() {
        return userAccount.getAllergies();
    }

    public void updateAllergies(String allergies) {
        userAccount.setAllergies(allergies);
        userDataAccessObject.save(userAccount);
    }
}
