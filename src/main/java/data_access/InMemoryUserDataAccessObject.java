package data_access;

import entity.CommonUser;
import entity.User;
import entity.UserAccount;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>() {
        {
            put("test", new CommonUser("test", "test", "Low Carb", "Peanuts"));
        }
    };

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    public void save(UserAccount userAccount){
        User user = users.getOrDefault(userAccount.getUsername(), new CommonUser(userAccount.getUsername(),
                "", userAccount.getPreferences(), userAccount.getAllergies()));
        users.put(user.getUsername(), user);

    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }
}
