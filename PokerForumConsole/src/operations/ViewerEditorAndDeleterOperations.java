
package operations;

import java.sql.SQLException;
import pokerforumconsole.MainApplication;
import static pokerforumconsole.PokerForumConsole.allChatMessages;
import static pokerforumconsole.PokerForumConsole.data;
import static pokerforumconsole.PokerForumConsole.threadPosts;
import entities.ChatMessage;
import database.DatabaseAccess;
import entities.ThreadPost;
import entities.Thread;
import entities.User;

public class ViewerEditorAndDeleterOperations extends ViewerAndEditorOperations {

    public ViewerEditorAndDeleterOperations() {
    }

    @Override
    public void showMainMenu(User loggedInUser) {
        super.showMainMenu(loggedInUser);
        System.out.println("(j) Delete Permanently Any Message from Any User");
    }

    @Override
    public void goToOptionInMainMenu(String option, User loggedInUser) throws SQLException {
        super.goToOptionInMainMenu(option, loggedInUser);
        switch (option) {
            case "j"://delete permanently any message from any user
                System.out.println("Select first User");
                User firstUser = this.chooseUser();
                System.out.println("Select second User");
                User secondUser = this.chooseUser();
                this.deleteMessagesMenu(firstUser, secondUser, loggedInUser);
                break;
            default:
                if (loggedInUser.getLevel() == 3) {
                    System.out.println("Invalid Option");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                }
        }
    }

    public void deleteMessagesMenu(User firstUser, User secondUser, User deleter) throws SQLException {
        String allMessages = this.allChatMessagesBetween(firstUser, secondUser);
        System.out.println(allMessages);
        System.out.println("a - Delete Message\nb - Main Menu");
        String option = this.askUser();
        switch (option) {
            case "a":
                ChatMessage selectedMessage = this.getMessageFromChatBetween(firstUser, secondUser);
                if (selectedMessage == null) {
                    System.out.println("There are No messages Between " + firstUser.getUsername() + " and " + secondUser.getUsername());
                    MainApplication.mainMenu(deleter);
                    break;
                } else {
                    this.deletePermanentlyAnyMessage(selectedMessage);
                    this.deleteMessagesMenu(firstUser, secondUser, deleter);
                    break;
                }
            case "b":
                MainApplication.mainMenu(deleter);
                break;
            default:
                System.out.println("Invalid Option");
                this.deleteMessagesMenu(firstUser, secondUser, deleter);
                break;
        }
    }

    public void deletePermanentlyAnyMessage(ChatMessage messageToBeDeleted) {
        int messageID = messageToBeDeleted.getMessageID();
        String sqlQuerry = "delete from messages where messageID=?";
        data.add(messageID);
        DatabaseAccess.update(sqlQuerry, data);
        allChatMessages.clear();
        allChatMessages = DatabaseAccess.getAllChatMessagesBetween(messageToBeDeleted.getFromUser(), messageToBeDeleted.getToUser());
    }

    @Override
    public void showThreadOptionsMenu() {
        super.showThreadOptionsMenu();
        System.out.println("e - Delete Thread Post");
    }

    @Override
    public void goToThreadsMenuOption(String option, Thread selectedThread, User loggedInUser) throws SQLException {
        super.goToThreadsMenuOption(option, selectedThread, loggedInUser);
        switch (option) {
            case "e"://delete Thread Post
                ThreadPost chosenPost = this.chooseThreadPostFromThread(selectedThread);
                if (chosenPost == null) {
                    System.out.println("There no Posts to Delete");
                    this.showThreadOptionsMenu();
                    String typedOption = this.askUser();
                    this.goToThreadsMenuOption(typedOption, selectedThread, loggedInUser);
                    break;
                } else {
                    deleteThreadPost(chosenPost);
                    System.out.println(this.threadPostsFromThread(selectedThread));
                    this.showThreadOptionsMenu();
                    String answer = this.askUser();
                    this.goToThreadsMenuOption(answer, selectedThread, loggedInUser);
                    break;
                }
            default:
                System.out.println("Invalid Option");
                this.showThreadOptionsMenu();
                String typedOption = this.askUser();
                this.goToThreadsMenuOption(typedOption, selectedThread, loggedInUser);
                break;
        }
    }

    public void deleteThreadPost(ThreadPost post) {
        String sqlQuerry = "delete from thread_messages where thred_message_id=?";
        data.add(post.getThread_message_id());
        DatabaseAccess.update(sqlQuerry, data);
        threadPosts.clear();
        threadPosts = DatabaseAccess.getAllPostsFromThread(post.getThreadID());
    }

}
