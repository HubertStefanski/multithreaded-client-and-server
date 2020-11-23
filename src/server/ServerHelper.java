package server;

import java.sql.*;
import java.util.Collections;
import java.util.Map;

public class ServerHelper {


    //Create a new connection with the database and return
    public static java.sql.Connection invokeConnection() {
        try {
            //Ensure that the proper class is used for the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Provide the url for the connection along with the proper database.
            String connectionUrl = "jdbc:mysql://localhost:3306/Assign2";
            //Return the connection
            return DriverManager.getConnection(connectionUrl, "root", "");

        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }

// Main authentication function for users
    public static boolean authenticate (String studentID) {

        //create a new flag
        boolean authFlag = false;

        try {
            //Get the connection
            Connection conn = invokeConnection();
            assert conn != null;
            //Create a new statement
            Statement stat = conn.createStatement();

            //Compose the statement using the variable
            String query = String.format("SELECT * FROM `students` WHERE `STUD_ID` = %s",studentID);
            //Execute the query and get the result set
            ResultSet rs = stat.executeQuery(query);

            //Check if the result is not empty,
            if (rs.next()) {
                //Set the flag to true
                authFlag = true;
                //Get tot_req
                int totreq = rs.getInt("TOT_REQ");
                //Increase TOT_REQ by 1
                totreq+=1;

                //compose the update query for TOT_REQ for this user
                String updatequery = String.format("UPDATE `students` SET TOT_REQ = %s  WHERE `STUD_ID` = %s",totreq,studentID);
                //Execite the update
                stat.executeUpdate(updatequery);
            }
            //Close off connections
            rs.close();
            stat.close();
            conn.close();
        } catch (
                SQLException e) {
            System.out.println(e);
        }
        //Return the flag
        return authFlag;

    }

    public static Map<String, String> getUser(String studentID) {
        //Create a new empty map to store data about the current student
        Map<String, String> map = new java.util.HashMap<>(Collections.emptyMap());

        try {
            //Create a new connection
            Connection conn = invokeConnection();
            assert conn != null;
            //Create a new statement
            Statement stat = conn.createStatement();

            //Compose a new query to get the student from the table
            String query = String.format("SELECT * FROM `students` WHERE `STUD_ID` = %s",studentID);
            //Execute the query
            ResultSet rs = stat.executeQuery(query);
            //Check the data exists
            if (rs.next()) {
                //Assign variables and data to the key/val map
                map.put("STUD_ID", rs.getString("STUD_ID"));
                map.put("FNAME", rs.getString("FNAME"));
                map.put("SNAME", rs.getString("SNAME"));
            }
            //Close off connections
            rs.close();
            stat.close();
            conn.close();
        } catch (
                SQLException e) {
            System.out.println(e);
        }
        //Return the map
        return map;

    }

}

