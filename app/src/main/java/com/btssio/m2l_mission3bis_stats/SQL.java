package com.btssio.m2l_mission3bis_stats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

    public static String url = "jdbc:mysql://212.227.29.168:3306/forma";
    public static String user = "user";
    public static String pass = "gfD!384h3";

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
