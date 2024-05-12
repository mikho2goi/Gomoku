/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Queue;

/**
 *
 * @author ASUS
 */
public class Player {
    
    private int phayerID = 0;
    private String  playerUserName;
    private String playerPassWord;
    private int playerRank = 0;
    private double playerWinRate = 0;
    private double playerScore = 0;
    private int numberOfGame = 0 ;
    private  Queue<Object> unfinishedBoard = null;
    private final int winScore = 2;
    private final int loseScore = 0;
    private final int drawScore = 1;
    public Player(String playerUserName,String playerPassWord) {
        this.playerUserName = playerUserName;
        this.playerPassWord = playerPassWord;
    }

    public Player(int ID, String playerUserName, String playerPassWord, int playerRank, double playerWinRate,double playerScore,int numberOfGame,String unFinishedBoard ) {
        
         this.phayerID = ID;
         this.playerUserName = playerUserName;
         this.playerPassWord = playerPassWord;
         this.playerRank = playerRank;
         this.playerWinRate = playerWinRate;
         this.playerScore = playerScore;
         this.numberOfGame = numberOfGame;
    }
    
    public int getPlayerID() {
        return phayerID;
    }

    public String getPlayerUserName() {
        return playerUserName;
    }

    public String getPlayerPassWord() {
        return playerPassWord;
    }

    public int getPlayerRank() {
        return playerRank;
    }

    public double getPlayerWinRate() {
        return playerWinRate;
    }

    public double getPlayerScore() {
        return playerScore;
    }

        public int getNumberOfGame() {
        return numberOfGame;
    }
    public void setPlayerID(int phayerID) {
        this.phayerID = phayerID;
    }

    public void setPlayerUserName(String playerUserName) {
        this.playerUserName = playerUserName;
    }

    public void setPlayerPassWord(String playerPassWord) {
        this.playerPassWord = playerPassWord;
    }

    public void setPlayerRank(int playerRank) {
        this.playerRank = playerRank;
    }

    public void setPlayerWinRate(double playerWinRate) {
        this.playerWinRate = playerWinRate;
    }

    public void setPlayerScore(double playerScore) {
        this.playerScore = playerScore;
    }
    
    public void setNumberOfGame(int numberOfGame) {
        this.numberOfGame = numberOfGame;
    }
    
    public void setScoreWin(){
        this.playerScore += winScore;
    }
    public void setScoreLose(){
        this.playerScore += loseScore;
    }
    public void setScoreDraw(){
        this.playerScore += drawScore;
    }
    
}
