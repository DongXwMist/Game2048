package com.dxw.game2048.util;

public class Constant {

    public static final String databaseName = "game2048.db";
    public static final String tableName = "GameScore";

    public static final String GAME2048_ID = "id";
    public static final String GAME2048_USERNAME = "username";
    public static final String GAME2048_GAMESCORE = "gameScore";

    public static final int GameColumnCount = 4;
    public static int CARD_WIDTH = 0;

    //sp文件名称
    public static final String SPNAME = "GameScore";
    public static final String USERNAME = "username";
    public static final String BESTSCORE = "BestScore";

    public static final String CRATE_TABLE =
            "create table " + tableName + "("
                    + GAME2048_ID + " integer primary key autoincrement,"
                    + GAME2048_USERNAME + " varchar(50) unique not null,"
                    + GAME2048_GAMESCORE + " int not null);";

}
