package jacksonmeyer.com.archat.Models;

import java.util.Map;

/**
 * Created by jacksonmeyer on 10/9/17.
 */

public class User {
    public String name;
    public String email;
    public String imageName;
    public String userUid;
    public String learningLanguageISO;
    public Map<String, ChatMessage> messages;

    public User() {
    }

    public User(String name, String email, String imageName, String userUid, String learningLanguageISO, Map<String, ChatMessage> messages) {
        this.name = name;
        this.email = email;
        this.imageName = imageName;
        this.userUid = userUid;
        this.learningLanguageISO = learningLanguageISO;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getLearningLanguageISO() {
        return learningLanguageISO;
    }

    public void setLearningLanguageISO(String learningLanguageISO) {
        this.learningLanguageISO = learningLanguageISO;
    }

    public Map<String, ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, ChatMessage> messages) {
        this.messages = messages;
    }
}