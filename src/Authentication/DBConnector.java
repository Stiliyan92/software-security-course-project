package Authentication;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Authentication.Account.FetchAccountFromDB;
public class DBConnector {

    private static final String SERVER = "localhost";
    private static final String DBUSER = "root";
    private static final String DBPASS = "";
    private static final String DBNAME = "accounts";
    private static final String DBURL = "jdbc:mysql://localhost:";
    private static final String DBPORT = "3306";
    private static Connection conn;
    private static DBConnector instance;

    public static DBConnector getInstance( ) {
        if (instance == null){
            instance = new DBConnector();
        }
        return instance;
    }

    private DBConnector() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(DBURL + DBPORT + '/' + DBNAME);
        dataSource.setDatabaseName(DBNAME);
        dataSource.setUser(DBUSER);
        dataSource.setPassword(DBPASS);
        dataSource.setServerName(SERVER);
        try {
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareQuery(String query) throws SQLException {
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(query);
            return statement;
    }

    public ResultSet getDB(String query) throws SQLException {
        PreparedStatement stmt = this.prepareQuery(query);
        ResultSet rs = stmt.executeQuery();
            return rs;
        }

    public List<Account> GetObjectsFromDB(String query)
    {
        List<Account> ResList = new ArrayList<Account>();
        try
        {
            PreparedStatement statement = this.prepareQuery(query);
            for (ResultSet rs = statement.executeQuery(query); rs.next();)
            {
                ResList.add(Account.FetchAccountFromDB(rs));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ResList;
    }

    public int updateDB(String query) throws SQLException {
        PreparedStatement stmt = this.prepareQuery(query);
        int rs = stmt.executeUpdate();

        return rs;
    }


}
