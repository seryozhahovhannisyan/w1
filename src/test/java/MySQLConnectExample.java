import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.ResultSet;

public class MySQLConnectExample {
    public static void main(String[] args) {

        // creates three different Connection objects
        Connection conn1 = null;

        try {
            // connect way #1
            String url1 = "jdbc:mysql://db-dev-hy.connectto.int:6033/connectto";
            String user = "connectto_user";
            String password = "T4-vXsDwXd13";

            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                // System.out.println("Connected to the database test1");
                String sql = "SELECT * FROM user";
                PreparedStatement pstm = conn1.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery();
                while (rs.next()){
                    System.out.println(rs.getString(2)+" "+rs.getString(3));
                }
                rs.close();
                pstm.close();
                conn1.close();
            }
 /*
            // connect way #2
            String url2 = "jdbc:mysql://db-dev-hy.connectto.int:6033/connectto?user=connectto_user&password=T4-vXsDwXd13";
            conn2 = DriverManager.getConnection(url2);
            if (conn2 != null) {
                System.out.println("Connected to the database test2");
            }
 
            // connect way #3
            String url3 = "jdbc:mysql://db-dev-hy.connectto.int:6033/connectto";
            Properties info = new Properties();
            info.put("user", "connectto_user");
            info.put("password", "T4-vXsDwXd13");
 
            conn3 = DriverManager.getConnection(url3, info);
            if (conn3 != null) {
                System.out.println("Connected to the database test3");
            }*/
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
    }
}