package com.dxw.game2048.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dxw.game2048.dao.GameScoreDao;
import com.dxw.game2048.entity.GameScore;
import com.dxw.game2048.util.Constant;
import com.dxw.game2048.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class GameScoreDaoImpl implements GameScoreDao {

    private DBUtil dbUtil;
    private Context mCx;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;

    public GameScoreDaoImpl(Context mCx) {
        Log.i("GameScoreImpl", "方法引用");
        this.mCx = mCx;
        /**
         * 创建数据库
         * */
        dbUtil = new DBUtil(mCx);
        dbReader = dbUtil.getReadableDatabase();
        dbWriter = dbUtil.getWritableDatabase();
    }

    /**
     * 添加数据
     */
    @Override
    public void addGameScore(GameScore g) {

        ContentValues values = new ContentValues();
        values.put("gameScore", g.getGameScore());
        values.put("username", g.getUsername());

        dbWriter.insert(Constant.tableName, null, values);
    }

    /**
     * 删除数据
     */
    @Override
    public void deleteGameScore(int id) {

        dbWriter.delete(Constant.tableName, "id=?",
                new String[]{String.valueOf(id)});

    }

    /**
     * 更新数据
     */
    @Override
    public void updateGameScore(GameScore g) {

        ContentValues values = new ContentValues();
        values.put("id", g.getId());
        values.put("username", g.getUsername());
        values.put("gameScore", g.getGameScore());
        dbWriter.update(Constant.tableName, values, "id=?", new String[]{g.getId() + ""});

    }

    /**
     * 根据id查找数据
     */
    @Override
    public GameScore findById(int id) {

        GameScore g = null;
        Cursor cursor = dbReader.query(Constant.tableName, null, "id=?",
                new String[]{id + ""}, null, null, null);

        if (cursor.moveToNext()) {
            g = new GameScore();
            g.setId(cursor.getInt(0));
            g.setUsername(cursor.getString(1));
            g.setGameScore(cursor.getInt(2));
        }
        cursor.close();
        return g;
    }

    @Override
    public GameScore findByUserName(String username) {
        GameScore g = null;
        Cursor cursor = dbReader.query(Constant.tableName, null, "username=?",
                new String[]{username + ""}, null, null, null);

        if (cursor.moveToNext()) {
            g = new GameScore();
            g.setId(cursor.getInt(0));
            g.setUsername(cursor.getString(1));
            g.setGameScore(cursor.getInt(2));
        }
        cursor.close();
        return g;
    }

    /**
     * 根据username查找数据
     */
    @Override
    public List<GameScore> findAllGameScore() {

        Cursor cursor = dbReader.rawQuery("select * from " + Constant.tableName + " order by "
                + Constant.GAME2048_GAMESCORE + " desc;", null);

        List<GameScore> scoreList = new ArrayList<GameScore>();
        while (cursor.moveToNext()) {
            GameScore g = new GameScore();
            g = new GameScore();
            g.setId(cursor.getInt(0));
            g.setUsername(cursor.getString(1));
            g.setGameScore(cursor.getInt(2));

            scoreList.add(g);
        }
        cursor.close();

        return scoreList;
    }

    public int getCount() {
        Cursor cursor = dbReader.rawQuery("select count(*) from " + Constant.tableName + ";", null);
        int count = -1;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }

}
