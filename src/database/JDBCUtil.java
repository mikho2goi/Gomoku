/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

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

//    public static void main(String[] args) throws SQLException {
//        Connection con = JDBCUtil.getConnection();
//        if (con != null) {
//            System.out.println("thanh cong");
//        }else{
//            System.out.println("ngu");
//        }
//        Scanner sc = new Scanner(System.in);
//        String name = sc.nextLine();
//        if (name != "2") {
//            closeConnection(con);
//            System.out.println("đóng kết nối thành công");
//        }else{
//            System.out.println("ngu");
//        }
//    }
}
