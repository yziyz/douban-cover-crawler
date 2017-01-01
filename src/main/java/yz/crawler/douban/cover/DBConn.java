package yz.crawler.douban.cover;

/**
 * Created by yz on 12/31/16.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConn {
    private final String DRIVER = "org.mariadb.jdbc.Driver";
    private final String URL = "jdbc:mariadb://" + Main.IP + "/LIBSYS";
    private final String USER = Main.USER;
    private final String PASSWD = Main.PASSWD;
    private Connection conn = null;

    public DBConn() {
        try {
            Class.forName(DRIVER);// load the Connecting class
            conn = DriverManager.getConnection(URL, USER, PASSWD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveRecord(String isbn, String URL) {
        try {
            String sql = "INSERT INTO IMAGE(M_ISBN, URL) VALUES(?,?);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setString(2, URL);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void closeConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
