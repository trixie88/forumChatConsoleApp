
package entities;

public class Thread {

    private int threadID;
    private String title;
    private int locked;
    private int postsCount;
    
    
    public Thread() {
    }

    public Thread(int threadID, String title, int locked, int postsCount) {
        this.threadID = threadID;
        this.title = title;
        this.locked = locked;
        this.postsCount = postsCount;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

   
    @Override
    public String toString() {
        return "\nThread{" + "threadID=" + threadID + ", title=" + title + ", locked=" + locked + ", postsCount=" + postsCount + '}';
    }

    
   
    
    
    
}
