
package pokerforumconsole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileAccess {

    public static void createUserMessagesFile() {
        String path = "C:" + File.separator + "forumMessages" + File.separator + "UserMessages.txt";
        File f = new File(path);

        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeToUserMessagesFile(String message) {
        FileWriter writer;
        try {
            writer = new FileWriter("C:\\forumMessages\\UserMessages.txt", true);
            PrintWriter printer = new PrintWriter(writer);
            printer.print(message);
            printer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createThreadMessagesFile() {
        String path = "C:" + File.separator + "forumMessages" + File.separator + "ThreadMessages.txt";
        File f = new File(path);

        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeToThreadMessagesFile(String message) {
        FileWriter writer;
        try {
            writer = new FileWriter("C:\\forumMessages\\ThreadMessages.txt", true);
            PrintWriter printer = new PrintWriter(writer);
            printer.print(message);
            printer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
