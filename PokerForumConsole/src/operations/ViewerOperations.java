package operations;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import pokerforumconsole.MainApplication;
import static pokerforumconsole.MainApplication.sc;
import static pokerforumconsole.PokerForumConsole.allChatMessages;
import entities.ChatMessage;
import entities.DeletedChatMessage;
import database.DatabaseAccess;
import entities.User;

public class ViewerOperations extends CommonOperations {

    public ViewerOperations() {
    }

    @Override
    public void showMainMenu(User loggedInUser) {
        super.showMainMenu(loggedInUser);
        System.out.println("(h) See Any Message from Any User (Including the ones they have deleted)");
    }

    @Override
    public void goToOptionInMainMenu(String option, User loggedInUser) throws SQLException {
        super.goToOptionInMainMenu(option, loggedInUser);
        switch (option) {
            case "h"://see any message from any user
                System.out.println("Select first User");
                User firstUser = this.chooseUser();
                System.out.println("Select second User");
                User secondUser = this.chooseUser();
                String allMessages = this.allChatMessagesBetween(firstUser, secondUser);
                System.out.println(allMessages);
                System.out.println("");
                System.out.println("Press any key to go back and press Enter");
                String anyKey = sc.nextLine();
                MainApplication.mainMenu(loggedInUser);
                break;
            default:// method gets overrided by the classes they extend this class and has more options in the other other classes so we want this default case to be activated only from  users with level 1.
                if (loggedInUser.getLevel() == 1) {
                    System.out.println("Invalid Option");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                }
        }
    }

    //returns all chat messages Including the ones the users have deleted from their personal chat conversations
    public String allChatMessagesBetween(User firstUser, User SecondUser) {
        allChatMessages.clear();
        allChatMessages = DatabaseAccess.getAllChatMessagesBetween(firstUser.getUsername(), SecondUser.getUsername());
        ArrayList<DeletedChatMessage> deletedMessages = DatabaseAccess.getAllDeletedChatMessagesFrom(firstUser.getUsername(), SecondUser.getUsername());
        String allMessages = "Messages between " + firstUser.getUsername() + " and " + SecondUser.getUsername() + "\n" + "\n";
        int i = 1;
        for (ChatMessage msg : allChatMessages) {
            String deletedFrom = "";
            for (DeletedChatMessage deletedMsg : deletedMessages) {
                if (deletedMsg.getMessageID() == msg.getMessageID()) {
                    deletedFrom += "Deleted From: " + deletedMsg.getDeletedFromUser() + "   ";
                }
            }
            String index = "Message " + "(" + i + ")";
            String user = msg.getFromUser();
            String message = msg.getMessage();
            Timestamp date = msg.getDate();
            int edited = msg.getEdited();
            Timestamp dateEdited = msg.getDateEdited();
            String isEdited = "";
            if (edited == 1) {
                isEdited = "(edited) LastEdited: " + dateEdited + "    Last Editor: " + msg.getLastEditor();
            }
            allMessages += index + "     " + date + "     " + isEdited + "    " + deletedFrom + "\n" + user + ": " + message + "\n" + "\n";
            i = i + 1;
        }
        return allMessages;
    }

}
