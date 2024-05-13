/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import database.PlayerDAO;
import java.text.DecimalFormat;
import java.util.Queue;

/**
 *
 * @author ASUS
 */
public class Player {

    private int playerID;
    private String playerUserName;
    private String playerPassWord;
    private String playerRank;
    private double playerWinRate;
    private int playerScore = 0;
    private int numberOfGame = 0;
    private Queue<Object> unfinishedBoard = null;
    private final int winScore = 2;
    private final int loseScore = 0;
    private final int drawScore = 1;
    private int winNumber = 0;

    private int loseNumber = 0;

    @Override
    public String toString() {
        return "Player{" + "playerID=" + playerID + ", playerUserName=" + playerUserName + ", playerPassWord=" + playerPassWord + ", playerRank=" + playerRank + ", playerWinRate=" + playerWinRate + ", playerScore=" + playerScore + ", numberOfGame=" + numberOfGame + ", unfinishedBoard=" + unfinishedBoard + ", winScore=" + winScore + ", loseScore=" + loseScore + ", drawScore=" + drawScore + ", winNumber=" + winNumber + ", loseNumber=" + loseNumber + ", drawNumber=" + drawNumber + ", elo=" + elo + '}';
    }
    private int drawNumber = 0;
    private double elo;

    public Player(String playerUserName, String playerPassWord) {
        this.playerUserName = playerUserName;
        this.playerPassWord = playerPassWord;
    }

    public Player(int ID, String playerUserName, String playerPassWord, String playerRank, int numberOfGame, double playerWinRate, int playerScore, int winNumber, int loseNumber, int drawNumber, double elo, String unFinishedBoard) {

        this.playerID = ID;
        this.playerUserName = playerUserName;
        this.playerPassWord = playerPassWord;
        this.playerRank = playerRank;
        this.playerWinRate = playerWinRate;
        this.playerScore = playerScore;
        this.numberOfGame = numberOfGame;
        this.drawNumber = drawNumber;
        this.winNumber = winNumber;
        this.loseNumber = loseNumber;
        this.elo = elo;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getPlayerUserName() {
        return playerUserName;
    }

    public String getPlayerPassWord() {
        return playerPassWord;
    }

    public String getPlayerRank() {
        return playerRank;
    }

    public double getPlayerWinRate() {
        return playerWinRate;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getNumberOfGame() {
        return numberOfGame;
    }

    public void setPlayerID(int phayerID) {
        this.playerID = phayerID;
    }

    public void setPlayerUserName(String playerUserName) {
        this.playerUserName = playerUserName;
    }

    public void setPlayerPassWord(String playerPassWord) {
        this.playerPassWord = playerPassWord;
    }

    public void setPlayerRank(String playerRank) {
        this.playerRank = playerRank;
    }

    public void setPlayerWinRate() {

        DecimalFormat df = new DecimalFormat("#.##");
        if (this.winNumber != 0) {
            double winrate = ((double) this.winNumber / (double) this.numberOfGame) * 100;
            double roundedNumber = Double.parseDouble(df.format(winrate));
            this.playerWinRate = roundedNumber;
        } else {
            this.playerWinRate = 0;
        }
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setNumberOfGame(int numberOfGame) {
        this.numberOfGame = numberOfGame;
    }

    public void setScoreWin() {
        this.playerScore += winScore;
    }

    public void setScoreLose() {
        this.playerScore += loseScore;
    }

    public void setScoreDraw() {
        this.playerScore += drawScore;
    }

    public int getWinNumber() {
        return winNumber;
    }

    public int getLoseNumber() {
        return loseNumber;
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }

    public void setLoseNumber(int loseNumber) {
        this.loseNumber = loseNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public double getElo() {
        return this.elo;
    }

    public void setElo(int score, int botLevel) {
        if (this.elo < 250) {
            if (score == -1) {
                this.elo += score * 5 + botLevel * 10;
            } else {
                this.elo += score * 20 + botLevel * 20;
            }
            this.setPlayerRank("UNRANK");
        } else if (this.elo >= 250 && this.elo <= 350) {
            if (score == -1) {
                this.elo += score * 10 + botLevel * 5;
            } else {
                this.elo += score * 15 + botLevel * 20;
            }
            this.setPlayerRank("BRONZE");

        } else if (this.elo >= 350 && this.elo <= 450) {
            if (score == -1) {
                this.elo += score * 15 + botLevel * 3;
            } else {
                this.elo += score * 10 + botLevel * 15;
            }
            this.setPlayerRank("SILVER");
        } else if (this.elo >= 450 && this.elo <= 600) {
            if (score == -1) {
                this.elo += score * 15 + botLevel * 2;
            } else {
                this.elo += score * 5 + botLevel * 10;
            }
            this.setPlayerRank("*GOLD*");
        } else if (this.elo >= 600 && this.elo <= 800) {
            if (score == -1) {
                this.elo += score * 20 + botLevel;
            } else {
                this.elo += score * 3 + botLevel * 3;
            }
            this.setPlayerRank("**PLATINUM**");

        } else if (this.elo > 800) {
            if (score == -1) {
                this.elo += score * 30 + botLevel;
            } else {
                this.elo += score + botLevel * 2;
            }
            this.setPlayerRank(">>>***DIMOND***<<<");

        }
        PlayerDAO.getInstance().update(this);
    }
    
    
}
// bronze1000 silver150 gold200 platinum250 dimond300
