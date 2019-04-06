package pokerforumconsole;

import com.mysql.cj.util.StringUtils;
import operations.ViewerEditorAndDeleterOperations;
import operations.ViewerAndEditorOperations;
import operations.CommonOperations;
import operations.AdminOperations;
import operations.ViewerOperations;
import database.DatabaseAccess;
import entities.User;
import java.sql.SQLException;
import java.util.Scanner;
import static pokerforumconsole.PokerForumConsole.allUsers;
import static pokerforumconsole.PokerForumConsole.unreadMessages;

public class MainApplication {

    public static Scanner sc = new Scanner(System.in);

    public static void mainMenu(User loggedInUser) throws SQLException  {
        unreadMessages.clear();
        unreadMessages = DatabaseAccess.getUnreadMessages(loggedInUser.getUsername());
        int level = loggedInUser.getLevel();
        if (level == 0) {
            new CommonOperations().showMainMenu(loggedInUser);
            System.out.print("Your option: ");
            String option = sc.nextLine();
            new CommonOperations().goToOptionInMainMenu(option, loggedInUser);
        } else if (level == 1) {
            new ViewerOperations().showMainMenu(loggedInUser);
            System.out.print("Your option: ");
            String option = sc.nextLine();
            new ViewerOperations().goToOptionInMainMenu(option, loggedInUser);
        } else if (level == 2) {
            new ViewerAndEditorOperations().showMainMenu(loggedInUser);
            System.out.print("Your option: ");
            String option = sc.nextLine();
            new ViewerAndEditorOperations().goToOptionInMainMenu(option, loggedInUser);
        } else if (level == 3) {
            new ViewerEditorAndDeleterOperations().showMainMenu(loggedInUser);
            System.out.print("Your option: ");
            String option = sc.nextLine();
            new ViewerEditorAndDeleterOperations().goToOptionInMainMenu(option, loggedInUser);
        } else if (level == 4) {
            new AdminOperations().showMainMenu(loggedInUser);
            System.out.print("Your option: ");
            String option = sc.nextLine();
            new AdminOperations().goToOptionInMainMenu(option, loggedInUser);
        }
    }

    public static void startingMenu() throws SQLException{
        System.out.println("a - Login\nb - Sign Up\nc - Exit");
        System.out.print("Your Option: ");
        String option = sc.nextLine();
        if (option.equals("a")) {
            Login.login();
        } else if (option.equals("b")) {
            signUp();
            startingMenu();
        } else if (option.equals("c")) {
            System.exit(0);
        } else {
            System.out.println("Invalid Option");
            startingMenu();
        }
    }

    public static User signUp() throws SQLException {
        System.out.print("Select Username: ");
        String username = sc.nextLine();
        boolean usernameExists = false;
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                usernameExists = true;
                break;
            }
        }
        if (usernameExists) {
            System.out.println("This username already exists");
             return signUp();
        } else {
            System.out.print("Select Password: ");
            String password = sc.nextLine();

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Surname: ");
            String surname = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();
            if (StringUtils.isEmptyOrWhitespaceOnly(username) || StringUtils.isEmptyOrWhitespaceOnly(password) || StringUtils.isEmptyOrWhitespaceOnly(name)
                    || StringUtils.isEmptyOrWhitespaceOnly(surname) || StringUtils.isEmptyOrWhitespaceOnly(email)) {
                System.out.println("Please Enter all Fields");
                 return signUp();
            }else{
               User newUser= DatabaseAccess.createUser(username, password, name, surname, email);
                System.out.println("Account "+username+" created Succesfully");
                return newUser;
            }
        }
    }

}
