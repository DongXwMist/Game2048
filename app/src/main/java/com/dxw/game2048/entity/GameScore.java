package com.dxw.game2048.entity;

public class GameScore {

    private int id;
    private String username;
    private int gameScore;

    public GameScore() {
    }

    public GameScore(int id, String username, int gameScore) {
        this.id = id;
        this.username = username;
        this.gameScore = gameScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }
}
