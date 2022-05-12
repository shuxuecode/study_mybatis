import java.sql.*;

/**
 * @author
 * @date 2022/5/12
 */
public class UnitTest {

    private static final String driver_class = "org.h2.Driver";
    private static final String jdbc_url = "jdbc:h2:mem:test";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName(driver_class);

        Connection conn = DriverManager.getConnection(jdbc_url, username, password);

        Statement statement = conn.createStatement();

        statement.execute("drop table if exists t_user");

        statement.execute("create table t_user(id int primary key, name varchar(20) not null, age int not null)");

        statement.executeUpdate("insert into t_user values(1, '测试', 20)");

        ResultSet resultSet = statement.executeQuery("select * from t_user");

        while (resultSet.next()) {
            System.out.print("id = " + resultSet.getInt("id"));
            System.out.print("name = " + resultSet.getString("name"));
            System.out.print("age = " + resultSet.getInt("age"));

            System.out.println();
        }

        statement.close();
        conn.close();

    }

}
