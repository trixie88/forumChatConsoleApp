
package operations;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import pokerforumconsole.MainApplication;
import static pokerforumconsole.MainApplication.sc;
import static pokerforumconsole.PokerForumConsole.allChatMessages;
import static pokerforumconsole.PokerForumConsole.data;
import static pokerforumconsole.PokerForumConsole.threadPosts;
import entities.ChatMessage;
import database.DatabaseAccess;
import entities.ThreadPost;
import entities.Thread;
import entities.User;


public class ViewerAndEditorOperations extends ViewerOperations {

    public ViewerAndEditorOperations() {
    }

    @Override
    public void showMainMenu(User loggedInUser) {
        super.showMainMenu(loggedInUser);
        System.out.println("(i) See and Edit Any Message from any user");
    }

    @Override
    public void goToOptionInMainMenu(String option, User loggedInUser) throws SQLException {
        super.goToOptionInMainMenu(option, loggedInUser);
        switch (option) {
            case "i"://see and edit any message from any user
                System.out.println("Select first User");
                User firstUser = this.chooseUser();
                System.out.println("Select second User");
                User secondUser = this.chooseUser();
                this.editMessagesMenu(firstUser, secondUser, loggedInUser);
                break;
            default:// method gets overrided by the classes they extend this class and has more options in the other other classes so we want this default case to be activated only from  users with level 2.
                if (loggedInUser.getLevel() == 2) {
                    System.out.println("Invalid Option");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                }
        }
    }

    public void editMessagesMenu(User firstUser, User secondUser, User editor) throws SQLException {
        String allMessages = this.allChatMessagesBetween(firstUser, secondUser);
        System.out.println(allMessages);
        System.out.println("a - Edit Message\nb - Main Menu");
        String option = this.askUser();
        switch (option) {
            case "a":
                ChatMessage messageToBeEdited = this.getMessageFromChatBetween(firstUser, secondUser);
                if (messageToBeEdited == null) {
                    System.out.println("There are no Messages to Edit Between " + firstUser.getUsername() + " and " + secondUser.getUsername() + "\n");
                    this.editMessagesMenu(firstUser, secondUser, editor);
                    break;
                } else {
                    this.editMessage(messageToBeEdited, editor);
                    this.editMessagesMenu(firstUser, secondUser, editor);
                    break;
                }
            case "b":
                MainApplication.mainMenu(editor);
                break;
            case "c":
                System.out.println("INVALID OPTION");
                this.editMessagesMenu(firstUser, secondUser, editor);
        }
    }

    public ChatMessage getMessageFromChatBetween(User firstUser, User secondUser) {
        String allMessages = this.allChatMessagesBetween(firstUser, secondUser);
        System.out.println(allMessages);
        if (allChatMessages.isEmpty()) {
            return null;
        } else {
            System.out.println("Select index of Message from 1 to " + allChatMessages.size());
            String stringIndex = sc.nextLine();
            try {
                int index = Integer.parseInt(stringIndex);
                if (index < 1 || index > allChatMessages.size()) {
                    System.out.println("Invalid option ");
                    return this.getMessageFromChatBetween(firstUser, secondUser);
                } else {
                    return allChatMessages.get(index - 1);
                }
            } catch (Exception e) {
                System.out.println("Invalid Option");
                return this.getMessageFromChatBetween(firstUser, secondUser);
            }
        }
    }

    public void editMessage(ChatMessage oldMessage, User editor) {
        System.out.println("Old message: " + oldMessage.getMessage());
        System.out.print("New Message: ");
        String newMessage = sc.nextLine();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String dateEdited = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String sqlQuerry = "update messages set message=?,date=?,edited=1,dateEdited=?,lastEditor=? where messageID=?";
        data.addAll(Arrays.asList(newMessage, oldMessage.getDate(), dateEdited, editor.getUsername(), oldMessage.getMessageID()));
        DatabaseAccess.update(sqlQuerry, data);
        oldMessage.setMessage(newMessage);
        oldMessage.setEdited(1);
        oldMessage.setDateEdited(now);
        allChatMessages.clear();
        allChatMessages = DatabaseAccess.getAllChatMessagesBetween(oldMessage.getFromUser(), oldMessage.getToUser());
    }

    @Override //extends the Thread options menu of user and adds a new choice (edit thread post)
    public void showThreadOptionsMenu() {
        super.showThreadOptionsMenu();
        System.out.println("d - Edit Thread Post");
    }

    @Override
    public void goToThreadsMenuOption(String option, Thread selectedThread, User loggedInUser) throws SQLException {
        super.goToThreadsMenuOption(option, selectedThread, loggedInUser);
        switch (option) {
            case "d"://edit Thread Post
                ThreadPost chosenPost = chooseThreadPostFromThread(selectedThread);
                if (chosenPost == null) {
                    System.out.println("There are no Posts in this Thread");
                    this.showThreadOptionsMenu();
                    String typedOption = this.askUser();
                    this.goToThreadsMenuOption(typedOption, selectedThread, loggedInUser);
                    break;
                } else {
                    this.editThreadPost(chosenPost);
                    System.out.println(this.threadPostsFromThread(selectedThread));
                    this.showThreadOptionsMenu();
                    String answer = this.askUser();
                    this.goToThreadsMenuOption(answer, selectedThread, loggedInUser);
                    break;
                }
            default:
                if (loggedInUser.getLevel() == 2) {
                    System.out.println("Invalid Option");
                    this.showThreadOptionsMenu();
                    String typedOption = this.askUser();
                    this.goToThreadsMenuOption(typedOption, selectedThread, loggedInUser);
                    break;
                }
        }
    }

    public void editThreadPost(ThreadPost post) throws SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String dateEdited = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        System.out.println("Old Post: " + post.getMessage());
        System.out.print("Edit Post: ");
        String editedPost = sc.nextLine();
        String sqlQuerry = "update thread_messages set message=?,date=?,edited=1,dateEdited=? where thred_message_id=?";
        data.addAll(Arrays.asList(editedPost, post.getDate(), dateEdited, post.getThread_message_id()));
        DatabaseAccess.update(sqlQuerry, data);
        threadPosts.clear();
        threadPosts = DatabaseAccess.getAllPostsFromThread(post.getThreadID());
    }

}
