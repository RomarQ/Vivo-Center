package pt.ubi.pdm.vivo.Users;


import java.util.ArrayList;
import java.util.Date;

import pt.ubi.pdm.vivo.session;

public class User {

    private boolean isAdmin = false;
    private String User_id;
    private String Username;
    private String Email;
    private Date CreatedAt;
    private ArrayList<Date> LoginLog = new ArrayList<>();

    public User () {
        User_id = "";
        Username = "";
        Email = "";
        CreatedAt = null;
    }

    public User (String id, String username, String email, Date createdAt, ArrayList<Date> loginLog, boolean admin) {
        this.User_id = id;
        this.Username = username;
        this.Email = email;
        this.CreatedAt = createdAt;
        this.isAdmin = admin;
        // LoginLog clone
        for (Date login: loginLog)
            this.LoginLog.add(login);
    }

    public boolean getIsAdmin() { return isAdmin; }
    public String getUserId() { return User_id; }
    public String getUsername() { return Username; }
    public String getEmail() { return Email; }
    public Date getCreatedAt() { return CreatedAt; }
    public Date getLastLogin() { return LoginLog.get(LoginLog.size()-1); }
    public ArrayList<Date> getLoginLog() { return LoginLog; }

    public User getUser(String id) {
        for (User u : session.users) {
            if (u.getUserId().equals(id)){
                return new User(
                        id,
                        u.getUsername(),
                        u.getEmail(),
                        u.getCreatedAt(),
                        u.getLoginLog(),
                        u.getIsAdmin()
                );
            }
        }
        return null;
    }

    public int getUserIndex(String id) {
        for (int i = 0; i<session.users.size(); i++)
            if (session.users.get(i).getUserId().equals(id))
                return i;

        return -1;
    }

    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin;}

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public void setLogin(Date login) { LoginLog.add(login); }
}
