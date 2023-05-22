package com.btssio.m2l_mission3bis_stats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
    public static String url = "jdbc:mysql://212.227.29.168:3306/crosl_e5";
    public static String user = "res_e5";
    public static String pass = "!bqw291Z6";

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
