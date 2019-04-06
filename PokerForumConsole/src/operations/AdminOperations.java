package operations;

import java.sql.SQLException;
import java.util.Arrays;
import pokerforumconsole.MainApplication;
import static pokerforumconsole.MainApplication.sc;
import static pokerforumconsole.PokerForumConsole.allThreads;
import static pokerforumconsole.PokerForumConsole.allUsers;
import static pokerforumconsole.PokerForumConsole.bannedUsers;
import static pokerforumconsole.PokerForumConsole.data;
import static pokerforumconsole.PokerForumConsole.lockedThreads;
import database.DatabaseAccess;
import entities.User;
import entities.Thread;

public class AdminOperations extends ViewerEditorAndDeleterOperations {

    public AdminOperations() {
    }

    @Override
    public void showMainMenu(User loggedInUser) {
        super.showMainMenu(loggedInUser);
        System.out.println("(k) Edit, Ban, Asign a Role or Delete Any Account\n(l) List of Banned Users (Unban User)\n(m) Delete Thread\n(n) Create Account\n(o) Lock Thread\n(p) Unlock Thread ");
    }

    @Override
    public void goToOptionInMainMenu(String option, User loggedInUser) throws SQLException {
        super.goToOptionInMainMenu(option, loggedInUser);
        switch (option) {
            case "k"://edit,ban,asigng a role or delete any account
                User selectedUser = this.chooseUser();
                this.editAnyAccountOptionsMenu(selectedUser, loggedInUser);
                break;
            case "l"://List of banned Users
                this.bannedUsersMenu(loggedInUser);
                break;
            case "m"://delete thread
                Thread selectedThread = this.chooseThread();
                this.deleteThread(selectedThread);
                MainApplication.mainMenu(loggedInUser);
                break;
            case "n"://create account
                this.createAccount();
                MainApplication.mainMenu(loggedInUser);
                break;
            case "o"://lock thread
                Thread chosenThread = this.chooseThread();
                if (chosenThread == null) {
                    System.out.println("There are No Threads to Lock");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                } else {
                    this.lockThread(chosenThread);
                    MainApplication.mainMenu(loggedInUser);
                    break;
                }
            case "p"://unlock thread
                Thread threadToBeUnlocked = this.chooseLockedThread();
                if (threadToBeUnlocked == null) {
                    System.out.println("There are No Locked Threads");
                    MainApplication.mainMenu(loggedInUser);
                    break;
                } else {
                    this.unlockThread(threadToBeUnlocked);
                    MainApplication.mainMenu(loggedInUser);
                    break;
                }
            default:
                System.out.println("Invalid Option");
                MainApplication.mainMenu(loggedInUser);
        }
    }

    public Thread chooseLockedThread() {
        if (lockedThreads.isEmpty()) {
            return null;
        } else {
            int i = 1;
            System.out.println("List Of Locked Threads:");
            for (Thread thread : lockedThreads) {
                System.out.println(i + " - " + thread.getTitle());
                i++;
            }
            System.out.println("\nSelect Index");
            String option = this.askUser();
            try {
                int index = Integer.parseInt(option);
                if (index < 1 || index > lockedThreads.size()) {
                    System.out.println("Invalid Option");
                    return chooseLockedThread();
                } else {
                    return lockedThreads.get(index - 1);
                }
            } catch (Exception e) {
                System.out.println("Invalid Option");
                return chooseLockedThread();
            }
        }
    }

    public void lockThread(Thread selectedThread) throws SQLException {
        if (selectedThread.getLocked() == 1) {
            System.out.println(selectedThread.getTitle() + " is already Locked");
        } else {
            String sqlQuerry = "update forumthreads set locked=1 where threadID=?";
            data.add(selectedThread.getThreadID());
            DatabaseAccess.update(sqlQuerry, data);
            allThreads.clear();
            allThreads = DatabaseAccess.getAllThreads();
            lockedThreads.clear();
            lockedThreads = DatabaseAccess.getLockedThreads();
            System.out.println(selectedThread.getTitle() + " is now Locked");
        }
    }

    public void unlockThread(Thread selectedThread) throws SQLException {
        if (selectedThread.getLocked() == 0) {
            System.out.println("This thread is Not Locked");
        } else {
            String sqlQuerry = "update forumthreads set locked=0 where threadID=?";
            data.add(selectedThread.getThreadID());
            DatabaseAccess.update(sqlQuerry, data);
            allThreads.clear();
            lockedThreads.clear();
            allThreads = DatabaseAccess.getAllThreads();
            lockedThreads = DatabaseAccess.getLockedThreads();
            System.out.println(selectedThread.getTitle() + " is now UnLocked");
        }
    }

    public void editAnyAccountOptionsMenu(User selectedUser, User editor) throws SQLException {
        System.out.println("a - Edit " + selectedUser.getUsername() + "\nb - Ban " + selectedUser.getUsername() + "\nc - Asign a Role to " + selectedUser.getUsername() + "\nd - Delete " + selectedUser.getUsername() + "\ne - Main Menu");
        String answer = this.askUser();
        switch (answer) {
            case "a":
                this.editPersonalAccountOptions(selectedUser, editor);
                break;
            case "b":
                this.banUser(selectedUser);
                this.editAnyAccountOptionsMenu(selectedUser, editor);
                break;
            case "c":
                if (selectedUser.getLevel() == 4) {
                    System.out.println("You can't asing another role to ADMIN");
                    this.editAnyAccountOptionsMenu(selectedUser, editor);
                    break;
                } else {
                    this.asignRole(selectedUser);
                    this.editAnyAccountOptionsMenu(selectedUser, editor);
                    break;
                }
            case "d":
                this.deleteAccount(selectedUser);
                MainApplication.mainMenu(editor);
                break;
            case "e":
                MainApplication.mainMenu(editor);
                break;
            default:
                System.out.println("Invalid Option");
                this.editAnyAccountOptionsMenu(selectedUser, editor);
                break;
        }
    }

