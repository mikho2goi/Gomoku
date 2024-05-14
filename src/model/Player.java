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
    private int numberOfGame = 0;
    private String unfinishedBoard = null;
    private int winNumber = 0;
    private int loseNumber = 0;
    private int drawNumber = 0;
    private double elo;

    public Player(String playerUserName, String playerPassWord) {
        this.playerUserName = playerUserName;
        this.playerPassWord = playerPassWord;
    }

    public Player(int ID, String playerUserName, String playerPassWord, String playerRank, int numberOfGame, double playerWinRate, int winNumber, int loseNumber, int drawNumber, double elo, String unFinishedBoard) {

        this.playerID = ID;
        this.playerUserName = playerUserName;
        this.playerPassWord = playerPassWord;
        this.playerRank = playerRank;
        this.playerWinRate = playerWinRate;
        this.numberOfGame = numberOfGame;
        this.drawNumber = drawNumber;
        this.winNumber = winNumber;
        this.loseNumber = loseNumber;
        this.elo = elo;
        this.unfinishedBoard = unFinishedBoard;
    }

    public String getUnfinishedBoard() {
        return unfinishedBoard;
    }

    public void setUnfinishedBoard(String unfinishedBoard) {
        this.unfinishedBoard = unfinishedBoard;
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

    public void setNumberOfGame(int numberOfGame) {
        this.numberOfGame = numberOfGame;
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
//silver mode vừa w25 L -18, mode dễ w 10 L -15, mode khó l -14 w55

    public void setElo(int score, int botLevel) {
        if (score == 0) {
            this.elo += 0;
        } else if (this.elo < 350) {
            if (score == -1) {
                this.elo += botLevel * 5;
            } else {
                this.elo += botLevel * 25;
            }
        } else if (this.elo >= 350 && this.elo <= 700) {
            if (score == -1) {
                this.elo += botLevel * 10;
            } else {
                this.elo += botLevel * 20;
            }

        } else if (this.elo >= 700 && this.elo <= 1050) {
            if (score == -1) {
                this.elo += botLevel * 10;
            } else {
                this.elo += botLevel * 15;
            }

        } else if (this.elo >= 1050 && this.elo <= 1400) {
            if (score == -1) {
                this.elo += botLevel * 15;
            } else {
                this.elo += botLevel * 10;
            }

        } else if (this.elo >= 1400 && this.elo <= 1750) {
            if (score == -1) {
                this.elo += botLevel * 20;
            } else {
                this.elo += botLevel * 10;
            }

        } else if (this.elo > 1750) {
            if (score == -1) {
                this.elo += botLevel * 25;
            } else {
                this.elo += botLevel * 5;
            }
        }
        setRank();
        PlayerDAO.getInstance().update(this);
    }

    public void setRank() {
        if (this.elo < 350) {
            this.setPlayerRank("UNRANK");
        } else if (this.elo >= 350 && this.elo <= 700) {
            this.setPlayerRank("BRONZE");
        } else if (this.elo >= 700 && this.elo <= 1050) {
            this.setPlayerRank("SILVER");
        } else if (this.elo >= 1050 && this.elo <= 1400) {
            this.setPlayerRank("*GOLD*");
        } else if (this.elo >= 1400 && this.elo <= 1750) {
            this.setPlayerRank("**PLATINUM**");
        } else if (this.elo > 1750) {
            this.setPlayerRank(">>>***DIMOND***<<<");
        }
    }

}
