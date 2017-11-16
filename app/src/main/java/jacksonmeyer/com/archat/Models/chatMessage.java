package jacksonmeyer.com.archat.Models;

import java.sql.Timestamp;

/**
 * Created by jacksonmeyer on 11/15/17.
 */

public class chatMessage {
    public String message;
    public java.sql.Timestamp timestamp;
    public String messageOwnerUid;

    public chatMessage () { }
    public chatMessage(String message, Timestamp timestamp, String messageOwnerUid) {
        this.message = message;
        this.timestamp = timestamp;
        this.messageOwnerUid = messageOwnerUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageOwnerUid() {
        return messageOwnerUid;
    }

    public void setMessageOwnerUid(String messageOwnerUid) {
        this.messageOwnerUid = messageOwnerUid;
    }
}
