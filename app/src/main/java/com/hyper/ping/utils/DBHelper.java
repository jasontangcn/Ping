package com.hyper.ping.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
  private static final int VERSION = 1;

  private static final String DATABASE_NAME = "test.db";
  private static final int DATABASE_VERSION = 1;

  public DBHelper(Context context) {
    // CursorFactory设置为null,使用默认值
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table Record(id varchar(225), path varchar(225), second int, isPlayed int)");// 0:true, 1：false
  }

  // 如果DATABASE_VERSION值被改为2,系统发现与现有数据库版本不同,即会调用onUpgrade
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}
