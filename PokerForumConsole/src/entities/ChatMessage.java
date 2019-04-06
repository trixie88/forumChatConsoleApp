package entities;

import java.sql.Timestamp;

public class ChatMessage {
    private int messageID;
    private String fromUser;
    private String toUser;
    private String message;
    private Timestamp date;
    private int edited;
    private Timestamp dateEdited;
    private String lastEditor;
    private int seen;

    public ChatMessage() {
    }

    public ChatMessage(int messageID, String fromUser, String toUser, String message, Timestamp date, int edited, Timestamp dateEdited,String lastEditor, int seen) {
        this.messageID=messageID;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
        this.date = date;
        this.edited = edited;
        this.dateEdited = dateEdited;
        this.lastEditor=lastEditor;
        this.seen=seen;
    }

    public ChatMessage(String fromUser, String toUser, String message, Timestamp date) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
        this.date = date;
        this.edited=0;
        this.dateEdited=null;
        this.seen=0;
    }

    public ChatMessage(String fromUser, String toUser, String message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
        this.date=new Timestamp(System.currentTimeMillis());
        this.edited=0;
        this.dateEdited=null;
        this.seen=0;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    
    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    
    
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getEdited() {
        return edited;
    }

    public void setEdited(int edited) {
        this.edited = edited;
    }

    public Timestamp getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Timestamp dateEdited) {
        this.dateEdited = dateEdited;
    }

    @Override
    public String toString() {
        return "\nChatMessage{" + "messageID=" + messageID + ", fromUser=" + fromUser + ", toUser=" + toUser + ", message=" + message + ", date=" + date + ", edited=" + edited + ", dateEdited=" + dateEdited + ", seen=" + seen + '}';
    }

    
    
    
    

    
   
    
    
}
