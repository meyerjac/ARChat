package jacksonmeyer.com.archat.Models;

/**
 * Created by jacksonmeyer on 11/15/17.
 */

public class chatMessage {
    public String message;
    public String date;
    public String messageOwnerUid;

    public chatMessage () { }
    public chatMessage(String message, String date, String messageOwnerUid) {
        this.message = message;
        this.date = date;
        this.messageOwnerUid = messageOwnerUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessageOwnerUid() {
        return messageOwnerUid;
    }

    public void setMessageOwnerUid(String messageOwnerUid) {
        this.messageOwnerUid = messageOwnerUid;
    }
}
