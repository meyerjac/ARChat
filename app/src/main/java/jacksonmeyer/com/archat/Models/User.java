package jacksonmeyer.com.archat.Models;

/**
 * Created by jacksonmeyer on 10/9/17.
 */

public class User {

    public String name;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.name = username;
        this.email = email;
    }

}