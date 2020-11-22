package server;

import java.sql.*;
import java.util.Collections;
import java.util.Map;

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


    public static boolean authenticate (String studentID) {
        boolean authFlag = false;

        try {
            Connection conn = invokeConnection();
            assert conn != null;
            Statement stat = conn.createStatement();

            String query = String.format("SELECT * FROM `students` WHERE `STUD_ID` = %s",studentID);
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                authFlag = true;
                int totreq = rs.getInt("TOT_REQ");
                totreq+=1;

                String updatequery = String.format("UPDATE `students` SET TOT_REQ = %s  WHERE `STUD_ID` = %s",totreq,studentID);
                stat.executeUpdate(updatequery);
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

    public static Map<String, String> getUser(String studentID) {
        Map<String, String> map = new java.util.HashMap<>(Collections.emptyMap());

        try {
            Connection conn = invokeConnection();
            assert conn != null;
            Statement stat = conn.createStatement();

            String query = String.format("SELECT * FROM `students` WHERE `STUD_ID` = %s",studentID);
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                map.put("STUD_ID", rs.getString("STUD_ID"));
                map.put("FNAME", rs.getString("FNAME"));
                map.put("SNAME", rs.getString("SNAME"));
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (
                SQLException e) {
            System.out.println(e);
        }
        return map;

    }

}

