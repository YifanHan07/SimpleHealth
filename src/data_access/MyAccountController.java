package data_access;

import use_case.MyAccountUseCase;

public class MyAccountController {
    private final MyAccountUseCase useCase;

    public MyAccountController(MyAccountUseCase useCase) {
        this.useCase = useCase;
    }

    public String getName() {
        return useCase.getName();
    }

    public void updateName(String name) {
        useCase.updateName(name);
    }

    public String getPreferences() {
        return useCase.getPreferences();
    }

    public void updatePreferences(String preferences) {
        useCase.updatePreferences(preferences);
    }

    public String getAllergies() {
        return useCase.getAllergies();
    }

    public void updateAllergies(String allergies) {
        useCase.updateAllergies(allergies);
    }

    public void update(String name, String preferences, String allergies) {
        updateName(name);
        updatePreferences(preferences);
        updateAllergies(allergies);
    }
}
