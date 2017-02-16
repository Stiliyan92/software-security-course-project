package Authentication;

import java.text.MessageFormat;

/**
 * Created by stili on 12/30/2016.
 */

public final class Consts  {
    // PUBLIC //
    public final static int GRANT_PRIVILEGE = 8;

    public static final MessageFormat ARE_FRIENDS = new MessageFormat("select friend2 from Friends where friend1 = ''{0}'' and friend2 = ''{1}''\n" +
            "Union All\n" +
            "select friend1 from Friends where friend2 = ''{1}'' and friend1 = ''{0}''");

    public static final MessageFormat ALL_FRIENDS = new MessageFormat("select friend2 from friends where friend1 = ''{0}'' " +
            "Union All " +
            "select friend1 from friends where friend2 = ''{0}'';");

    public static final MessageFormat INSERT_INTO_ACCOUNT = new MessageFormat(
            "INSERT INTO account (name, password, locked, role) VALUES(''{0}'', ''{1}'', {2}, ''{3}'');");
    public static final MessageFormat INSERT_INTO_USER = new MessageFormat(
            "INSERT INTO user (name, phone, email) VALUES(''{0}'', ''{1}'', ''{2}'');");
    public static final MessageFormat DELETE_ACCOUNT = new MessageFormat("DELETE FROM ACCOUNT WHERE NAME = ''{0}'';");
    public static final String SELECT_ALL_USERS = "SELECT * FROM account;";
    public static final MessageFormat CHECK_FOR_USER = new MessageFormat("SELECT * FROM ACCOUNT WHERE NAME = ''{0}'';");
    public static final MessageFormat SET_PARAMETER = new MessageFormat("update account set {0} = {1} where name=''{2}'';");
    public static final MessageFormat SET_PASSWORD = new MessageFormat("update account set {0} = ''{1}'' where name=''{2}'';");
    public static final String HEADER = "{typ : JWT, alg : HS256}";
    // PRIVATE //

    private Consts(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
