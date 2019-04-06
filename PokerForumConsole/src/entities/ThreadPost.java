
package entities;

import java.sql.Timestamp;


public class ThreadPost {
    private int thread_message_id;
    private String username;
    private String message;
    private Timestamp date;
    private int ThreadID;
    private int edited;
    private  Timestamp dateEdited;

    public ThreadPost() {
    }

    public ThreadPost(int thread_message_id, String username, String message, Timestamp date, int ThreadID, int edited, Timestamp dateEdited) {
        this.thread_message_id = thread_message_id;
        this.username = username;
        this.message = message;
        this.date = date;
        this.ThreadID = ThreadID;
        this.edited = edited;
        this.dateEdited = dateEdited;
    }

    public ThreadPost(String username, String message, int ThreadID) {
        this.username = username;
        this.message = message;
        this.ThreadID = ThreadID;
        this.date=new Timestamp(System.currentTimeMillis());
        this.edited=0;
        this.dateEdited=null;
    }

    public int getThread_message_id() {
        return thread_message_id;
    }

    public void setThread_message_id(int thread_message_id) {
        this.thread_message_id = thread_message_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getThreadID() {
        return ThreadID;
    }

    public void setThreadID(int ThreadID) {
        this.ThreadID = ThreadID;
    }

    public int getEdited() {
        return edited;
    }

    public void setEdited(int edited) {
        this.edited = edited;
    }

    public Timestamp getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Timestamp dateEdited) {
        this.dateEdited = dateEdited;
    }

    @Override
    public String toString() {
        return "\nThreadPosts{" + "thread_message_id=" + thread_message_id + ", username=" + username + ", message=" + message + ", date=" + date + ", ThreadID=" + ThreadID + ", edited=" + edited + ", dateEdited=" + dateEdited + '}';
    }
    
}
