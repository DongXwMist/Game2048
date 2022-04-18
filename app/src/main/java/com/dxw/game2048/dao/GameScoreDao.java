package com.dxw.game2048.dao;

import com.dxw.game2048.entity.GameScore;

import java.util.List;

public interface GameScoreDao {

    public void addGameScore(GameScore g);

    public void deleteGameScore(int id);

    public void updateGameScore(GameScore g);

    public GameScore findById(int id);

    public GameScore findByUserName(String username);

    public List<GameScore> findAllGameScore();

}
