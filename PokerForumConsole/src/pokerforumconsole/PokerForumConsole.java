package pokerforumconsole;


import database.DatabaseAccess;
import entities.ChatMessage;
import entities.DeletedChatMessage;
import entities.Thread;
import entities.ThreadPost;
import entities.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PokerForumConsole {
    
    public static ArrayList<User> bannedUsers = DatabaseAccess.getAllBannedUsers();
    public static ArrayList<User> allUsers=DatabaseAccess.getAllUsers();
    public static ArrayList<ChatMessage> allChatMessages=new ArrayList<>();
    public static ArrayList<ChatMessage> personalChatMessages=new ArrayList<>();
    public static ArrayList<DeletedChatMessage> deletedChatMessages=DatabaseAccess.getAllDeletedChatMessages();
    public static ArrayList <Thread> allThreads=DatabaseAccess.getAllThreads();
    public static ArrayList<ThreadPost> threadPosts=new ArrayList<>();
    public static ArrayList data=new ArrayList();
    public static ArrayList<ChatMessage> unreadMessages=new ArrayList<>();
    public static ArrayList<Thread> lockedThreads=DatabaseAccess.getLockedThreads();
    
    public static void main(String[] args) {
        
        FileAccess.createThreadMessagesFile();
        FileAccess.createUserMessagesFile();
        try {
            MainApplication.startingMenu();
        } catch (SQLException ex) {
            Logger.getLogger(PokerForumConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
}
