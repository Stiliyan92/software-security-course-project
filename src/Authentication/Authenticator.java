package Authentication;


import Exceptions.AuthenticationError;
import Exceptions.LockedAccount;
import Exceptions.UndefinedAccount;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stili on 10/11/2016.
 */

public class Authenticator {


    private static DBConnector db;
    private static Authenticator authenticator = null;
    private static String DBLoggedInEntry = "logged";
    private static String DBLockedEntry = "locked";
    private static String DBPasswordEntry = "password";
    private List<Account> accounts;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private Authenticator() {
        accounts = new ArrayList<Account>();
        db = DBConnector.getInstance();
    }

    /* Static 'instance' method */
    public static Authenticator getInstance( ) {
        if (authenticator == null){
            authenticator = new Authenticator();
        }
        return authenticator;
    }

    /* Other methods protected by singleton-ness */
    public String demoMethod( ) {
        try {
            db.getDB(Consts.SELECT_ALL_USERS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "demoMethod for singleton";
    }

    public int createAccount(String name, String pwd1, String pwd2, String phone, String email) {
        int i = -99;
        if (!pwd1.equals(pwd2)) {
            System.out.print("Password doesn't match");
            return 12;
        }
        if (checkifUserExists(name) != null) {
            System.out.print("User with that name already exists");
            return 11;
        } else {
            String encryptedPassword = null;
            try {
                encryptedPassword = AESencrp.encrypt(pwd1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String[] accountFormaters = {name, encryptedPassword, "false", "user"};
                String accountQuery = Consts.INSERT_INTO_ACCOUNT.format(accountFormaters);
                i = this.db.updateDB(accountQuery);

                String[] userFormaters = {name, phone, email};
                String userQuery = Consts.INSERT_INTO_USER.format(userFormaters);
                i = this.db.updateDB(userQuery);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public Account checkifUserExists(String name){
        if(name != null){
            String[] stringFormaters = {name};
            String query = Consts.CHECK_FOR_USER.format(stringFormaters);
            try {
                ResultSet rs = db.getDB(query);
                if (rs.isBeforeFirst() ) {
                    rs.first();
                    Account acc = Account.FetchAccountFromDB(rs);
                    return acc;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deleteAccount(String name) {
        Account account = checkifUserExists(name);
        if (account != null) {
            Account logged_account = checkifUserIsLogged(name);
            if(logged_account != null) {
                System.out.println( name + " is logged in!");
                this.logout(logged_account);
                this.accounts.remove(logged_account);
            }
            this.lockAccount(name);
            String[] stringFormaters = {name};
            String query = Consts.DELETE_ACCOUNT.format(stringFormaters);
            try {
                this.db.updateDB(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("There is no such user");
        }
    }

    public Account getAccount(String name){
        Account acc = this.checkifUserExists(name);
        if(acc != null){
            return new Account(acc);
        }
        else return null;

    }
    public Account login(String name, String pwd) {
        Account account = this.checkifUserExists(name);
        if(account == null){
            throw new UndefinedAccount("User with name" + name + "is not registered");
        }
        if(account.isLocked()){
            throw new LockedAccount(name + "'s account is locked");
        }
        accounts.add(account);
        try {
            String encryptedPassword = AESencrp.encrypt(pwd);
            if(encryptedPassword.equals(account.getPassword())){
                account.setLoggedIn(true);
                this.changeAccountParameter(name, this.DBLoggedInEntry, "true", Consts.SET_PARAMETER);
            }
            else{
                throw new AuthenticationError("Passwords do not match");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return account;
    }

    public Account changePassword(String name, String pwd1, String pwd2){
        Account account = this.checkifUserExists(name);
        if(account == null){
            throw new UndefinedAccount("User" + name + " doesn't exist in databae");
        }
        if(!pwd1.equals(pwd2)){
            throw new AuthenticationError("Passwords do not match");
        }
        String encryptedPassword = null;
        try {
            encryptedPassword = AESencrp.encrypt(pwd1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        account.setPassword(encryptedPassword);
            this.changeAccountParameter(name, this.DBPasswordEntry, encryptedPassword, Consts.SET_PASSWORD);
        return account;
    }

    private void changeAccountParameter(String name, String parameter, String value, MessageFormat update_query){
        Account account = this.checkifUserExists(name);
        if (account != null){
            String[] stringFormatters = {parameter, value, name};
            String query = update_query.format(stringFormatters);
            try {
                this.db.updateDB(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("No such user\n");
        }
    }

    public void lockAccount(String name){
        this.changeAccountParameter(name, this.DBLockedEntry, "true", Consts.SET_PARAMETER);
    }

    public void unlockAccount(String name){
        this.changeAccountParameter(name, this.DBLockedEntry, "false", Consts.SET_PARAMETER);
    }

    public void logout(Account acc){
        acc.setLoggedIn(false);
        this.changeAccountParameter(acc.getUsername(), this.DBLoggedInEntry, "false", Consts.SET_PARAMETER);
    }
    private Account checkifUserIsLogged(String name){
        for(int i = 0; i < this.accounts.size(); ++i){
            Account account = this.accounts.get(i);
            if(account.getUsername().equals(name)){
                return account;
            }
        }
        return null;
    }

    public Account login(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession(false);
        String username = (String) req.getParameter("username");
        String password = (String) req.getParameter("password");
        Account acc = this.checkifUserIsLogged(username);
        if(acc != null){
            try {
                String acc_decrypted_pass = AESencrp.decrypt(acc.getPassword());
                if(!acc_decrypted_pass.equals(password)){
                    System.out.println("Password doesn't match");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            //  throw AuthenticationError;
        }
        return acc;
    }
}
