package database;

import entities.ChatMessage;
import entities.DeletedChatMessage;
import entities.Thread;
import entities.ThreadPost;
import entities.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import static pokerforumconsole.PokerForumConsole.allUsers;

public class DatabaseAccess {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Properties connectionProps = new Properties();
            connectionProps.put("user", "trixakias");
            connectionProps.put("password", "theo2512");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/forum?zeroDateTimeBehavior=convertToNull&characterEncoding=utf-8&autoReconnect=true", connectionProps);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static ArrayList<Thread> getLockedThreads() {
        ArrayList<Thread> lockedThreads = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps=conn.prepareStatement("SELECT * FROM forum.forumthreads where locked=1");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int threadID = resultSet.getInt("threadID");
                String title = resultSet.getString("title");
                int locked = resultSet.getInt("locked");
                int posts = resultSet.getInt("posts");
                Thread newThread = new Thread(threadID, title, locked, posts);
                lockedThreads.add(newThread);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lockedThreads;
    }

    public static User createUser(String username, String password, String name, String surname, String email) { 
        User newUser=new User(username, password, name, surname, email);
        try {
            Connection conn = DatabaseAccess.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("insert into username (username,password,name,surname,email) values (?,?,?,?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, surname);
            ps.setString(5, email);
            ps.executeUpdate();
            allUsers.clear();
            allUsers=DatabaseAccess.getAllUsers();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return newUser;
    }

    public static void setMessagesSeen(String fromUser, String toUser) {
        try {
            Connection conn = DatabaseAccess.getConnection();
            PreparedStatement ps;
            ps = conn.prepareStatement("update messages set seen=1 where fromUser=? and toUser=?");
            ps.setString(1, fromUser);
            ps.setString(2, toUser);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<ChatMessage> getUnreadMessages(String Username) {
        ArrayList<ChatMessage> unreadMessages = new ArrayList<>();
        try {
            Connection conn = DatabaseAccess.getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from messages where toUser=? and seen=0 group by fromUser;");
            ps.setString(1, Username);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String fromUser = resultSet.getString("fromUser");
                String toUser = resultSet.getString("toUser");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int edited = resultSet.getInt("edited");
                Timestamp dateEdited = resultSet.getTimestamp("dateEdited");
                String lastEditor = resultSet.getString("lastEditor");
                int seen = resultSet.getInt("seen");
                ChatMessage newChatMessage = new ChatMessage(messageID, fromUser, toUser, message, date, edited, dateEdited, lastEditor, seen);
                unreadMessages.add(newChatMessage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unreadMessages;
    }

    //the messages the user sees in his personal chat 
    public static ArrayList<ChatMessage> getAllPersonalChatMessages(String selectedUsername, String loggedInUsername) {

        ArrayList<ChatMessage> personalChatMessages = new ArrayList<>();
        try {
            Connection conn = DatabaseAccess.getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from messages \n"
                    + "where messages.messageID not in (select deletedmessages.messageID from deletedmessages where deletedmessages.deletedFromUser=?)\n"
                    + "and  (messages.fromUser=? and messages.toUser=? or messages.fromUser=? and messages.toUser=?) order by messages.messageID;");
            ps.setString(1, loggedInUsername);
            ps.setString(2, loggedInUsername);
            ps.setString(3, selectedUsername);
            ps.setString(4, selectedUsername);
            ps.setString(5, loggedInUsername);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String fromUser = resultSet.getString("fromUser");
                String toUser = resultSet.getString("toUser");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int edited = resultSet.getInt("edited");
                Timestamp dateEdited = resultSet.getTimestamp("dateEdited");
                String lastEditor = resultSet.getString("lastEditor");
                int seen = resultSet.getInt("seen");
                ChatMessage newMessage = new ChatMessage(messageID, fromUser, toUser, message, date, edited, dateEdited, lastEditor, seen);
                personalChatMessages.add(newMessage);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personalChatMessages;
    }

    public static ArrayList<User> getAllBannedUsers() {
        ArrayList<User> allBannedUsers = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from forum.username where banned=1;");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                int level = resultSet.getInt("level");
                int banned = resultSet.getInt("banned");
                User newUser = new User(username, password, name, surname, email, level, banned);
                allBannedUsers.add(newUser);

            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allBannedUsers;

    }

    public static void update(String sqlQuerry, ArrayList data) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps;
            ps = conn.prepareStatement(sqlQuerry);
            for (int i = 1; i <= data.size(); i++) {
                if (data.get(i - 1) instanceof String) {
                    String stringData = (String) data.get(i - 1);
                    ps.setString(i, stringData);
                } else if (data.get(i - 1) instanceof Integer) {
                    int integerData = (int) data.get(i - 1);
                    ps.setInt(i, integerData);
                } else if (data.get(i - 1) instanceof Timestamp) {
                    Timestamp timestampData = (Timestamp) data.get(i - 1);
                    ps.setTimestamp(i, timestampData);
                }
            }
            ps.executeUpdate();
            data.clear();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from forum.username");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                int level = resultSet.getInt("level");
                int banned = resultSet.getInt("banned");
                User newUser = new User(username, password, name, surname, email, level, banned);
                allUsers.add(newUser);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allUsers;
    }

    //returns all the chat messages between two users including the messages they have deleted from their personal chats
    public static ArrayList<ChatMessage> getAllChatMessagesBetween(String sender, String receiver) {
        ArrayList<ChatMessage> allChatMessages = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from forum.messages where fromUser=? and toUser=? or fromUser=? and toUser=? order by messageID");
            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, receiver);
            ps.setString(4, sender);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String fromUser = resultSet.getString("fromUser");
                String toUser = resultSet.getString("toUser");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int edited = resultSet.getInt("edited");
                Timestamp dateEdited = resultSet.getTimestamp("dateEdited");
                String lastEditor = resultSet.getString("lastEditor");
                int seen = resultSet.getInt("seen");
                ChatMessage newMessage = new ChatMessage(messageID, fromUser, toUser, message, date, edited, dateEdited, lastEditor, seen);
                allChatMessages.add(newMessage);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allChatMessages;
    }

    public static ArrayList<ChatMessage> getAllChatMessages() {
        ArrayList<ChatMessage> allChatMessages = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from forum.messages");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String fromUser = resultSet.getString("fromUser");
                String toUser = resultSet.getString("toUser");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int edited = resultSet.getInt("edited");
                Timestamp dateEdited = resultSet.getTimestamp("dateEdited");
                String lastEditor = resultSet.getString("lastEditor");
                int seen = resultSet.getInt("seen");
                ChatMessage newMessage = new ChatMessage(messageID, fromUser, toUser, message, date, edited, dateEdited, lastEditor, seen);
                allChatMessages.add(newMessage);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allChatMessages;
    }

    public static ArrayList<DeletedChatMessage> getAllDeletedChatMessages() {
        ArrayList<DeletedChatMessage> allDeletedChatMessages = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from forum.deletedmessages");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String deletedFromUser = resultSet.getString("deletedFromUser");
                DeletedChatMessage newDeletedChatMessage = new DeletedChatMessage(messageID, deletedFromUser);
                allDeletedChatMessages.add(newDeletedChatMessage);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allDeletedChatMessages;
    }

    public static ArrayList<DeletedChatMessage> getAllPersonalDeletedChatMessages(String firstUsername) {
        ArrayList<DeletedChatMessage> allDeletedChatMessages = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from deletedmessages where deletedFromUser=?");
            ps.setString(1, firstUsername);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String deletedFromUser = resultSet.getString("deletedFromUser");
                DeletedChatMessage newDeletedChatMessage = new DeletedChatMessage(messageID, deletedFromUser);
                allDeletedChatMessages.add(newDeletedChatMessage);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allDeletedChatMessages;
    }

    public static ArrayList<DeletedChatMessage> getAllDeletedChatMessagesFrom(String firstUsername, String secondUsername) {
        ArrayList<DeletedChatMessage> allDeletedChatMessages = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from deletedmessages where deletedFromUser=? or deletedmessages.deletedFromUser=?");
            ps.setString(1, firstUsername);
            ps.setString(2, secondUsername);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int messageID = resultSet.getInt("messageID");
                String deletedFromUser = resultSet.getString("deletedFromUser");
                DeletedChatMessage newDeletedChatMessage = new DeletedChatMessage(messageID, deletedFromUser);
                allDeletedChatMessages.add(newDeletedChatMessage);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allDeletedChatMessages;
    }

    public static ArrayList<ThreadPost> getAllPostsFromThread(int threadID) {
        ArrayList<ThreadPost> allPosts = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("select * from forum.thread_messages where threadID=?");
            ps.setInt(1, threadID);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int thread_message_id = resultSet.getInt("thred_message_id");
                String username = resultSet.getString("username");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("date");
                int idofThread = resultSet.getInt("threadID");
                int edited = resultSet.getInt("edited");
                Timestamp dateEdited = resultSet.getTimestamp("dateEdited");
                ThreadPost newPost = new ThreadPost(thread_message_id, username, message, date, idofThread, edited, dateEdited);
                allPosts.add(newPost);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allPosts;

    }

    public static ArrayList<Thread> getAllThreads() {
        ArrayList<Thread> allThreads = new ArrayList<>();
        try {
            Connection conn = getConnection();
            ResultSet resultSet = null;
            PreparedStatement ps;
            ps = conn.prepareStatement("SELECT * FROM forum.forumthreads;");
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int threadID = resultSet.getInt("threadID");
                String title = resultSet.getString("title");
                int locked = resultSet.getInt("locked");
                int posts = resultSet.getInt("posts");
                Thread newThread = new Thread(threadID, title, locked, posts);
                allThreads.add(newThread);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allThreads;
    }

}
