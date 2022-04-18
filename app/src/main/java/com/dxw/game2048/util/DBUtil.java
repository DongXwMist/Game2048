package com.dxw.game2048.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBUtil extends SQLiteOpenHelper {

    private Context mContext;

    /**
     * 创建数据库
     * */
    public DBUtil(Context mCxt){
        super(mCxt, Constant.databaseName, null,1);
        mContext = mCxt;
    }


    /**
     * 创建表
     * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constant.CRATE_TABLE);
        Log.i("DBUtil","表开始建立");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ Constant.tableName);
        Log.i("DBUtil","表重新建立");
    }
}
