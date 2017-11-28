package jacksonmeyer.com.archat.Models;

/**
 * Created by jacksonmeyer on 11/15/17.
 */

public class ChatMessage {
    public String primMessage;
    public String secMessage;
    public String date;
    public String messageOwnerUid;

    public ChatMessage() { }

    public ChatMessage(String primMessage, String secMessage, String date, String messageOwnerUid) {
        this.primMessage = primMessage;
        this.secMessage = secMessage;
        this.date = date;
        this.messageOwnerUid = messageOwnerUid;
    }

    public String getPrimMessage() {
        return primMessage;
    }

    public String getSecMessage() {
        return secMessage;
    }


    public String getDate() {
        return date;
    }


    public String getMessageOwnerUid() {
        return messageOwnerUid;
    }
}
