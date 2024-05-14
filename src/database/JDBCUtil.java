/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ASUS
 */
public class JDBCUtil {

    public static Connection getConnection()  {
        Connection c = null;
        
        try {
            String url = "jdbc:mySQL://localhost:3306/gomoku";
            String username = "root";
            String password = "";
            
            c = DriverManager.getConnection(url, username, password);
            System.out.println("Connect to database sucess");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return c;
    }
    
    public static void closeConnection(Connection c){
        try {
            if(c != null){
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
