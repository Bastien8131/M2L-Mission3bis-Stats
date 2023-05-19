package com.btssio.m2l_mission3bis_stats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

    //Connection Database V1
    /*
    public static String url = "jdbc:mysql://212.227.29.168:3306/crosl";
    public static String user = "res";
    public static String pass = "0L5ieb75&";
     */

    //Connection Database v2
    public static String url = "jdbc:mysql://212.227.29.168:3306/crosl_v2";
    public static String user = "res_v2";
    public static String pass = "#085E9sho";

    public static Statement connexionSQLBDD() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //Class.forName("");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            return st;
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
