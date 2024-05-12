/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package database;

import java.util.ArrayList;
import model.Player;

/**
 *
 * @author ASUS
 */
public interface DAOInterface<T> {
    
    public int insert(T t);
    
    public void update(T t);
    
    public ArrayList<T> selectAll();
    
    public T selectById(T t);
    
    public ArrayList<T> selectByCondition(String condition);
    
     public boolean checkDuplicated(String username) ;
     
     public Player verifyPlayer(Player player);
}
