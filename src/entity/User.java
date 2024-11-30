package entity;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getUsername();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Returns the preference of the user.
     *
     * @return the preference of the user.
     */
    String getPreference();

    /**
     * Returns the allergies of the user.
     * @return the allergies of the user.
     */
    String getAllergies();

}
