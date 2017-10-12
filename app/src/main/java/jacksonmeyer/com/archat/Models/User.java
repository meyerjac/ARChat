package jacksonmeyer.com.archat.Models;

import android.net.Uri;

/**
 * Created by jacksonmeyer on 10/9/17.
 */

public class User {

    public String name;
    public String email;
    public Uri profileImageUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, Uri profileImageUrl) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;

    }

}