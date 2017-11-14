package jacksonmeyer.com.archat.Models;

/**
 * Created by jacksonmeyer on 10/9/17.
 */

public class User {

    public String name;
    public String email;
    public String imageName;

    public User (){ }
    public User(String name, String email, String imageName) {
        this.name = name;
        this.email = email;
        this.imageName = imageName;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}