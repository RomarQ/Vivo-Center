package pt.ubi.pdm.vivo.profile;

import java.util.Date;

public class Profile {

    // Every time a user connects, is created an object Profile
    // And is destroyed when App is closed

    // All atributes can be static since there will only be 1 instance UP during APP lifecycle
    private static boolean isAdmin = false;
    private static String User_id;
    private static String Username;
    private static String Email;
    private static Date CreatedAt;
    private static String Token;

    public Profile () {
        User_id = "";
        Username = "";
        Email = "";
        CreatedAt = null;
        Token = "";
    }

    public boolean getIsAdmin() { return isAdmin; }
    public String getUserId() { return User_id; }
    public String getUsername() { return Username; }
    public String getEmail() { return Email; }
    public Date getCreatedAt() { return CreatedAt; }
    public String getToken() { return Token; }

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

    public void setToken(String token) {
        Token = token;
    }

}