    public void banUser(User userToGetBanned) {
        if (userToGetBanned.getUsername().equals("admin")) {
            System.out.println("You can't ban Admin");
        } else {
            int banned = userToGetBanned.getBanned();
            if (banned == 1) {
                System.out.println(userToGetBanned.getUsername() + " is already banned");
            } else {
                String sqlQuerry = "update username set banned=1 where username=?";
                data.add(userToGetBanned.getUsername());
                DatabaseAccess.update(sqlQuerry, data);
                userToGetBanned.setBanned(1);
                bannedUsers.clear();
                allUsers.clear();
                bannedUsers = DatabaseAccess.getAllBannedUsers();
                allUsers = DatabaseAccess.getAllUsers();
                System.out.println(userToGetBanned.getUsername() + " Succesfully Banned");
            }
        }
    }

    public void asignRole(User userToChangeRole) {
        String selectedUsername = userToChangeRole.getUsername();
        System.out.println("Levels:\n0 - Simple User\n1 - View the transacted data between the users\n2 - View and Edit the transacted data between the users\n3 - View Edit and Delete the transacted data between the users");
        System.out.println(selectedUsername + " is Level: " + userToChangeRole.getLevel());
        System.out.print("Choose Level: ");
        String option = sc.nextLine();
        if (option.equals("0")) {
            this.editLevelOfUser(userToChangeRole, 0);
            System.out.println(userToChangeRole.getUsername() + " is now a simple User");
        } else if (option.equals("1")) {
            this.editLevelOfUser(userToChangeRole, 1);
            System.out.println(selectedUsername + " can now View the transacted data between the users");
        } else if (option.equals("2")) {
            this.editLevelOfUser(userToChangeRole, 2);
            System.out.println(selectedUsername + " can now View  and Edit the transacted data between the users");
        } else if (option.equals("3")) {
            this.editLevelOfUser(userToChangeRole, 3);
            System.out.println(selectedUsername + " can now View, Edit and Delete the transacted data between the users");
        } else {
            System.out.println("Invalid Option");
            this.asignRole(userToChangeRole);
        }
    }

    public void editLevelOfUser(User userToChangeRole, int level) {
        String selectedUsername = userToChangeRole.getUsername();
        String sqlQuerry = "update username set level=? where username=?";
        data.addAll(Arrays.asList(level, selectedUsername));
        DatabaseAccess.update(sqlQuerry, data);
        userToChangeRole.setLevel(level);
        allUsers.clear();
        allUsers = DatabaseAccess.getAllUsers();
    }

    public void bannedUsersMenu(User loggedInUser) throws SQLException {
        bannedUsers.clear();
        bannedUsers = DatabaseAccess.getAllBannedUsers();
        if (bannedUsers.isEmpty()) {
            System.out.println("There are no Banned Users");
            MainApplication.mainMenu(loggedInUser);
        } else {
            this.printBannedUsers();
            System.out.println("\na - Unban\nb - Main Menu");
            String option = this.askUser();
            switch (option) {
                case "a":
                    User userToBeUnbanned = this.chooseBannedUser();
                    if (userToBeUnbanned == null) {
                        System.out.println("There are no banned Users");
                        MainApplication.mainMenu(loggedInUser);
                        break;
                    } else {
                        this.unban(userToBeUnbanned);
                        this.bannedUsersMenu(loggedInUser);
                        break;
                    }
                case "b":
                    MainApplication.mainMenu(loggedInUser);
                    break;
                default:
                    System.out.println("Invalid Option");
                    this.bannedUsersMenu(loggedInUser);
                    break;
            }
        }
    }

    public User chooseBannedUser() {
        if (bannedUsers.isEmpty()) {
            return null;
        } else {
            this.printBannedUsers();
            System.out.print("\nChoose index of user: ");
            String selectedIndex = sc.nextLine();
            try {
                int index = Integer.parseInt(selectedIndex);
                if (index > 0 && index <= bannedUsers.size()) {
                    return bannedUsers.get(index - 1);
                } else {
                    System.out.println("Invalid Option ");
                    return this.chooseBannedUser();
                }
            } catch (Exception e) {
                System.out.println("Invalid Option");
                return this.chooseBannedUser();
            }
        }
    }

    public void printBannedUsers() {
        int i = 1;
        System.out.println("List of Banned Users: ");
        for (User user : bannedUsers) {
            System.out.println(i + " - " + user.getUsername());
            i++;
        }
    }

    public void unban(User userToGetUnbanned) {
        String sqlQuerry = "update username set banned=0 where username=?";
        data.add(userToGetUnbanned.getUsername());
        DatabaseAccess.update(sqlQuerry, data);
        bannedUsers.clear();
        bannedUsers = DatabaseAccess.getAllBannedUsers();
        allUsers.clear();
        allUsers = DatabaseAccess.getAllUsers();
    }

    public void deleteThread(Thread thread) {
        allThreads.clear();
        allThreads = DatabaseAccess.getAllThreads();
        if (allThreads.isEmpty()) {
            System.out.println("There are no Threads to Delete");
        } else {
            String sqlQuerry = "delete from forumthreads where threadID=?";
            data.add(thread.getThreadID());
            DatabaseAccess.update(sqlQuerry, data);
            allThreads.clear();
            allThreads = DatabaseAccess.getAllThreads();
            System.out.println(thread.getTitle() + " Successfully Deleted");
        }
    }

    public void createAccount() throws SQLException {
        User newUser = MainApplication.signUp();
        asignRole(newUser);
    }

}
