/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import model.Player;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
/**
 *
 * @author ASUS
 */
public class PlayerDAO implements DAOInterface<Player> {

    public static PlayerDAO getInstance() {
        return new PlayerDAO();
    }

    @Override
    public int insert(Player t) {
        int result = 0;
        try {
            //make connection
            Connection con = JDBCUtil.getConnection();
            // statement
            String sql = "INSERT INTO player (ID,playerUserName,playerPassWord) "
                    + "VALUES (?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, t.getPlayerID());
            pst.setString(2, t.getPlayerUserName());
            pst.setString(3, t.getPlayerPassWord());
            //excute
            result = pst.executeUpdate();
         
            //close connection
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public void update(Player t) {
             int result = 0;
        try {
            //make connection
            Connection con = JDBCUtil.getConnection();
            // statement
            String sql = "UPDATE INTO player (ID,playerUserName,playerPassWord) "
                    + "VALUES (?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, t.getPlayerID());
            pst.setString(2, t.getPlayerUserName());
            pst.setString(3, t.getPlayerPassWord());
            //excute
            result = pst.executeUpdate();
         
            //close connection
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public ArrayList<Player> selectAll() {
        ArrayList<Player> playerList = new ArrayList<>();
        try {
           // make connection
            Connection conn = JDBCUtil.getConnection();
            // query
            String sql = "SELECT * FROM player";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            //excute
            ResultSet rs =  pst.executeQuery();
            
            JDBCUtil.closeConnection(conn);
        } catch (Exception e) {
        }
        
        return playerList;
    }

    @Override
    public Player selectById(Player t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Player> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
       public boolean checkDuplicated(String username) {
           Connection con = JDBCUtil.getConnection();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM player WHERE playerUserName = ?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
      
     @Override
     public Player verifyPlayer(Player player) {
         Connection con = JDBCUtil.getConnection();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT *\n"
                    + "FROM player\n"
                    + "WHERE playerUserName = ?\n"
                    + "AND playerPassWord = ?"
            );
            preparedStatement.setString(1, player.getPlayerUserName());
            preparedStatement.setString(2, player.getPlayerPassWord());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Player(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getDouble(6),
                        rs.getInt(7),
                        rs.getString(8));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
     
    
}
