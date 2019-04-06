package entities;


public class User {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private int level;
    private int banned;

    public User() {
    }

    public User(String username, String password, String name, String surname, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.level = 0;
        this.banned = 0;
    }

    public User(String username, String password, String name, String surname, String email, int level, int banned) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.level = level;
        this.banned = banned;
    }

    public User(String username, String password, String name, String surname, String email, int level) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.level = level;
        this.banned = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "\nUser{" + "username=" + username + ", password=" + password + ", name=" + name + ", surname=" + surname + ", email=" + email + ", level=" + level + ", banned=" + banned + '}';
    }

    
   
}
