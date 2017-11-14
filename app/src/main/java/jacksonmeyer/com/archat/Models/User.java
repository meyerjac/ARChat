package jacksonmeyer.com.archat.Models;

/**
 * Created by jacksonmeyer on 10/9/17.
 */

public class User {

    public String name;
    public String email;
    public String profileImageUrl;

    public User (){ }

    public User(String name, String email, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}