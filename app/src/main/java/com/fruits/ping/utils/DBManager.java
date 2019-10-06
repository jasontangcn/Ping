package com.fruits.ping.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fruits.ping.ChatMessage;
import com.fruits.ping.backup.AudioRecordBackup;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
  public static final String TAG = DBManager.class.getSimpleName();

  private DBHelper helper;
  private SQLiteDatabase db;

  public DBManager(Context context) {
    helper = new DBHelper(context);
    // getWritableDatabase内部调用了context.openOrCreateDatabase(name, 0, factory);
    // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
  }

  public void addAudioRecord(ChatMessage message) {
    //record.setId(UUID.randomUUID().toString());
    db = helper.getWritableDatabase();
    db.beginTransaction();
    try {
      db.execSQL("INSERT INTO messages VALUES(?, ?, ?, ?)", new Object[]{message.getId(), message.getFilePath(), message.getDuration(), message.isPlayed() ? 0 : 1});
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
      db.close();
    }
  }


  public void updateAudioRecord(AudioRecordBackup record) {
    db = helper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("isPlayed", record.isPlayed() ? 0 : 1);
    db.update("messages", values, "id = ?", new String[]{record.getId()});
    db.close();

  }


  public void deleteAudioRecord(AudioRecordBackup record) {
    db = helper.getWritableDatabase();
    db.delete("messages", "id = ?", new String[]{record.getId()});
    db.close();

  }

  /**
   * retrieveAudioRecords all persons, return list
   *
   * @return List<Person>
   */
  public List<AudioRecordBackup> retrieveAudioRecords() {
    db = helper.getWritableDatabase();
    ArrayList<AudioRecordBackup> records = new ArrayList<AudioRecordBackup>();
    Cursor c = db.rawQuery("SELECT * FROM messages", null);
    while (c.moveToNext()) {
      AudioRecordBackup record = new AudioRecordBackup();
      record.setId(c.getString(c.getColumnIndex("id")));
      record.setFilePath(c.getString(c.getColumnIndex("path")));
      record.setDuration(c.getInt(c.getColumnIndex("duration")));
      record.setPlayed(c.getInt(c.getColumnIndex("isPlayed")) == 0 ? true : false);
      records.add(record);
    }
    c.close();
    return records;
  }

  public void close() {
    db.close();
  }
}
