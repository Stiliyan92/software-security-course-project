package Authentication;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by stili on 10/6/2016.
 */
public class Account {

    public Account(Integer id, String name, String pwd, Boolean locked, String role) {
        this.id = id;
        this.username = name;
        this.password = pwd;
        this.isLocked = locked;
        this.userRole = role;
        this.loggedIn = false;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLocked(boolean isLocked){
        this.isLocked = isLocked;
    }

    public void setLoggedIn(boolean state){
        this.loggedIn = state;
    }

    public boolean getLoggenIn(){
        return this.loggedIn;
    }

    public boolean isLocked(){
        return this.isLocked;
    }

    public Account(Account other){
        this.username = other.getUsername();
        this.password = other.getPassword();
        this.isLocked = other.isLocked();
        this.loggedIn = other.loggedIn;

    }

    private int id;
    private boolean loggedIn;
    private boolean isLocked;
    private String userRole;
    private String username;
    private String password;
    private String phone;
    private String email;

    public int getId() {
        return id;
    }

    public static Account FetchAccountFromDB(ResultSet rs)
    {
        Account userToCreate = null;
        try
        {
            int id = rs.getInt("ID");
            String userName = rs.getString("name");
            String password = rs.getString("password");
            Boolean isLocked = rs.getBoolean("locked");
            String userRole = rs.getString("role");
//            String phone = rs.getString("phone");
//            String email = rs.getString("email");
            userToCreate = new Account(id, userName, password, isLocked, userRole);
        }
        catch (SQLException e) {
//            e.printStackTrace("Error in fetching user from DB:  \n" + e.getMessage());
        }
        return userToCreate;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Account other = (Account) o;
        // field comparison
        return Objects.equals(this.username, other.getUsername());
    }

    @Override
    public String toString() {
        return "Account{" +
                "loggedIn=" + loggedIn +
                ", isLocked=" + isLocked +
                ", userRole='" + userRole + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
