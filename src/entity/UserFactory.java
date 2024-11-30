package entity;

/**
 * Factory for creating users.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param password the password of the new user
     * @param preference the dietary preference of the new user
     * @param allergies the allergies of the new user
     * @return the new user
     */
    User create(String name, String password, String preference, String allergies);

}
