
package pokerforumconsole;


import entities.User;
import java.sql.SQLException;
import java.util.Scanner;
import static pokerforumconsole.PokerForumConsole.allUsers;

public class Login {

    public static void login() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String typedUsername = sc.nextLine();
        System.out.println("");
        System.out.print("Password: ");
        String typedPassword = sc.nextLine();
        boolean userExists = false;
        User loggedInUser = new User();
        for (User user : allUsers) {
            if (user.getUsername().equals(typedUsername) && user.getPassword().equals(typedPassword)) {
                userExists = true;
                loggedInUser = user;
                break;
            }
        }
        if (userExists == false) {
            System.out.println("Wrong username or password");
            login();
        } else if (loggedInUser.getBanned() == 1) {
            System.out.println("You are Banned from Pokerlobby");
        } else {
            MainApplication.mainMenu(loggedInUser);
        }
    }
}
