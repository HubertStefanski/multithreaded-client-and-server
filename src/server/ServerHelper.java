package server;

import java.sql.*;

public class ServerHelper {


    public static java.sql.Connection invokeConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:3306/Assign2";
            return DriverManager.getConnection(connectionUrl, "root", "");

        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }

    public static boolean authenticate(String studentID) throws SQLException {
        boolean authFlag = false;
        try {
            Connection conn = invokeConnection();
            assert conn != null;
            Statement stat = conn.createStatement();

            String query = String.format("SELECT * FROM `students` WHERE `STUD_ID` = %s",studentID);
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                authFlag = true;
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (
                SQLException e) {
            System.out.println(e);
        }
        return authFlag;

    }

}

