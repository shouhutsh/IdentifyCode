package cn.edu.zzti.Main;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by ae-mp02 on 2015/8/6.
 */
public class DBHelper {
    private DBHelper() {
    }

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;

    static {
        try {
            Properties prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));

            Class.forName(prop.getProperty("jdbc_driver"));
            conn = DriverManager.getConnection(prop.getProperty("db_url"),
                    prop.getProperty("user"),
                    prop.getProperty("pass"));
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long insert(Code code) {
        try {
            stmt.execute(String.format("insert into code(real_char, binary_code, width, height)values('%s', '%s', %s, %s)",
                    code.getReal_char(), code.getBinary_code(), code.getWidth(), code.getHeight()));
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                code.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return code.getId();
    }

    public void delete(Code code) {
        try {
            stmt.execute(String.format("delete from code where id=%s", code.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Code code) {
        try {
            stmt.executeUpdate(String.format("update code set real_char='%s',binary_code='%s',width=%s,height=%s where id=%s",
                    code.getReal_char(), code.getBinary_code(), code.getWidth(), code.getHeight(), code.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Code> findByRealChar(char realChar) {
        List<Code> codes = new ArrayList<Code>();
        try {
            rs = stmt.executeQuery(String.format("select * from code where real_char=%s", realChar));
            while (rs.next()) {
                Code c = new Code();
                c.setId(rs.getInt("id"));
                c.setReal_char(rs.getString("real_char"));
                c.setBinary_code(rs.getString("binary_code"));
                c.setWidth(rs.getInt("width"));
                c.setHeight(rs.getInt("height"));
                codes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codes;
    }

    public Code findById(long id) {
        Code c = null;
        try {
            rs = stmt.executeQuery(String.format("select * from code where id=%s", id));
            if (rs.next()) {
                c = new Code();
                c.setId(rs.getInt("id"));
                c.setReal_char(rs.getString("real_char"));
                c.setBinary_code(rs.getString("binary_code"));
                c.setWidth(rs.getInt("width"));
                c.setHeight(rs.getInt("height"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBHelper helper = new DBHelper();
        Code c = new Code();

        c.setReal_char("r");
        c.setBinary_code("binary");

        long id = helper.insert(c);

        Code c2 = helper.findById(id);

        assert c.equals(c2);
        System.out.println(c);

        c.setBinary_code("test");
        helper.update(c);

        c2 = helper.findById(c.getId());

        assert c.equals(c2);
        System.out.println(c2);

        helper.delete(c);

        helper.close();
    }
}
