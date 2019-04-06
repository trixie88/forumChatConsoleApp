package operations;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import pokerforumconsole.FileAccess;
import pokerforumconsole.MainApplication;
import static pokerforumconsole.MainApplication.sc;
import static pokerforumconsole.PokerForumConsole.allChatMessages;
import static pokerforumconsole.PokerForumConsole.allThreads;
import static pokerforumconsole.PokerForumConsole.allUsers;
import static pokerforumconsole.PokerForumConsole.bannedUsers;
import static pokerforumconsole.PokerForumConsole.data;
import static pokerforumconsole.PokerForumConsole.personalChatMessages;
import static pokerforumconsole.PokerForumConsole.threadPosts;
import static pokerforumconsole.PokerForumConsole.unreadMessages;
import entities.ChatMessage;
import database.DatabaseAccess;
import entities.ThreadPost;
import entities.User;
import entities.Thread;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommonOperations {

    public static java.sql.Connection conn = null;
    public static ResultSet resultSet = null;
    public static PreparedStatement ps;
    public static boolean youAreTheBlocker = false;
    public static boolean youAreBlocked = false;
    public static boolean blockedChat = false;
    public static String blocker;
    public static String usernameToGetBlocked;

    public CommonOperations() {
    }

    public void showMainMenu(User loggedInUSER) {
        System.out.println("");
        System.out.println("Main Menu");
        System.out.println("Logged in as " + loggedInUSER.getUsername());
        System.out.print("(a) New Messages: " +unreadMessages.size() + "\n(b) Chat (Send Message, Block-Unblock other users, Delete your Messages) \n(c) Choose Thread\n(d) Create Thread\n(e) LeaderBoard\n(f) Edit your Account \n(g) Change Password\n");
    }

    public void goToOptionInMainMenu(String option, User loggedInUser) throws SQLException {
        switch (option) {
            case "a"://new messages
                User newMessagesUser = this.chooseNewMessagesUser();
                if (newMessagesUser == null) {
                    System.out.println("You have no New Messages");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                } else {
                    String newMessages = this.personalChat(newMessagesUser, loggedInUser);
                    System.out.println(newMessages);
                    this.chatMenuOptions(newMessagesUser, loggedInUser);
                    break;
                }
            case "b"://Chat(Block-Unblock other users, Delete your Messages)
                User selectedUser = this.chooseUser();
                String allMessages = this.personalChat(selectedUser, loggedInUser);
                System.out.println(allMessages);
                this.chatMenuOptions(selectedUser, loggedInUser);
                break;
            case "c":// choose thread
                Thread selectedThread = this.chooseThread();
                if (selectedThread == null) {
                    System.out.println("There are no Threads");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                } else {
                    String threadMessages = this.threadPostsFromThread(selectedThread);
                    System.out.println(threadMessages);
                    this.showThreadOptionsMenu();
                    String answer = this.askUser();
                    this.goToThreadsMenuOption(answer, selectedThread, loggedInUser);
                    break;
                }
            case "d"://create thread
                System.out.print("Enter Thread title:");
                String title = sc.nextLine();
                this.createNewThread(title, loggedInUser);
                Thread newThread = this.getThreadFromTitle(title);
                System.out.println(this.threadPostsFromThread(newThread));
                this.showThreadOptionsMenu();
                String answer = this.askUser();
                this.goToThreadsMenuOption(answer, newThread, loggedInUser);
                break;
            case "e"://see leaderboard
                this.seeLeaderBoard(loggedInUser);
                break;
            case "f"://edit account
                this.editPersonalAccountOptions(loggedInUser, loggedInUser);
                break;
            case "g"://change password
                this.changePassword(loggedInUser);
                break;
            default:// method gets overrided by the classes they extend this class and has more options in the other other classes so we want this default case to be activated only from  users with level 0. 
                if (loggedInUser.getLevel() == 0) {
                    System.out.println("Invalid Option");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                }
        }

    }

    public String personalChat(User selectedUser, User loggedInUser) throws SQLException {
        personalChatMessages.clear();
        personalChatMessages = DatabaseAccess.getAllPersonalChatMessages(selectedUser.getUsername(), loggedInUser.getUsername());
        String allMessages = "Messages between " + selectedUser.getUsername() + " and " + loggedInUser.getUsername() + "\n" + "\n";
        int i = 1;
        for (ChatMessage msg : personalChatMessages) {
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
            allMessages += index + "     " + date + "     " + isEdited + "\n" + user + ": " + message + "\n" + "\n";
            i = i + 1;
        }
        return allMessages;
    }

    public User getUserFromUsername(String username) {
        User userTobeReturned = new User();
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                userTobeReturned = user;
                break;
            }
        }
        return userTobeReturned;
    }

    
    public User chooseNewMessagesUser() {
        if (unreadMessages.isEmpty()) {
            return null;
        } else {
            int i = 1;
            System.out.println("You have new Messages From:");
            for (ChatMessage chatMessage : unreadMessages) {
                System.out.println(i + " - " + chatMessage.getFromUser());
                i++;
            }
            System.out.print("Your option: ");
            String answer = sc.nextLine();
            try {
                int answerToString = Integer.parseInt(answer);
                if (answerToString <= 0 || answerToString > i) {
                    System.out.println("Please select a valid index");
                    return chooseNewMessagesUser();
                } else {
                    String selectedUsername = unreadMessages.get(answerToString - 1).getFromUser();
                    User seletectedUser = getUserFromUsername(selectedUsername);
                    return seletectedUser;
                }
            } catch (Exception e) {
                System.out.println("Please select a valid index");
                return this.chooseNewMessagesUser();
            }
        }
    }

    public User chooseUser() throws SQLException {
        int i = 1;
        System.out.println("Select index from usernames below:");
        for (User user : allUsers) {
            System.out.println(i + " - " + user.getUsername());
            i++;
        }
        System.out.print("Your option: ");
        String answer = sc.nextLine();
        try {
            int answerToString = Integer.parseInt(answer);
            if (answerToString <= 0 || answerToString > i) {
                System.out.println("Invalid Option");
                return chooseUser();
            } else {
                return allUsers.get(answerToString - 1);
            }
        } catch (Exception e) {
            System.out.println("Invalid Option");
            return this.chooseUser();
        }
    }

    public void chatMenuOptions(User selectedUser, User loggedInUser) throws SQLException {
        //sets that these messages between these 2 users are seen from loggedInUser and are no Longer New Messages for him
        DatabaseAccess.setMessagesSeen(selectedUser.getUsername(), loggedInUser.getUsername());
        usernameToGetBlocked = selectedUser.getUsername();
        blocker = loggedInUser.getUsername();
        System.out.println("a - Main Menu\nb - Send Message\nc - Block " + selectedUser.getUsername() + "\nd - Unblock " + selectedUser.getUsername() + "\ne - Delete Message");
        String option = this.askUser();
        if (option.equals("a")) {
            MainApplication.mainMenu(loggedInUser);
        } else if (option.equals("b")) {
            this.checkIfBlocked(selectedUser, loggedInUser);
            this.chat(selectedUser, loggedInUser);
            chatMenuOptions(selectedUser, loggedInUser);
        } else if (option.equals("c")) {
            this.checkIfBlocked(selectedUser, loggedInUser);
            this.blockUser(selectedUser, loggedInUser);
            chatMenuOptions(selectedUser, loggedInUser);
        } else if (option.equals("d")) {
            this.checkIfBlocked(selectedUser, loggedInUser);
            this.unblockUser(selectedUser, loggedInUser);
            chatMenuOptions(selectedUser, loggedInUser);
        } else if (option.equals("e")) {
            ChatMessage selectedChatMessage = getChatMessage(selectedUser, loggedInUser);
            if (selectedChatMessage == null) {
                System.out.println("There are no Messages to Delete");
                this.chatMenuOptions(selectedUser, loggedInUser);
            } else {
                this.moveMessageToDeleted(selectedChatMessage, loggedInUser);
                String newMessages = this.personalChat(selectedUser, loggedInUser);
                System.out.println(newMessages);
                this.chatMenuOptions(selectedUser, loggedInUser);
            }
        } else {
            System.out.println("Invalid Option");
            chatMenuOptions(selectedUser, loggedInUser);
        }
    }

    public ChatMessage getChatMessage(User selectedUser, User loggedInUser) throws SQLException {
        personalChatMessages.clear();
        personalChatMessages = DatabaseAccess.getAllPersonalChatMessages(selectedUser.getUsername(), loggedInUser.getUsername());
        if (personalChatMessages.isEmpty()) {
            return null;
        } else {
            String allMessages = this.personalChat(selectedUser, loggedInUser);
            System.out.println(allMessages);
            System.out.println("Select index of Message to delete from 1 to " + personalChatMessages.size());
            String stringIndex = sc.nextLine();
            try {
                int index = Integer.parseInt(stringIndex);
                if (index < 1 || index > personalChatMessages.size()) {
                    System.out.println("Invalid option ");
                    return this.getChatMessage(selectedUser, loggedInUser);
                } else {
                    return personalChatMessages.get(index - 1);
                }
            } catch (Exception e) {
                System.out.println("Invalid Option");
                return this.getChatMessage(selectedUser, loggedInUser);
            }
        }
    }

    public void moveMessageToDeleted(ChatMessage selectedMessage, User loggedInUser) {
        int messageID = selectedMessage.getMessageID();
        String sqlQuerry = "insert into deletedmessages (messageID, deletedFromUser) values( ? , ? ) ";
        data.addAll(Arrays.asList(messageID, loggedInUser.getUsername()));
        DatabaseAccess.update(sqlQuerry, data);
    }

    public String askUser() {
        System.out.print("Your option: ");
        String option = sc.nextLine();
        return option;
    }

    //gets overrided from userViewerAndEditor and from UserViewerEditorDeleter and has more options afterwards
    public void showThreadOptionsMenu() {
        System.out.println("a - Post to thread\nb - Main Menu\nc - Quote");
    }

    public void goToThreadsMenuOption(String option, Thread selectedThread, User loggedInUser) throws SQLException {
        switch (option) {
            case "a": //post to thread Option
                if (selectedThread.getLocked() == 1) {
                    System.out.println("THREAD LOCKED BY ADMIN. YOU CAN'T POST");
                } else {
                    System.out.print("Your post: ");
                    String post = sc.nextLine();
                    this.postToThread(selectedThread, post, loggedInUser);
                    System.out.println(this.threadPostsFromThread(selectedThread));
                }
                this.showThreadOptionsMenu();
                String answer = this.askUser();
                this.goToThreadsMenuOption(answer, selectedThread, loggedInUser);
                break;
            case "b": // main menu Option
                MainApplication.mainMenu(loggedInUser);
                break;
            case "c"://Quote to post Option
                if (selectedThread.getLocked() == 1) {
                    System.out.println("THREAD LOCKED BY ADMIN. YOU CAN'T QUOTE");
                } else {
                    ThreadPost threadPost = this.chooseThreadPostFromThread(selectedThread);
                    if (threadPost == null) {
                        System.out.println("There are No Forum Posts to Quote");
                    } else {
                        this.quote(threadPost, selectedThread, loggedInUser);
                        System.out.println(this.threadPostsFromThread(selectedThread));
                    }
                }
                this.showThreadOptionsMenu();
                String answerr = this.askUser();
                this.goToThreadsMenuOption(answerr, selectedThread, loggedInUser);
                break;
            default: //because the method gets overrided from other users we want this default case to be activated only for the users with level<=1 (the other users have more options)
                if (loggedInUser.getLevel() <= 1) {
                    System.out.println("Invalid Option");
                    this.showThreadOptionsMenu();
                    String typedOption = this.askUser();
                    this.goToThreadsMenuOption(typedOption, selectedThread, loggedInUser);
                    break;
                }
        }
    }

    public void quote(ThreadPost selectedThreadPost, Thread selectedThread, User loggedInUser) {
        System.out.println("Quote Forum Post: [" + selectedThreadPost.getUsername() + ": " + selectedThreadPost.getMessage() + "]");
        System.out.print("Quote: ");
        String quote = sc.nextLine();
        String postToThread = "\n      [" + selectedThreadPost.getUsername() + ": " + selectedThreadPost.getMessage() + "]\n  " + quote;
        postToThread(selectedThread, postToThread, loggedInUser);
    }

    public ThreadPost chooseThreadPostFromThread(Thread selectedThread) {
        threadPosts.clear();
        threadPosts = DatabaseAccess.getAllPostsFromThread(selectedThread.getThreadID());
        if (threadPosts.isEmpty()) {
            return null;
        } else {
            String threadMessages = this.threadPostsFromThread(selectedThread);
            System.out.println(threadMessages);
            System.out.println("Select index of Post between 1 and " + threadPosts.size());
            String option = this.askUser();
            try {
                int index = Integer.parseInt(option);
                if (index < 1 || index > threadPosts.size()) {
                    System.out.println("Invalid Option");
                    return chooseThreadPostFromThread(selectedThread);
                } else {
                    return threadPosts.get(index - 1);
                }
            } catch (Exception e) {
                System.out.println("Invalid Option");
                return chooseThreadPostFromThread(selectedThread);
            }
        }
    }

    public void createNewThread(String title, User loggedInUser) throws SQLException {
        allThreads.clear();
        allThreads = DatabaseAccess.getAllThreads();
        boolean threadExists = false;
        for (Thread thread : allThreads) {
            if (thread.getTitle().equals(title)) {
                threadExists = true;
                break;
            }
        }
        if (threadExists) {
            System.out.println("This title already Exists");
            MainApplication.mainMenu(loggedInUser);
        } else {
            String sqlQuerry = "insert into forumthreads (title) values (?);";
            data.add(title);
            DatabaseAccess.update(sqlQuerry, data);
            System.out.println("Thread succesfully created");
            allThreads.clear();
            allThreads = DatabaseAccess.getAllThreads();
        }
    }

    public Thread chooseThread() {
        allThreads.clear();
        allThreads = DatabaseAccess.getAllThreads();
        if (allThreads.isEmpty()) {
            return null;
        } else {
            System.out.println("Select index of Thread below:");
            int i = 1;
            for (Thread thread : allThreads) {
                System.out.println(i + " - " + thread.getTitle() + " - Posts: " + thread.getPostsCount());
                i++;
            }
            System.out.print("Your option: ");
            String answer = sc.nextLine();
            try {
                int answerToString = Integer.parseInt(answer);
                if (answerToString <= 0 || answerToString > i) {
                    System.out.println("Please select a valid index");
                    return this.chooseThread();
                } else {
                    Thread selectedThread = allThreads.get(answerToString - 1);
                    return selectedThread;
                }
            } catch (Exception e) {
                System.out.println("Please select a valid index");
                return this.chooseThread();
            }
        }
    }

    public Thread getThreadFromTitle(String title) {
        allThreads.clear();
        allThreads = DatabaseAccess.getAllThreads();
        Thread threadToBeReturned = new Thread();
        for (Thread thread : allThreads) {
            if (thread.getTitle().equals(title)) {
                threadToBeReturned = thread;
                break;
            }
        }
        return threadToBeReturned;
    }

    public String threadPostsFromThread(Thread selectedThread) {
        threadPosts.clear();
        threadPosts = DatabaseAccess.getAllPostsFromThread(selectedThread.getThreadID());
        int i = 1;

        String allPostsFromThisThread = "Thread: " + selectedThread.getTitle() + "\n" + "\n";
        for (ThreadPost threadPost : threadPosts) {
            String index = "Post " + "(" + i + ")";
            String user = threadPost.getUsername();
            String message = threadPost.getMessage();
            Timestamp date = threadPost.getDate();
            int edited = threadPost.getEdited();
            Timestamp dateEdited = threadPost.getDateEdited();
            String isEdited = "";
            if (edited == 1) {
                isEdited = "(edited) LastEdited: " + dateEdited;
            }
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            allPostsFromThisThread += index + "     " + dateString + "      " + isEdited + "\n" + user + ": " + message + "\n" + "\n";
            i = i + 1;
        }
        return allPostsFromThisThread;
    }

    public void postToThread(Thread selectedThread, String message, User loggedInUser) {
        if (selectedThread.getLocked() == 1) {
            System.out.println("THREAD LOCKED BY ADMIN");
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
            String sqlQuerry = "insert into thread_messages(username,message,threadID) values(?,?,?);";
            data.addAll(Arrays.asList(loggedInUser.getUsername(), message, selectedThread.getThreadID()));
            DatabaseAccess.update(sqlQuerry, data);
            String messageToFile = selectedThread.getTitle() + "\n" + date + "\n" + loggedInUser.getUsername() + ": " + message + "\n" + "\n";
            FileAccess.writeToThreadMessagesFile(messageToFile);
            threadPosts.clear();
            threadPosts = DatabaseAccess.getAllPostsFromThread(selectedThread.getThreadID());
        }

    }

    public void changePassword(User loggedInUser) throws SQLException {
        System.out.print("Old Password: ");
        String typedPassword = sc.nextLine();
        String existingPassword = loggedInUser.getPassword();

        if (!existingPassword.equals(typedPassword)) {
            System.out.println("Wrong Password");
            this.changePassword(loggedInUser);
        } else {
            System.out.print("New Password: ");
            String newPassword = sc.nextLine();

            String sqlQuerry = "update username set password=? where username=?";
            data.addAll(Arrays.asList(newPassword, loggedInUser.getUsername()));
            DatabaseAccess.update(sqlQuerry, data);

            System.out.println("Succesfully Password Changed");
            System.out.println("");
            loggedInUser.setPassword(newPassword);
            allUsers.clear();
            allUsers = DatabaseAccess.getAllUsers();
            MainApplication.mainMenu(loggedInUser);
        }

    }

    public void editPersonalAccountOptions(User userToGetEdited, User Editor) throws SQLException {
        conn = DatabaseAccess.getConnection();
        String name = userToGetEdited.getName();
        String surname = userToGetEdited.getSurname();
        String email = userToGetEdited.getEmail();
        int level = userToGetEdited.getLevel();
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Email: " + email);
        System.out.println("a - Edit name \nb - Edit surname\nc - Edit email\nd - Delete Account\ne - Main Menu");
        String option = this.askUser();
        switch (option) {
            case "a":
                System.out.println("Old name: " + name);
                System.out.print("New name: ");
                String newName = sc.nextLine();
                String sqlQuerry = "update username set name=? where username=?";
                data.addAll(Arrays.asList(newName, userToGetEdited.getUsername()));
                DatabaseAccess.update(sqlQuerry, data);
                userToGetEdited.setName(newName);
                allUsers.clear();
                allUsers = DatabaseAccess.getAllUsers();
                this.editPersonalAccountOptions(userToGetEdited, Editor);
                break;
            case "b":
                System.out.println("Old surname: " + surname);
                System.out.print("New surname: ");
                String newSurname = sc.nextLine();
                String sqlQuerry2 = "update username set surname=? where username=?";
                data.addAll(Arrays.asList(newSurname, userToGetEdited.getUsername()));
                DatabaseAccess.update(sqlQuerry2, data);
                userToGetEdited.setSurname(newSurname);
                allUsers.clear();
                allUsers = DatabaseAccess.getAllUsers();
                this.editPersonalAccountOptions(userToGetEdited, Editor);
                break;
            case "c":
                System.out.println("Old email: " + email);
                System.out.print("New email: ");
                String newEmail = sc.nextLine();
                String sqlQuerry3 = "update username set email=? where username=?";
                data.addAll(Arrays.asList(newEmail, userToGetEdited.getUsername()));
                DatabaseAccess.update(sqlQuerry3, data);
                userToGetEdited.setEmail(newEmail);
                allUsers.clear();
                allUsers = DatabaseAccess.getAllUsers();
                this.editPersonalAccountOptions(userToGetEdited, Editor);
                break;
            case "d":
                if (userToGetEdited.getLevel() == 4) {
                    System.out.println("You can't delete Admin.");
                    this.editPersonalAccountOptions(userToGetEdited, Editor);
                    break;
                } else {
                    this.deleteAccount(userToGetEdited);
                    if (userToGetEdited.equals(Editor)) {
                        MainApplication.startingMenu();
                        break;
                    } else {
                        MainApplication.mainMenu(Editor);
                        break;
                    }
                }
            case "e":
                MainApplication.mainMenu(Editor);
                break;
            default:
                System.out.println("Invalid Option");
                this.editPersonalAccountOptions(userToGetEdited, Editor);
        }

    }

    public void deleteAccount(User userToGetDeleted) {
        if (userToGetDeleted.getLevel() == 4) {
            System.out.println("You can't delete Admin");
        } else {
            String sqlQuerry = "delete from username where username=?";
            data.add(userToGetDeleted.getUsername());
            DatabaseAccess.update(sqlQuerry, data);
            System.out.println(userToGetDeleted.getUsername() + " Successfully Deleted");
            allUsers.clear();
            allUsers = DatabaseAccess.getAllUsers();
            bannedUsers.clear();
            bannedUsers = DatabaseAccess.getAllBannedUsers();
        }
    }

    public void chat(User selectedUser, User loggedInUser) throws SQLException {
        if (blockedChat) {
            System.out.println("Chat with " + selectedUser.getUsername() + " is Blocked");
        } else {
            System.out.print("Type your message: ");
            String yourMessage = sc.nextLine();
            if (yourMessage.length() > 250) {
                System.out.println("Message must be maximum 250 Characters");
                this.chat(selectedUser, loggedInUser);
            } else {
                this.sendMessage(loggedInUser, selectedUser, yourMessage);
                String allMessages = this.personalChat(selectedUser, loggedInUser);
                System.out.println(allMessages);
            }
        }
    }

    public void sendMessage(User loggedInUser, User toUser, String message) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String sqlQuerry = "insert into messages (fromUser,toUser,message,date) values (?,?,?,?)";
        data.addAll(Arrays.asList(loggedInUser.getUsername(), toUser.getUsername(), message, date));
        DatabaseAccess.update(sqlQuerry, data);
        String messageToFile = date + "  " + loggedInUser.getUsername() + " to " + toUser.getUsername() + "\n" + "Message: " + message + " \n" + "\n";
        FileAccess.writeToUserMessagesFile(messageToFile);
        allChatMessages.clear();
        allChatMessages = DatabaseAccess.getAllChatMessagesBetween(loggedInUser.getUsername(), toUser.getUsername());
    }

    public void checkIfBlocked(User selectedUser, User loggedInUser) {
        try {
            conn = DatabaseAccess.getConnection();
            youAreTheBlocker = false;
            youAreBlocked = false;
            blockedChat = false;
            ps = conn.prepareStatement("select blocked from block where blocker=? or blocker=?");
            ps.setString(1, selectedUser.getUsername());
            ps.setString(2, loggedInUser.getUsername());
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String blockedUser = resultSet.getString("blocked");
                if (blockedUser.equals(selectedUser.getUsername()) || blockedUser.equals(loggedInUser.getUsername())) {
                    blockedChat = true;
                }
                if (blockedUser.equals(selectedUser.getUsername())) {
                    youAreTheBlocker = true;
                }
                if (blockedUser.equals(loggedInUser.getUsername())) {
                    youAreBlocked = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void blockUser(User selectedUser, User loggedInUser) {
        if (usernameToGetBlocked.equals("admin")) {
            System.out.println("You can't block admin.\nAdmin is a GOD !");
        } else {
            if (blockedChat) {
                if ((!youAreTheBlocker) && youAreBlocked) {
                    String sqlQuerry = "insert into block (blocker,blocked) values (?,?)";
                    data.addAll(Arrays.asList(blocker, usernameToGetBlocked));
                    DatabaseAccess.update(sqlQuerry, data);
                    System.out.println(" You blocked " + selectedUser.getUsername() + "\n" + selectedUser.getUsername() + " has also blocked you");
                    this.checkIfBlocked(selectedUser, loggedInUser);
                } else if (youAreTheBlocker && youAreBlocked) {
                    System.out.println("You have already both blocked each other with " + selectedUser.getUsername());
                } else {
                    System.out.println("You have already blocked " + selectedUser.getUsername());
                }
            } else {
                String sqlQuerry = "insert into block (blocker,blocked) values (?,?)";
                data.addAll(Arrays.asList(blocker, usernameToGetBlocked));
                DatabaseAccess.update(sqlQuerry, data);
                System.out.println("Chat with " + usernameToGetBlocked + " is now blocked");
                blockedChat = true;
                this.checkIfBlocked(selectedUser, loggedInUser);
            }
        }
    }

    public void unblockUser(User selectedUser, User loggedInUser) {
        if (youAreTheBlocker && (!youAreBlocked)) {
            String sqlQuerry = "delete from block where blocker=? and blocked=?";
            data.addAll(Arrays.asList(blocker, usernameToGetBlocked));
            DatabaseAccess.update(sqlQuerry, data);
            System.out.println("Chat with " + usernameToGetBlocked + " is now Unblocked");
            blockedChat = false;
            this.checkIfBlocked(selectedUser, loggedInUser);
        } else if (youAreTheBlocker && youAreBlocked) {
            String sqlQuerry = "delete from block where blocker=? and blocked=?";
            data.addAll(Arrays.asList(blocker, usernameToGetBlocked));
            DatabaseAccess.update(sqlQuerry, data);
            System.out.println("You unblocked " + selectedUser.getUsername() + " BUT " + selectedUser.getUsername() + " has also blocked you");
            this.checkIfBlocked(selectedUser, loggedInUser);
        } else if ((!youAreTheBlocker) && youAreBlocked) {
            System.out.println("You can't unblock chat.\n" + selectedUser.getUsername() + " has blocked you.");
        } else {
            System.out.println("Chat is not blocked");
        }

    }

    public void seeLeaderBoard(User loggedInUser) {
        ArrayList<String> leaderboardUsernames = new ArrayList<String>();
        ArrayList<Integer> posts = new ArrayList<Integer>();
        try {
            conn = DatabaseAccess.getConnection();
            ps = conn.prepareStatement("select username, count(username) as posts \n"
                    + "from thread_messages\n"
                    + "group by username\n"
                    + "order by posts desc "
                    + "limit 4;");
            resultSet = ps.executeQuery();

            int users = 0;
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int count = resultSet.getInt("posts");
                leaderboardUsernames.add(username);
                posts.add(count);
                users++;
            }
            if (users == 0) {
                System.out.println("First: Null \nPOSTS: NULL\n\nSecond: Null\nPOSTS: NULL\n\nThird: Null\nPOSTS: NULL\n\nFourth: Null\nPOSTS: NULL");
            } else if (users == 1) {
                System.out.println("First: " + leaderboardUsernames.get(0) + "\nPOSTS: " + posts.get(0) + "\n\nSecond: Null\nPOSTS: NULL\n\nThird: Null\nPOSTS: NULL\n\nFourth: Null\nPOSTS: NULL");
            } else if (users == 2) {
                System.out.println("First: " + leaderboardUsernames.get(0) + "\nPOSTS: " + posts.get(0) + "\n\nSecond: " + leaderboardUsernames.get(1) + "\nPOSTS: " + posts.get(1) + "\n\nThird: Null\nPOSTS: NULL\n\nFourth: Null\nPOSTS: NULL");
            } else if (users == 3) {
                System.out.println("First: " + leaderboardUsernames.get(0) + "\nPOSTS: " + posts.get(0) + "\n\nSecond: " + leaderboardUsernames.get(1) + "\nPOSTS: " + posts.get(1) + "\n\nThird: " + leaderboardUsernames.get(2) + "\nPOSTS: " + posts.get(2) + "\n\nFourth: Null\nPOSTS: NULL");
            } else if (users == 4) {
                System.out.println("First: " + leaderboardUsernames.get(0) + "\nPOSTS: " + posts.get(0) + "\n\nSecond: " + leaderboardUsernames.get(1) + "\nPOSTS: " + posts.get(1) + "\n\nThird: " + leaderboardUsernames.get(2) + "\nPOSTS: " + posts.get(2) + "\n\nFourth: " + leaderboardUsernames.get(3) + "\nPOSTS: " + posts.get(3));
            }
            boolean invalidOption = true;
            while (invalidOption) {
                System.out.println("a - Main Menu");
                System.out.print("Your option: ");
                String option = sc.nextLine();
                if (option.equals("a")) {
                    invalidOption = false;
                    MainApplication.mainMenu(loggedInUser);
                } else {
                    System.out.println("Invalid Option");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
