package entities;

public class DeletedChatMessage {
    private  int messageID;
    private String deletedFromUser;

    public DeletedChatMessage(int messageID, String deletedFromUser) {
        this.messageID = messageID;
        this.deletedFromUser = deletedFromUser;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getDeletedFromUser() {
        return deletedFromUser;
    }

    public void setDeletedFromUser(String deletedFromUser) {
        this.deletedFromUser = deletedFromUser;
    }

    @Override
    public String toString() {
        return "\nDeletedChatMessage{" + "messageID=" + messageID + ", deletedFromUser=" + deletedFromUser + '}';
    }
    
    
}
